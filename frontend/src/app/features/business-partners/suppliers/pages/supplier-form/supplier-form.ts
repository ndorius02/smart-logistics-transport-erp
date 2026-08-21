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

import { SupplierService } from '../../services/supplier.service';
import { SupplierCreateRequest } from '../../models/supplier-create-request.model';
import { SupplierUpdateRequest } from '../../models/supplier-update-request.model';

@Component({
  selector: 'app-supplier-form',
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
  templateUrl: './supplier-form.html',
  styleUrl: './supplier-form.scss'
})
export class SupplierForm implements OnInit {

  private readonly fb =
    inject(FormBuilder);

  private readonly supplierService =
    inject(SupplierService);

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

  readonly supplierId =
    signal<string | null>(null);

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

      active: [
        true
      ]
    });

  get isEditMode(): boolean {
    return this.supplierId() !== null;
  }

  ngOnInit(): void {

    const id =
      this.route.snapshot.paramMap.get('id');

    if (id) {
      this.supplierId.set(id);
      this.loadSupplier(id);
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
      this.updateSupplier();
    } else {
      this.createSupplier();
    }
  }

  cancel(): void {
    this.router.navigate([
      '/business-partners/suppliers'
    ]);
  }

  private loadSupplier(
    id: string
  ): void {

    this.loading.set(true);

    this.supplierService
      .getById(id)
      .subscribe({

        next: supplier => {

          this.form.patchValue({

            code:
            supplier.code,

            companyName:
            supplier.companyName,

            contactName:
              supplier.contactName ?? '',

            email:
              supplier.email ?? '',

            phoneNumber:
              supplier.phoneNumber ?? '',

            address:
            supplier.address,

            city:
            supplier.city,

            postalCode:
              supplier.postalCode ?? '',

            country:
            supplier.country,

            vatNumber:
              supplier.vatNumber ?? '',

            active:
            supplier.active
          });

          this.loading.set(false);
        },

        error: error => {

          console.error(
            'Unable to load supplier',
            error
          );

          this.errorMessage.set(
            'Unable to load supplier'
          );

          this.loading.set(false);
        }
      });
  }

  private createSupplier(): void {

    const value =
      this.form.getRawValue();

    const request:
      SupplierCreateRequest = {

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
        this.toNullable(value.vatNumber)
    };

    this.supplierService
      .create(request)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Supplier created successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.router.navigate([
            '/business-partners/suppliers'
          ]);
        },

        error: error => {

          this.handleError(
            error,
            'Unable to create supplier'
          );
        }
      });
  }

  private updateSupplier(): void {

    const id =
      this.supplierId();

    if (!id) {
      return;
    }

    const value =
      this.form.getRawValue();

    const request:
      SupplierUpdateRequest = {

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

      active:
      value.active
    };

    this.supplierService
      .update(
        id,
        request
      )
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Supplier updated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.router.navigate([
            '/business-partners/suppliers'
          ]);
        },

        error: error => {

          this.handleError(
            error,
            'Unable to update supplier'
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
