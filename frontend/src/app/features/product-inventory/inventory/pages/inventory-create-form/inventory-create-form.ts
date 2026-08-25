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

import { Router } from '@angular/router';

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

import { InventoryService } from '../../services/inventory.service';
import { InventoryCreateRequest } from '../../models/inventory-create-request.model';

import { Product } from '../../../products/models/product.model';
import { ProductService } from '../../../products/services/product.service';

import { Warehouse } from '../../../../warehouses/models/warehouse.model';
import { WarehouseService } from '../../../../warehouses/services/warehouse.service';

@Component({
  selector: 'app-inventory-create-form',
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
  templateUrl: './inventory-create-form.html',
  styleUrl: './inventory-create-form.scss'
})
export class InventoryCreateForm implements OnInit {

  private readonly fb =
    inject(FormBuilder);

  private readonly inventoryService =
    inject(InventoryService);

  private readonly productService =
    inject(ProductService);

  private readonly warehouseService =
    inject(WarehouseService);

  private readonly router =
    inject(Router);

  private readonly snackBar =
    inject(MatSnackBar);

  readonly products =
    signal<Product[]>([]);

  readonly warehouses =
    signal<Warehouse[]>([]);

  readonly loadingReferenceData =
    signal(false);

  readonly saving =
    signal(false);

  readonly form = this.fb.nonNullable.group({

    productId: [
      '',
      Validators.required
    ],

    warehouseId: [
      '',
      Validators.required
    ],

    quantity: [
      0,
      [
        Validators.required,
        Validators.min(0)
      ]
    ],

    reservedQuantity: [
      0,
      [
        Validators.required,
        Validators.min(0)
      ]
    ],

    minimumStockLevel: [
      0,
      [
        Validators.required,
        Validators.min(0)
      ]
    ]

  });

  ngOnInit(): void {
    this.loadReferenceData();
  }

  private loadReferenceData(): void {

    this.loadingReferenceData.set(true);

    this.productService
      .getAll(
        0,
        100,
        'name,asc'
      )
      .subscribe({

        next: response => {

          this.products.set(
            response.content.filter(
              product => product.active
            )
          );

          this.loadWarehouses();
        },

        error: error => {

          this.loadingReferenceData.set(false);

          this.showBackendError(
            error,
            'Unable to load products'
          );
        }
      });
  }

  private loadWarehouses(): void {

    this.warehouseService
      .getAll(
        0,
        100
      )
      .subscribe({

        next: response => {

          this.warehouses.set(
            response.content.filter(
              warehouse => warehouse.active
            )
          );

          this.loadingReferenceData.set(false);
        },

        error: error => {

          this.loadingReferenceData.set(false);

          this.showBackendError(
            error,
            'Unable to load warehouses'
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

    const value =
      this.form.getRawValue();

    if (
      value.reservedQuantity
      > value.quantity
    ) {

      this.snackBar.open(
        'Reserved quantity cannot exceed physical quantity',
        'Close',
        {
          duration: 5000
        }
      );

      return;
    }

    const request: InventoryCreateRequest = {

      productId:
      value.productId,

      warehouseId:
      value.warehouseId,

      quantity:
      value.quantity,

      reservedQuantity:
      value.reservedQuantity,

      minimumStockLevel:
      value.minimumStockLevel
    };

    this.saving.set(true);

    this.inventoryService
      .create(request)
      .subscribe({

        next: () => {

          this.saving.set(false);

          this.snackBar.open(
            'Inventory position created successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.router.navigate([
            '/product-inventory/inventory'
          ]);
        },

        error: error => {

          this.saving.set(false);

          this.showBackendError(
            error,
            'Unable to create inventory position'
          );
        }
      });
  }

  cancel(): void {

    this.router.navigate([
      '/product-inventory/inventory'
    ]);
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
