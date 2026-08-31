import { Component, DestroyRef, OnInit, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { GoodsReceptionService } from '../../services/goods-reception.service';
import { PurchaseOrderService } from '../../services/purchase-order.service';
import { GoodsReceptionCreateRequest } from '../../models/goods-reception-create-request';
import { PurchaseOrderResponse } from '../../models/purchase-order-response';
import { PurchaseOrderItemResponse } from '../../models/purchase-order-item-response';
import { PurchaseOrderStatus } from '../../models/purchase-order-status';

@Component({
  selector: 'app-goods-reception-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatProgressSpinnerModule
  ],

  templateUrl: './goods-reception-form.html',
  styleUrl: './goods-reception-form.scss'
})
export class GoodsReceptionForm implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly goodsReceptionService = inject(GoodsReceptionService);
  private readonly purchaseOrderService = inject(PurchaseOrderService);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);
  private readonly destroyRef = inject(DestroyRef);

  readonly purchaseOrders = signal<PurchaseOrderResponse[]>([]);
  readonly selectedPurchaseOrderId = signal<string | null>(null);
  readonly selectedPurchaseOrderItemId = signal<string | null>(null);
  readonly loading = signal(false);
  readonly saving = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly form =
    this.formBuilder.nonNullable.group({

      reference: [
        '',
        [
          Validators.required,
          Validators.maxLength(50)
        ]
      ],

      purchaseOrderId: [
        '',
        Validators.required
      ],

      purchaseOrderItemId: [
        '',
        Validators.required
      ],

      quantity: [
        0,
        [
          Validators.required,
          Validators.min(0.001)
        ]
      ],

      notes: [
        '',
        Validators.maxLength(500)
      ]
    });

  readonly selectedPurchaseOrder =
    computed<PurchaseOrderResponse | null>(() => {
      const id = this.selectedPurchaseOrderId();
      if (!id) {
        return null;
      }
      return (
        this.purchaseOrders()
          .find(
            purchaseOrder => purchaseOrder.id === id
          ) ?? null
      );
    });

  readonly availableItems =
    computed<PurchaseOrderItemResponse[]>(() => {
      const purchaseOrder = this.selectedPurchaseOrder();
      if (!purchaseOrder) {
        return [];
      }
      return (
        purchaseOrder.items ?? []
      ).filter(
        item => item.remainingQuantity > 0
      );
    });

  readonly selectedItem =
    computed<PurchaseOrderItemResponse | null>(() => {
      const itemId = this.selectedPurchaseOrderItemId();
      if (!itemId) {
        return null;
      }
      return (this.availableItems()
          .find(
            item => item.id === itemId
          ) ?? null
      );
    });

  ngOnInit(): void {this.loadEligiblePurchaseOrders();}

  private loadEligiblePurchaseOrders(): void {
    this.loading.set(true);
    this.errorMessage.set(null);
    this.purchaseOrderService
      .getAll(0, 100)
      .pipe(
        finalize(() => {this.loading.set(false);
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: page => {
          const eligiblePurchaseOrders =
            (page.content ?? [])
              .filter(
                purchaseOrder => {
                  const eligibleStatus = purchaseOrder.status ===
                    PurchaseOrderStatus.APPROVED ||
                    purchaseOrder.status ===
                    PurchaseOrderStatus.PARTIALLY_RECEIVED;
                  const hasRemainingItems =
                    (purchaseOrder.items ?? [])
                      .some(
                        item =>
                          item.remainingQuantity > 0
                      );
                  return (
                    eligibleStatus && hasRemainingItems);
                }
              );
          this.purchaseOrders.set(eligiblePurchaseOrders);
        },

        error: error => {
          console.error(
            'Failed to load eligible purchase orders',
            error
          );
          this.purchaseOrders.set([]);
          this.errorMessage.set(
            error?.error?.message ??
            'Unable to load eligible purchase orders.'
          );
        }
      });
  }

  onPurchaseOrderChange(purchaseOrderId: string): void {
    this.selectedPurchaseOrderId.set(purchaseOrderId
    );
    this.selectedPurchaseOrderItemId.set(null);

    this.form.controls
      .purchaseOrderItemId
      .setValue('');
    this.form.controls
      .quantity
      .setValue(0);
  }

  onItemChange(
    purchaseOrderItemId: string
  ): void {
    this.selectedPurchaseOrderItemId.set(
      purchaseOrderItemId
    );
    this.form.controls
      .quantity
      .setValue(0);
  }

  save(): void {
    if (this.form.invalid || this.saving()) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    const item = this.selectedItem();
    if (!item) {
      this.snackBar.open(
        'Please select a valid purchase order item.',
        'Close',
        {
          duration: 4000
        }
      );
      return;
    }

    if (
      value.quantity > item.remainingQuantity) {
      this.snackBar.open(
        `Quantity cannot exceed remaining quantity (${item.remainingQuantity}).`,
        'Close',
        {
          duration: 4000
        }
      );
      return;
    }

    const request:
      GoodsReceptionCreateRequest = {
      reference:
        value.reference
          .trim()
          .toUpperCase(),
      purchaseOrderItemId:
      value.purchaseOrderItemId,
      quantity:
      value.quantity,
      notes:
        value.notes.trim() || null
    };

    this.saving.set(true);
    this.goodsReceptionService
      .receive(request)
      .pipe(finalize(() => {this.saving.set(false);}),
        takeUntilDestroyed(
          this.destroyRef
        )
      )
      .subscribe({
        next: reception => {
          this.snackBar.open(
            'Goods reception recorded successfully.',
            'Close',
            {
              duration: 3000
            }
          );
          this.router.navigate([
            '/procurement/purchase-orders',
            reception.purchaseOrderId
          ]);
        },
        error: error => {
          console.error(
            'Failed to record goods reception',
            error
          );
          this.snackBar.open(
            error?.error?.message ??
            'Unable to record goods reception.',
            'Close',
            {
              duration: 4000
            }
          );
        }
      });
  }

  cancel(): void {
    this.router.navigate([
      '/procurement/goods-receptions'
    ]);
  }

  purchaseOrderLabel(purchaseOrder: PurchaseOrderResponse): string {
    return (
      `${purchaseOrder.orderNumber} - ` +
      `${purchaseOrder.supplierName} - ` +
      `${purchaseOrder.warehouseName}`
    );
  }

  itemLabel(item: PurchaseOrderItemResponse): string {

    return (
      `${item.productSku} - ` +
      `${item.productName} ` +
      `(${item.remainingQuantity} remaining)`
    );
  }
}
