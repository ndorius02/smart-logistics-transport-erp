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
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import {
  MatSnackBar,
  MatSnackBarModule
} from '@angular/material/snack-bar';

import { CarrierService } from '../../services/carrier.service';
import { CarrierCreateRequest } from '../../models/carrier-create-request.model';
import { CarrierUpdateRequest } from '../../models/carrier-update-request.model';

@Component({
  selector: 'app-carrier-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatSlideToggleModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './carrier-form.html',
  styleUrl: './carrier-form.scss'
})
export class CarrierForm implements OnInit {

  private readonly fb = inject(FormBuilder);
  private readonly carrierService = inject(CarrierService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  readonly loading = signal(false);
  readonly errorMessage = signal<string | null>(null);
  readonly carrierId = signal<string | null>(null);

  readonly form =
    this.fb.nonNullable.group({

      code: [
        '',
        [
          Validators.required,
          Validators.maxLength(50)
        ]
      ],

      companyName: [
        '',
        [
          Validators.required,
          Validators.maxLength(150)
        ]
      ],

      contactName: [
        '',
        [
          Validators.maxLength(150)
        ]
      ],

      email: [
        '',
        [
          Validators.email,
          Validators.maxLength(150)
        ]
      ],

      phoneNumber: [
        '',
        [
          Validators.maxLength(30)
        ]
      ],

      address: [
        '',
        [
          Validators.required,
          Validators.maxLength(255)
        ]
      ],

      city: [
        '',
        [
          Validators.required,
          Validators.maxLength(100)
        ]
      ],

      postalCode: [
        '',
        [
          Validators.maxLength(20)
        ]
      ],

      country: [
        '',
        [
          Validators.required,
          Validators.maxLength(100)
        ]
      ],

      vatNumber: [
        '',
        [
          Validators.maxLength(50)
        ]
      ],

      licenseNumber: [
        '',
        [
          Validators.required,
          Validators.maxLength(100)
        ]
      ],

      active: [
        true
      ]
    });

  get isEditMode(): boolean {
    return this.carrierId() !== null;
  }

  ngOnInit(): void {

    const id =
      this.route.snapshot.paramMap.get('id');

    if (id) {
      this.carrierId.set(id);
      this.loadCarrier(id);
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
      this.updateCarrier();
    } else {
      this.createCarrier();
    }
  }

  cancel(): void {
    this.router.navigate([
      '/business-partners/carriers'
    ]);
  }

  private loadCarrier(
    id: string
  ): void {

    this.loading.set(true);

    this.carrierService
      .getById(id)
      .subscribe({

        next: carrier => {

          this.form.patchValue({

            code:
            carrier.code,

            companyName:
            carrier.companyName,

            contactName:
              carrier.contactName ?? '',

            email:
              carrier.email ?? '',

            phoneNumber:
              carrier.phoneNumber ?? '',

            address:
            carrier.address,

            city:
            carrier.city,

            postalCode:
              carrier.postalCode ?? '',

            country:
            carrier.country,

            vatNumber:
              carrier.vatNumber ?? '',

            licenseNumber:
            carrier.licenseNumber,

            active:
            carrier.active
          });

          this.loading.set(false);
        },

        error: error => {

          console.error(
            'Unable to load carrier',
            error
          );

          this.errorMessage.set(
            'Unable to load carrier'
          );

          this.loading.set(false);
        }
      });
  }

  private createCarrier(): void {

    const value =
      this.form.getRawValue();

    const request:
      CarrierCreateRequest = {

      code:
      value.code,

      companyName:
      value.companyName,

      contactName:
        this.toNullable(value.contactName),

      email:
        this.toNullable(value.email),

      phoneNumber:
        this.toNullable(value.phoneNumber),

      address:
      value.address,

      city:
      value.city,

      postalCode:
        this.toNullable(value.postalCode),

      country:
      value.country,

      vatNumber:
        this.toNullable(value.vatNumber),

      licenseNumber:
      value.licenseNumber
    };

    this.carrierService
      .create(request)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Carrier created successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.router.navigate([
            '/business-partners/carriers'
          ]);
        },

        error: error => {

          this.handleError(
            error,
            'Unable to create carrier'
          );
        }
      });
  }

  private updateCarrier(): void {

    const id =
      this.carrierId();

    if (!id) {
      return;
    }

    const value =
      this.form.getRawValue();

    const request:
      CarrierUpdateRequest = {

      code:
      value.code,

      companyName:
      value.companyName,

      contactName:
        this.toNullable(value.contactName),

      email:
        this.toNullable(value.email),

      phoneNumber:
        this.toNullable(value.phoneNumber),

      address:
      value.address,

      city:
      value.city,

      postalCode:
        this.toNullable(value.postalCode),

      country:
      value.country,

      vatNumber:
        this.toNullable(value.vatNumber),

      licenseNumber:
      value.licenseNumber,

      active:
      value.active
    };

    this.carrierService
      .update(
        id,
        request
      )
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Carrier updated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.router.navigate([
            '/business-partners/carriers'
          ]);
        },

        error: error => {

          this.handleError(
            error,
            'Unable to update carrier'
          );
        }
      });
  }

  private toNullable(
    value: string
  ): string | null {

    const normalized =
      value.trim();

    return normalized.length > 0
      ? normalized
      : null;
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
