import {
  Component,
  inject,
  OnInit,
  signal
} from '@angular/core';

import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  ActivatedRoute,
  Router
} from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import {
  MatSnackBar,
  MatSnackBarModule
} from '@angular/material/snack-bar';

import { DriverService } from '../../services/driver.service';
import { DriverCreateRequest } from '../../models/driver-create-request.model';
import { DriverUpdateRequest } from '../../models/driver-update-request.model';
import { DriverStatus } from '../../models/driver-status.model';

@Component({
  selector: 'app-driver-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './driver-form.html',
  styleUrl: './driver-form.scss'
})
export class DriverForm implements OnInit {

  private readonly fb =
    inject(FormBuilder);

  private readonly driverService =
    inject(DriverService);

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly snackBar =
    inject(MatSnackBar);

  readonly loading =
    signal(false);

  readonly errorMessage =
    signal<string | null>(null);

  readonly driverId =
    signal<string | null>(null);

  readonly driverStatuses =
    Object.values(DriverStatus);

  readonly form =
    this.fb.nonNullable.group({

      firstName: [
        '',
        [
          Validators.required,
          Validators.maxLength(100)
        ]
      ],

      lastName: [
        '',
        [
          Validators.required,
          Validators.maxLength(100)
        ]
      ],

      licenseNumber: [
        '',
        [
          Validators.required,
          Validators.maxLength(50)
        ]
      ],

      phoneNumber: [
        '',
        [
          Validators.required,
          Validators.maxLength(30)
        ]
      ],

      status: [
        DriverStatus.AVAILABLE,
        [
          Validators.required
        ]
      ]
    });

  get isEditMode(): boolean {
    return this.driverId() !== null;
  }

  ngOnInit(): void {

    const id =
      this.route.snapshot.paramMap.get('id');

    if (id) {
      this.driverId.set(id);
      this.loadDriver(id);
    }
  }

  submit(): void {

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set(null);

    if (this.isEditMode) {
      this.updateDriver();
    } else {
      this.createDriver();
    }
  }

  cancel(): void {
    this.router.navigate([
      '/drivers'
    ]);
  }

  private loadDriver(
    id: string
  ): void {

    this.loading.set(true);

    this.driverService
      .getById(id)
      .subscribe({

        next: driver => {

          this.form.patchValue({
            firstName: driver.firstName,
            lastName: driver.lastName,
            licenseNumber: driver.licenseNumber,
            phoneNumber: driver.phoneNumber,
            status: driver.status
          });

          this.loading.set(false);
        },

        error: () => {

          this.errorMessage.set(
            'Unable to load driver'
          );

          this.loading.set(false);
        }
      });
  }

  private createDriver(): void {

    const value =
      this.form.getRawValue();

    const request:
      DriverCreateRequest = {

      firstName:
      value.firstName,

      lastName:
      value.lastName,

      licenseNumber:
      value.licenseNumber,

      phoneNumber:
      value.phoneNumber
    };

    this.driverService
      .create(request)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Driver created successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.router.navigate([
            '/drivers'
          ]);
        },

        error: error => {
          this.handleError(
            error,
            'Unable to create driver'
          );
        }
      });
  }

  private updateDriver(): void {

    const id =
      this.driverId();

    if (!id) {
      return;
    }

    const value =
      this.form.getRawValue();

    const request:
      DriverUpdateRequest = {

      firstName:
      value.firstName,

      lastName:
      value.lastName,

      licenseNumber:
      value.licenseNumber,

      phoneNumber:
      value.phoneNumber,

      status:
      value.status
    };

    this.driverService
      .update(
        id,
        request
      )
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Driver updated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.router.navigate([
            '/drivers'
          ]);
        },

        error: error => {
          this.handleError(
            error,
            'Unable to update driver'
          );
        }
      });
  }

  private handleError(
    error: any,
    fallbackMessage: string
  ): void {

    this.loading.set(false);

    const backendMessage =
      error?.error?.message;

    this.errorMessage.set(
      backendMessage ??
      fallbackMessage
    );
  }
}
