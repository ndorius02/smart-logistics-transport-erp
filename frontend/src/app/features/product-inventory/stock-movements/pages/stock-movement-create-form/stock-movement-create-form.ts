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

import {
  StockMovementType
} from '../../models/stock-movement.model';

import {
  StockMovementCreateRequest
} from '../../models/stock-movement-create-request.model';

import {
  StockMovementService
} from '../../services/stock-movement.service';

import { Product } from '../../../products/models/product.model';
import { ProductService } from '../../../products/services/product.service';

import { Warehouse } from '../../../../warehouses/models/warehouse.model';
import { WarehouseService } from '../../../../warehouses/services/warehouse.service';

@Component({
  selector: 'app-stock-movement-create-form',
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
  templateUrl: './stock-movement-create-form.html',
  styleUrl: './stock-movement-create-form.scss'
})
export class StockMovementCreateForm implements OnInit {

  private readonly fb =
    inject(FormBuilder);

  private readonly stockMovementService =
    inject(StockMovementService);

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

  readonly movementTypes: StockMovementType[] = [
    'STOCK_IN',
    'STOCK_OUT',
    'ADJUSTMENT_IN',
    'ADJUSTMENT_OUT'
  ];

  readonly form = this.fb.nonNullable.group({

    reference: [
      '',
      [
        Validators.required,
        Validators.maxLength(50)
      ]
    ],

    productId: [
      '',
      Validators.required
    ],

    warehouseId: [
      '',
      Validators.required
    ],

    movementType: [
      '' as StockMovementType,
      Validators.required
    ],

    quantity: [
      null as number | null,
      [
        Validators.required,
        Validators.min(0.001)
      ]
    ],

    reason: [
      '',
      [
        Validators.maxLength(255)
      ]
    ],

    notes: [
      '',
      [
        Validators.maxLength(500)
      ]
    ]

  });

  ngOnInit(): void {

    this.loadReferenceData();

    this.form.controls.movementType
      .valueChanges
      .subscribe(type => {
        this.updateReasonValidation(type);
      });
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

  private updateReasonValidation(
    type: StockMovementType
  ): void {

    const reasonControl =
      this.form.controls.reason;

    const adjustment =
      type === 'ADJUSTMENT_IN'
      || type === 'ADJUSTMENT_OUT';

    if (adjustment) {

      reasonControl.setValidators([
        Validators.required,
        Validators.maxLength(255)
      ]);

    } else {

      reasonControl.setValidators([
        Validators.maxLength(255)
      ]);
    }

    reasonControl.updateValueAndValidity({
      emitEvent: false
    });
  }

  isAdjustment(): boolean {

    const type =
      this.form.controls.movementType.value;

    return type === 'ADJUSTMENT_IN'
      || type === 'ADJUSTMENT_OUT';
  }

  movementLabel(
    type: StockMovementType
  ): string {

    switch (type) {

      case 'STOCK_IN':
        return 'Stock In';

      case 'STOCK_OUT':
        return 'Stock Out';

      case 'ADJUSTMENT_IN':
        return 'Adjustment In';

      case 'ADJUSTMENT_OUT':
        return 'Adjustment Out';
    }
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

    if (value.quantity === null) {
      return;
    }

    const request: StockMovementCreateRequest = {

      reference:
        value.reference
          .trim()
          .toUpperCase(),

      productId:
      value.productId,

      warehouseId:
      value.warehouseId,

      movementType:
      value.movementType,

      quantity:
      value.quantity,

      reason:
        value.reason.trim() || null,

      notes:
        value.notes.trim() || null
    };

    this.saving.set(true);

    this.stockMovementService
      .create(request)
      .subscribe({

        next: () => {

          this.saving.set(false);

          this.snackBar.open(
            'Stock movement created successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.router.navigate([
            '/product-inventory/stock-movements'
          ]);
        },

        error: error => {

          this.saving.set(false);

          this.showBackendError(
            error,
            'Unable to create stock movement'
          );
        }
      });
  }

  cancel(): void {

    this.router.navigate([
      '/product-inventory/stock-movements'
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
