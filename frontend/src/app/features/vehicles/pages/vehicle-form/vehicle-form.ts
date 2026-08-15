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

import { VehicleService } from '../../services/vehicle.service';

import {
  VehicleCreateRequest
} from '../../models/vehicle-create-request.model';

import {
  VehicleUpdateRequest
} from '../../models/vehicle-update-request.model';

import {
  VehicleType
} from '../../models/vehicle-type.model';

import {
  VehicleStatus
} from '../../models/vehicle-status.model';

@Component({
  selector: 'app-vehicle-form',
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
  templateUrl: './vehicle-form.html',
  styleUrl: './vehicle-form.scss'
})
export class VehicleForm implements OnInit {

  private readonly fb =
    inject(FormBuilder);

  private readonly vehicleService =
    inject(VehicleService);

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

  readonly vehicleId =
    signal<string | null>(null);

  readonly vehicleTypes =
    Object.values(VehicleType);

  readonly vehicleStatuses =
    Object.values(VehicleStatus);

  readonly form =
    this.fb.nonNullable.group({

      registrationNumber: [
        '',
        [
          Validators.required,
          Validators.maxLength(50)
        ]
      ],

      brand: [
        '',
        [
          Validators.required,
          Validators.maxLength(100)
        ]
      ],

      model: [
        '',
        [
          Validators.required,
          Validators.maxLength(100)
        ]
      ],

      vehicleType: [
        VehicleType.TRUCK,
        [
          Validators.required
        ]
      ],

      loadCapacity: [
        1,
        [
          Validators.required,
          Validators.min(1)
        ]
      ],

      operationalStatus: [
        VehicleStatus.AVAILABLE,
        [
          Validators.required
        ]
      ]
    });

  get isEditMode(): boolean {
    return this.vehicleId() !== null;
  }

  ngOnInit(): void {

    const id =
      this.route.snapshot.paramMap.get('id');

    if (id) {
      this.vehicleId.set(id);
      this.loadVehicle(id);
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
      this.updateVehicle();
    } else {
      this.createVehicle();
    }
  }

  cancel(): void {
    this.router.navigate([
      '/vehicles'
    ]);
  }

  private loadVehicle(
    id: string
  ): void {

    this.loading.set(true);

    this.vehicleService
      .getById(id)
      .subscribe({

        next: vehicle => {

          this.form.patchValue({
            registrationNumber:
            vehicle.registrationNumber,

            brand:
            vehicle.brand,

            model:
            vehicle.model,

            vehicleType:
            vehicle.vehicleType,

            loadCapacity:
            vehicle.loadCapacity,

            operationalStatus:
            vehicle.operationalStatus
          });

          this.loading.set(false);
        },

        error: () => {

          this.errorMessage.set(
            'Unable to load vehicle'
          );

          this.loading.set(false);
        }
      });
  }

  private createVehicle(): void {

    const value =
      this.form.getRawValue();

    const request:
      VehicleCreateRequest = {

      registrationNumber:
      value.registrationNumber,

      brand:
      value.brand,

      model:
      value.model,

      vehicleType:
      value.vehicleType,

      loadCapacity:
      value.loadCapacity
    };

    this.vehicleService
      .create(request)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Vehicle created successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.router.navigate([
            '/vehicles'
          ]);
        },

        error: error => {
          this.handleError(
            error,
            'Unable to create vehicle'
          );
        }
      });
  }

  private updateVehicle(): void {

    const id =
      this.vehicleId();

    if (!id) {
      return;
    }

    const value =
      this.form.getRawValue();

    const request:
      VehicleUpdateRequest = {

      registrationNumber:
      value.registrationNumber,

      brand:
      value.brand,

      model:
      value.model,

      vehicleType:
      value.vehicleType,

      loadCapacity:
      value.loadCapacity,

      operationalStatus:
      value.operationalStatus
    };

    this.vehicleService
      .update(
        id,
        request
      )
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Vehicle updated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.router.navigate([
            '/vehicles'
          ]);
        },

        error: error => {
          this.handleError(
            error,
            'Unable to update vehicle'
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
