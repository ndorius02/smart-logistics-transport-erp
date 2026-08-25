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
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import {
  MatSnackBar,
  MatSnackBarModule
} from '@angular/material/snack-bar';

import { ProductCategoryService } from '../../services/product-category.service';
import { ProductCategoryCreateRequest } from '../../models/product-category-create-request.model';
import { ProductCategoryUpdateRequest } from '../../models/product-category-update-request.model';

@Component({
  selector: 'app-product-category-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './product-category-form.html',
  styleUrl: './product-category-form.scss'
})
export class ProductCategoryForm implements OnInit {

  private readonly fb =
    inject(FormBuilder);

  private readonly productCategoryService =
    inject(ProductCategoryService);

  private readonly router =
    inject(Router);

  private readonly route =
    inject(ActivatedRoute);

  private readonly snackBar =
    inject(MatSnackBar);

  readonly loading =
    signal(false);

  readonly saving =
    signal(false);

  readonly editMode =
    signal(false);

  private categoryId: string | null = null;

  readonly form = this.fb.nonNullable.group({

    code: [
      '',
      [
        Validators.required,
        Validators.maxLength(50)
      ]
    ],

    name: [
      '',
      [
        Validators.required,
        Validators.maxLength(100)
      ]
    ],

    description: [
      '',
      [
        Validators.maxLength(500)
      ]
    ]

  });

  ngOnInit(): void {

    this.categoryId =
      this.route.snapshot.paramMap.get('id');

    if (this.categoryId) {
      this.editMode.set(true);
      this.loadCategory(this.categoryId);
    }
  }

  private loadCategory(
    id: string
  ): void {

    this.loading.set(true);

    this.productCategoryService
      .getById(id)
      .subscribe({

        next: category => {

          this.form.patchValue({
            code: category.code,
            name: category.name,
            description:
              category.description ?? ''
          });

          this.loading.set(false);
        },

        error: error => {

          console.error(
            'Unable to load product category',
            error
          );

          this.loading.set(false);

          this.showBackendError(
            error,
            'Unable to load product category'
          );
        }
      });
  }

  save(): void {

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    if (this.saving()) {
      return;
    }

    this.saving.set(true);

    const value =
      this.form.getRawValue();

    if (
      this.editMode()
      && this.categoryId
    ) {

      const request: ProductCategoryUpdateRequest = {
        code: value.code.trim(),
        name: value.name.trim(),
        description:
          value.description.trim() || null,
        active: true
      };

      this.productCategoryService
        .update(
          this.categoryId,
          request
        )
        .subscribe({

          next: () => {
            this.handleSuccess(
              'Product category updated successfully'
            );
          },

          error: error => {
            this.handleSaveError(
              error,
              'Unable to update product category'
            );
          }
        });

      return;
    }

    const request: ProductCategoryCreateRequest = {
      code: value.code.trim(),
      name: value.name.trim(),
      description:
        value.description.trim() || null
    };

    this.productCategoryService
      .create(request)
      .subscribe({

        next: () => {
          this.handleSuccess(
            'Product category created successfully'
          );
        },

        error: error => {
          this.handleSaveError(
            error,
            'Unable to create product category'
          );
        }
      });
  }

  cancel(): void {

    this.router.navigate([
      '/product-inventory/product-categories'
    ]);
  }

  private handleSuccess(
    message: string
  ): void {

    this.saving.set(false);

    this.snackBar.open(
      message,
      'Close',
      {
        duration: 3000
      }
    );

    this.router.navigate([
      '/product-inventory/product-categories'
    ]);
  }

  private handleSaveError(
    error: any,
    fallbackMessage: string
  ): void {

    this.saving.set(false);

    this.showBackendError(
      error,
      fallbackMessage
    );
  }

  private showBackendError(
    error: any,
    fallbackMessage: string
  ): void {

    const backendMessage =
      error?.error?.message;

    this.snackBar.open(
      backendMessage ?? fallbackMessage,
      'Close',
      {
        duration: 5000
      }
    );
  }
}
