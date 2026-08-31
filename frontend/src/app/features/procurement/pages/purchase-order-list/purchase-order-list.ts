import { Component, DestroyRef, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { finalize } from 'rxjs/operators';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { PurchaseOrderService } from '../../services/purchase-order.service';
import { PurchaseOrderResponse } from '../../models/purchase-order-response';
import { PurchaseOrderStatus } from '../../models/purchase-order-status';

@Component({
  selector: 'app-purchase-order-list',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatChipsModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatTooltipModule
  ],

  templateUrl: './purchase-order-list.html',
  styleUrl: './purchase-order-list.scss'
})
export class PurchaseOrderList implements OnInit {
  private readonly purchaseOrderService = inject(PurchaseOrderService);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);
  readonly purchaseOrders = signal<PurchaseOrderResponse[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly errorMessage = signal<string | null>(null);

  readonly displayedColumns: string[] = [
    'orderNumber',
    'supplier',
    'warehouse',
    'orderDate',
    'expectedDeliveryDate',
    'status',
    'totalAmount',
    'actions'
  ];

  pageIndex = 0;
  pageSize = 10;

  ngOnInit(): void {this.loadPurchaseOrders();}
    loadPurchaseOrders(): void {
    this.loading.set(true);
    this.errorMessage.set(null);
    this.purchaseOrderService
      .getAll(this.pageIndex, this.pageSize)
      .pipe(finalize(() => {this.loading.set(false);}),
        takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: page => {
          this.purchaseOrders.set(
            page?.content ?? []
          );
          this.totalElements.set(
            page?.totalElements ?? 0
          );
        },

        error: error => {
          console.error(
            'Failed to load purchase orders',
            error
          );
          this.purchaseOrders.set([]);
          this.totalElements.set(0);
          this.errorMessage.set(
            error?.error?.message ??
            'Unable to load purchase orders.'
          );
        }
      });
  }

  onPageChange(
    event: PageEvent
  ): void {
    this.pageIndex =
      event.pageIndex;
    this.pageSize =
      event.pageSize;
    this.loadPurchaseOrders();
  }

  createPurchaseOrder(): void {
    this.router.navigate([
      '/procurement/purchase-orders/new'
    ]);
  }

  viewPurchaseOrder(
    id: string
  ): void {

    this.router.navigate([
      '/procurement/purchase-orders',
      id
    ]);
  }

  editPurchaseOrder(
    id: string
  ): void {

    this.router.navigate([
      '/procurement/purchase-orders',
      id,
      'edit'
    ]);
  }

  canEdit(
    purchaseOrder: PurchaseOrderResponse
  ): boolean {
    return (
      purchaseOrder.status ===
      PurchaseOrderStatus.DRAFT
    );
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
