import { Component, DestroyRef, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize, forkJoin } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { PurchaseOrderService } from '../../services/purchase-order.service';
import { PurchaseOrderResponse } from '../../models/purchase-order-response';
import { PurchaseOrderItemResponse } from '../../models/purchase-order-item-response';
import { PurchaseOrderItemCreateRequest } from '../../models/purchase-order-item-create-request';
import { PurchaseOrderItemUpdateRequest } from '../../models/purchase-order-item-update-request';
import { PurchaseOrderStatus } from '../../models/purchase-order-status';
import { ProductService } from '../../../product-inventory/products/services/product.service';
import { Product } from '../../../product-inventory/products/models/product.model';

@Component({
  selector: 'app-purchase-order-detail',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatChipsModule,
    MatDividerModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatSelectModule,
    MatTableModule,
    MatTooltipModule
  ],

  templateUrl: './purchase-order-detail.html',
  styleUrl: './purchase-order-detail.scss'
})
export class PurchaseOrderDetail implements OnInit {
  private readonly purchaseOrderService = inject(PurchaseOrderService);
  private readonly productService = inject(ProductService);
  private readonly formBuilder = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);
  private readonly destroyRef = inject(DestroyRef);

  readonly purchaseOrder = signal<PurchaseOrderResponse | null>(null);
  readonly products = signal<Product[]>([]);
  readonly loading = signal(false);
  readonly errorMessage = signal<string | null>(null);
  readonly showItemForm = signal(false);
  readonly editingItemId = signal<string | null>(null);
  readonly savingItem = signal(false);
  readonly deletingItemId = signal<string | null>(null);

  readonly submitting = signal(false);
  readonly approving = signal(false);
  readonly cancelling = signal(false);

  readonly canManageItems = computed(() => {
    return (this.purchaseOrder()?.status === PurchaseOrderStatus.DRAFT);
    });

  readonly displayedColumns: string[] = [
    'product',
    'orderedQuantity',
    'receivedQuantity',
    'remainingQuantity',
    'unitPrice',
    'lineTotal',
    'actions'
  ];

  readonly itemForm =
    this.formBuilder.nonNullable.group({

      productId: [
        '',
        Validators.required
      ],

      orderedQuantity: [
        1,
        [
          Validators.required,
          Validators.min(0.001)
        ]
      ],

      unitPrice: [
        0,
        [
          Validators.required,
          Validators.min(0)
        ]
      ]
    });

  private purchaseOrderId!: string;

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.router.navigate(['/procurement/purchase-orders']);
      return;
    }

    this.purchaseOrderId = id;
    this.loadPage();
  }

  private loadPage(): void {
    this.loading.set(true);
    this.errorMessage.set(null);

    forkJoin({
      purchaseOrder:
        this.purchaseOrderService
          .getById(this.purchaseOrderId),
      products:
        this.productService
          .getAll(
            0,
            500,
            'name,asc'
          )
    })
      .pipe(finalize(() => {this.loading.set(false);
      }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: result => {
          this.purchaseOrder.set(result.purchaseOrder);
          this.products.set(
            (result.products.content ?? [])
              .filter(product => product.active)
          );
        },

        error: error => {
          console.error(
            'Failed to load purchase order detail',
            error
          );
          this.errorMessage.set(
            error?.error?.message ??
            'Unable to load purchase order.'
          );
        }
      });
  }

  openAddItemForm(): void {
    if (!this.canManageItems()) {
      return;
    }

    this.editingItemId.set(null);
    this.itemForm.controls
      .productId
      .enable();
    this.itemForm.reset({ productId: '', orderedQuantity: 1, unitPrice: 0 });
    this.showItemForm.set(true);
  }

  editItem(
    item: PurchaseOrderItemResponse
  ): void {
    if (!this.canManageItems()) {
      return;
    }
    this.editingItemId.set(
      item.id
    );
    this.itemForm.controls
      .productId
      .enable();
    this.itemForm.reset({
      productId:
      item.productId,
      orderedQuantity:
      item.orderedQuantity,
      unitPrice:
      item.unitPrice
    });

    this.itemForm.controls
      .productId
      .disable();
    this.showItemForm.set(true);
  }

  cancelItemForm(): void {
    this.showItemForm.set(false);
    this.editingItemId.set(null);
    this.itemForm.controls
      .productId
      .enable();
    this.itemForm.reset({
      productId: '',
      orderedQuantity: 1,
      unitPrice: 0
    });
  }

  saveItem(): void {

    if (
      this.itemForm.invalid || this.savingItem() || !this.canManageItems()) {
      this.itemForm.markAllAsTouched();
      return;
    }
    const value = this.itemForm.getRawValue();
    const editingId = this.editingItemId();
    this.savingItem.set(true);

    if (editingId) {
      const request: PurchaseOrderItemUpdateRequest = {
        orderedQuantity:
        value.orderedQuantity,
        unitPrice:
        value.unitPrice
      };

      this.purchaseOrderService
        .updateItem(this.purchaseOrderId, editingId, request)
        .pipe(
          finalize(() => {this.savingItem.set(false);}),
          takeUntilDestroyed(this.destroyRef)
        )
        .subscribe({

          next: purchaseOrder => {this.purchaseOrder.set(purchaseOrder);
            this.cancelItemForm();
            this.snackBar.open(
              'Purchase order item updated successfully.',
              'Close',
              {
                duration: 3000
              }
            );
          },

          error: error => {
            console.error(
              'Failed to update purchase order item',
              error
            );
            this.snackBar.open(
              error?.error?.message ??
              'Unable to update purchase order item.',
              'Close',
              {
                duration: 4000
              }
            );
          }
        });

      return;
    }

    const request:
      PurchaseOrderItemCreateRequest = {
      productId:
      value.productId,
      orderedQuantity:
      value.orderedQuantity,
      unitPrice:
      value.unitPrice
    };

    this.purchaseOrderService
      .addItem(this.purchaseOrderId, request)
      .pipe(finalize(() => {this.savingItem.set(false);}),
        takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: purchaseOrder => {
          this.purchaseOrder.set(purchaseOrder);
          this.cancelItemForm();
          this.snackBar.open(
            'Product added to purchase order.',
            'Close',
            {
              duration: 3000
            }
          );
        },

        error: error => {
          console.error(
            'Failed to add purchase order item',
            error
          );
          this.snackBar.open(
            error?.error?.message ??
            'Unable to add product to purchase order.',
            'Close',
            {
              duration: 4000
            }
          );
        }
      });
  }

  removeItem(item: PurchaseOrderItemResponse): void {
    if (
      !this.canManageItems() || this.deletingItemId()) {
      return;
    }

    const confirmed = window.confirm(
        `Remove ${item.productName} from this purchase order?`
      );

    if (!confirmed) {
      return;
    }

    this.deletingItemId.set(item.id);
    this.purchaseOrderService
      .removeItem(this.purchaseOrderId, item.id)
      .pipe(finalize(() => {this.deletingItemId.set(null);}),
        takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: purchaseOrder => {
          this.purchaseOrder.set(purchaseOrder);
          if (this.editingItemId() === item.id) {
            this.cancelItemForm();
          }

          this.snackBar.open(
            'Purchase order item removed.',
            'Close',
            {
              duration: 3000
            }
          );
        },

        error: error => {
          console.error(
            'Failed to remove purchase order item', error);

          this.snackBar.open(
            error?.error?.message ??
            'Unable to remove purchase order item.',
            'Close',
            {
              duration: 4000
            }
          );
        }
      });
  }

  productAlreadyUsed(
    productId: string
  ): boolean {

    return (
      this.purchaseOrder()
        ?.items
        ?.some(
          item =>
            item.productId === productId
        ) ?? false
    );
  }


  canSubmit(): boolean {
    const po = this.purchaseOrder();

    return (!!po && po.status === PurchaseOrderStatus.DRAFT && po.items.length > 0);
  }

  canApprove(): boolean {
    return (this.purchaseOrder()?.status === PurchaseOrderStatus.SUBMITTED);
  }

  canCancel(): boolean {
    const status = this.purchaseOrder()?.status;

    return (status === PurchaseOrderStatus.DRAFT ||
      status === PurchaseOrderStatus.SUBMITTED ||
      status === PurchaseOrderStatus.APPROVED
    );
  }

  submitPurchaseOrder(): void {

    if (!this.canSubmit() || this.submitting()) {
      return;
    }

    const confirmed = window.confirm(
        'Submit this purchase order for approval?'
      );

    if (!confirmed) {
      return;
    }

    this.submitting.set(true);
    this.purchaseOrderService
      .submit(this.purchaseOrderId)
      .pipe(finalize(() => {this.submitting.set(false);}),
        takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: purchaseOrder => {
          this.purchaseOrder.set(purchaseOrder);
          this.cancelItemForm();
          this.snackBar.open(
            'Purchase order submitted successfully.',
            'Close',
            {
              duration: 3000
            }
          );
        },

        error: error => {
          console.error(
            'Failed to submit purchase order',
            error
          );
          this.snackBar.open(
            error?.error?.message ??
            'Unable to submit purchase order.',
            'Close',
            {
              duration: 4000
            }
          );
        }
      });
  }

  approvePurchaseOrder(): void {
    if (!this.canApprove() || this.approving()) {
      return;
    }

    const confirmed = window.confirm(
        'Approve this purchase order?'
      );

    if (!confirmed) {
      return;
    }

    this.approving.set(true);

    this.purchaseOrderService
      .approve(this.purchaseOrderId)
      .pipe(finalize(() => {this.approving.set(false);}),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({

        next: purchaseOrder => {
          this.purchaseOrder.set(purchaseOrder);
          this.snackBar.open(
            'Purchase order approved successfully.',
            'Close',
            {
              duration: 3000
            }
          );
        },

        error: error => {

          console.error(
            'Failed to approve purchase order',
            error
          );

          this.snackBar.open(
            error?.error?.message ??
            'Unable to approve purchase order.',
            'Close',
            {
              duration: 4000
            }
          );
        }
      });
  }

  cancelPurchaseOrder(): void {

    if (!this.canCancel() || this.cancelling()) {
      return;
    }

    const confirmed = window.confirm(
        'Cancel this purchase order?'
      );

    if (!confirmed) {
      return;
    }

    this.cancelling.set(true);
    this.purchaseOrderService
      .cancel(this.purchaseOrderId)
      .pipe(finalize(() => {this.cancelling.set(false);}),
        takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: purchaseOrder => {
          this.purchaseOrder.set(purchaseOrder);
          this.cancelItemForm();
          this.snackBar.open(
            'Purchase order cancelled successfully.',
            'Close',
            {
              duration: 3000
            }
          );
        },

        error: error => {

          console.error(
            'Failed to cancel purchase order',
            error
          );

          this.snackBar.open(
            error?.error?.message ??
            'Unable to cancel purchase order.',
            'Close',
            {
              duration: 4000
            }
          );
        }
      });
  }

  editPurchaseOrder(): void {
    if (!this.canManageItems()) {
      return;
    }

    this.router.navigate([
      '/procurement/purchase-orders',
      this.purchaseOrderId,
      'edit'
    ]);
  }

  back(): void {

    this.router.navigate([
      '/procurement/purchase-orders'
    ]);
  }

  statusLabel(
    status: PurchaseOrderStatus
  ): string {

    switch (status) {

      case PurchaseOrderStatus.DRAFT:
        return 'Draft';

      case PurchaseOrderStatus.SUBMITTED:
        return 'Submitted';

      case PurchaseOrderStatus.APPROVED:
        return 'Approved';

      case PurchaseOrderStatus.PARTIALLY_RECEIVED:
        return 'Partially Received';

      case PurchaseOrderStatus.RECEIVED:
        return 'Received';

      case PurchaseOrderStatus.CANCELLED:
        return 'Cancelled';

      default:
        return status;
    }
  }
}
