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

import { CustomerService } from '../../services/customer.service';
import { CustomerCreateRequest } from '../../models/customer-create-request.model';
import { CustomerUpdateRequest } from '../../models/customer-update-request.model';

@Component({
  selector: 'app-customer-form',
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
  templateUrl: './customer-form.html',
  styleUrl: './customer-form.scss'
})
export class CustomerForm implements OnInit {

  private readonly fb =
    inject(FormBuilder);

  private readonly customerService =
    inject(CustomerService);

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

  readonly customerId =
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
    return this.customerId() !== null;
  }

  ngOnInit(): void {

    const id =
      this.route.snapshot.paramMap.get('id');

    if (id) {
      this.customerId.set(id);
      this.loadCustomer(id);
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
      this.updateCustomer();
    } else {
      this.createCustomer();
    }
  }

  cancel(): void {
    this.router.navigate([
      '/business-partners/customers'
    ]);
  }

  private loadCustomer(
    id: string
  ): void {

    this.loading.set(true);

    this.customerService
      .getById(id)
      .subscribe({

        next: customer => {

          this.form.patchValue({

            code:
            customer.code,

            companyName:
            customer.companyName,

            contactName:
              customer.contactName ?? '',

            email:
              customer.email ?? '',

            phoneNumber:
              customer.phoneNumber ?? '',

            address:
            customer.address,

            city:
            customer.city,

            postalCode:
              customer.postalCode ?? '',

            country:
            customer.country,

            vatNumber:
              customer.vatNumber ?? '',

            active:
            customer.active
          });

          this.loading.set(false);
        },

        error: error => {

          console.error(
            'Unable to load customer',
            error
          );

          this.errorMessage.set(
            'Unable to load customer'
          );

          this.loading.set(false);
        }
      });
  }

  private createCustomer(): void {

    const value =
      this.form.getRawValue();

    const request:
      CustomerCreateRequest = {

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

    this.customerService
      .create(request)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Customer created successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.router.navigate([
            '/business-partners/customers'
          ]);
        },

        error: error => {

          this.handleError(
            error,
            'Unable to create customer'
          );
        }
      });
  }

  private updateCustomer(): void {

    const id =
      this.customerId();

    if (!id) {
      return;
    }

    const value =
      this.form.getRawValue();

    const request:
      CustomerUpdateRequest = {

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

    this.customerService
      .update(
        id,
        request
      )
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Customer updated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.router.navigate([
            '/business-partners/customers'
          ]);
        },

        error: error => {

          this.handleError(
            error,
            'Unable to update customer'
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
