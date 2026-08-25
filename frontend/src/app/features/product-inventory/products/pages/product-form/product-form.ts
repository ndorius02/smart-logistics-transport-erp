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

import {
  Product,
  UnitOfMeasure
} from '../../models/product.model';

import { ProductCreateRequest } from '../../models/product-create-request.model';
import { ProductUpdateRequest } from '../../models/product-update-request.model';
import { ProductService } from '../../services/product.service';

import { ProductCategory } from '../../../product-categories/models/product-category.model';
import { ProductCategoryService } from '../../../product-categories/services/product-category.service';

@Component({
  selector: 'app-product-form',
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
  templateUrl: './product-form.html',
  styleUrl: './product-form.scss'
})
export class ProductForm implements OnInit {

  private readonly fb =
    inject(FormBuilder);

  private readonly productService =
    inject(ProductService);

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

  readonly categories =
    signal<ProductCategory[]>([]);

  private productId: string | null = null;

  private currentActive = true;

  readonly unitsOfMeasure: UnitOfMeasure[] = [
    'PIECE',
    'BOX',
    'PALLET',
    'KILOGRAM',
    'LITER',
    'METER'
  ];

  readonly form = this.fb.nonNullable.group({

    sku: [
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
        Validators.maxLength(150)
      ]
    ],

    description: [
      '',
      [
        Validators.maxLength(500)
      ]
    ],

    categoryId: [
      '',
      Validators.required
    ],

    unitOfMeasure: [
      '' as UnitOfMeasure,
      Validators.required
    ],

    weight: [
      null as number | null,
      [
        Validators.min(0)
      ]
    ]

  });

  ngOnInit(): void {

    this.loadCategories();

    this.productId =
      this.route.snapshot.paramMap.get('id');

    if (this.productId) {

      this.editMode.set(true);

      this.loadProduct(
        this.productId
      );
    }
  }

  private loadCategories(): void {

    this.productCategoryService
      .getAll(
        0,
        100,
        'name,asc'
      )
      .subscribe({

        next: response => {

          this.categories.set(
            response.content.filter(
              category => category.active
            )
          );
        },

        error: error => {

          console.error(
            'Unable to load product categories',
            error
          );

          this.showBackendError(
            error,
            'Unable to load product categories'
          );
        }
      });
  }

  private loadProduct(
    id: string
  ): void {

    this.loading.set(true);

    this.productService
      .getById(id)
      .subscribe({

        next: (product: Product) => {

          this.currentActive =
            product.active;

          this.form.patchValue({

            sku:
            product.sku,

            name:
            product.name,

            description:
              product.description ?? '',

            categoryId:
            product.categoryId,

            unitOfMeasure:
            product.unitOfMeasure,

            weight:
            product.weight

          });

          this.loading.set(false);
        },

        error: error => {

          console.error(
            'Unable to load product',
            error
          );

          this.loading.set(false);

          this.showBackendError(
            error,
            'Unable to load product'
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
      && this.productId
    ) {

      const request: ProductUpdateRequest = {

        sku:
          value.sku.trim(),

        name:
          value.name.trim(),

        description:
          value.description.trim() || null,

        categoryId:
        value.categoryId,

        unitOfMeasure:
        value.unitOfMeasure,

        weight:
        value.weight,

        active:
        this.currentActive
      };

      this.productService
        .update(
          this.productId,
          request
        )
        .subscribe({

          next: () => {

            this.handleSuccess(
              'Product updated successfully'
            );
          },

          error: error => {

            this.handleSaveError(
              error,
              'Unable to update product'
            );
          }
        });

      return;
    }

    const request: ProductCreateRequest = {

      sku:
        value.sku.trim(),

      name:
        value.name.trim(),

      description:
        value.description.trim() || null,

      categoryId:
      value.categoryId,

      unitOfMeasure:
      value.unitOfMeasure,

      weight:
      value.weight
    };

    this.productService
      .create(request)
      .subscribe({

        next: () => {

          this.handleSuccess(
            'Product created successfully'
          );
        },

        error: error => {

          this.handleSaveError(
            error,
            'Unable to create product'
          );
        }
      });
  }

  cancel(): void {

    this.router.navigate([
      '/product-inventory/products'
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
      '/product-inventory/products'
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
