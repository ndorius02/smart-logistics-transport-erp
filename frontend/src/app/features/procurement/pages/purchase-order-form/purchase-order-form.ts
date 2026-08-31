import { Component, DestroyRef, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, finalize } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { PurchaseOrderService } from '../../services/purchase-order.service';
import { PurchaseOrderCreateRequest } from '../../models/purchase-order-create-request';
import { PurchaseOrderUpdateRequest } from '../../models/purchase-order-update-request';
import { SupplierService } from '../../../business-partners/suppliers/services/supplier.service';
import { Supplier } from '../../../business-partners/suppliers/models/supplier.model';
import { WarehouseService } from '../../../warehouses/services/warehouse.service';
import { Warehouse } from '../../../warehouses/models/warehouse.model';

@Component({
  selector: 'app-purchase-order-form',
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

  templateUrl: './purchase-order-form.html',
  styleUrl: './purchase-order-form.scss'
})
export class PurchaseOrderForm implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly purchaseOrderService = inject(PurchaseOrderService);
  private readonly supplierService = inject(SupplierService);
  private readonly warehouseService = inject(WarehouseService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);
  private readonly destroyRef = inject(DestroyRef);

  readonly suppliers = signal<Supplier[]>([]);
  readonly warehouses = signal<Warehouse[]>([]);
  readonly loading = signal(false);
  readonly loadingReferenceData = signal(false);
  readonly saving = signal(false);
  readonly editMode = signal(false);
  readonly errorMessage = signal<string | null>(null);
  private purchaseOrderId:
    string | null = null;

  readonly form =
    this.formBuilder.nonNullable.group({

      orderNumber: [
        '',
        [
          Validators.required,
          Validators.maxLength(50)
        ]
      ],

      supplierId: [
        '',
        Validators.required
      ],

      warehouseId: [
        '',
        Validators.required
      ],

      expectedDeliveryDate: [
        ''
      ],

      notes: [
        '',
        Validators.maxLength(500)
      ]
    });

  ngOnInit(): void {

    this.purchaseOrderId =
      this.route.snapshot.paramMap.get('id');

    this.editMode.set(
      this.purchaseOrderId !== null
    );

    this.loadReferenceData();

    if (
      this.editMode() &&
      this.purchaseOrderId
    ) {

      this.loadPurchaseOrder(
        this.purchaseOrderId
      );
    }
  }

  private loadReferenceData(): void {
    this.loadingReferenceData.set(true);
    this.errorMessage.set(null);

    forkJoin({
      suppliers:
        this.supplierService.getAll(0, 100, 'companyName,asc'),
      warehouses:
        this.warehouseService.getAll(0, 100, 'name,asc')
    })
      .pipe(finalize(() => {this.loadingReferenceData.set(false);}),
        takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: result => {
          this.suppliers.set((result.suppliers.content ?? [])
              .filter(supplier => supplier.active)
          );

          this.warehouses.set(
            (result.warehouses.content ?? [])
              .filter(warehouse => warehouse.active)
          );
        },

        error: error => {
          console.error(
            'Failed to load purchase order reference data',
            error
          );
          this.errorMessage.set(
            error?.error?.message ??
            'Unable to load suppliers and warehouses.'
          );
          this.suppliers.set([]);
          this.warehouses.set([]);
        }
      });
  }

  private loadPurchaseOrder(
    id: string
  ): void {
    this.loading.set(true);
    this.errorMessage.set(null);
    this.purchaseOrderService
      .getById(id)
      .pipe(finalize(() => {this.loading.set(false);}),
        takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: purchaseOrder => {
          this.form.patchValue({
            orderNumber:
            purchaseOrder.orderNumber,
            supplierId:
            purchaseOrder.supplierId,
            warehouseId:
            purchaseOrder.warehouseId,
            expectedDeliveryDate:
              purchaseOrder.expectedDeliveryDate ?? '',
            notes:
              purchaseOrder.notes ?? ''
          });

          this.form.controls
            .orderNumber
            .disable();
        },
        error: error => {
          console.error(
            'Failed to load purchase order',
            error
          );
          this.snackBar.open(
            error?.error?.message ??
            'Unable to load purchase order.',
            'Close',
            {
              duration: 4000
            }
          );

          this.router.navigate([
            '/procurement/purchase-orders'
          ]);
        }
      });
  }

  save(): void {

    if (this.form.invalid || this.saving()) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    this.saving.set(true);

    if (this.editMode() && this.purchaseOrderId) {
      const request:
        PurchaseOrderUpdateRequest = {
        supplierId:
        value.supplierId,
        warehouseId:
        value.warehouseId,
        expectedDeliveryDate:
          value.expectedDeliveryDate || null,
        notes:
          value.notes.trim() || null
      };
      this.purchaseOrderService
        .update(
          this.purchaseOrderId,
          request
        )
        .pipe(

          finalize(() => {
            this.saving.set(false);
          }),

          takeUntilDestroyed(this.destroyRef)
        )
        .subscribe({
          next: purchaseOrder => {

            this.snackBar.open(
              'Purchase order updated successfully.',
              'Close',
              {
                duration: 3000
              }
            );
            this.router.navigate([
              '/procurement/purchase-orders',
              purchaseOrder.id
            ]);
          },

          error: error => {
            console.error(
              'Failed to update purchase order',
              error
            );
            this.snackBar.open(
              error?.error?.message ??
              'Unable to update purchase order.',
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
      PurchaseOrderCreateRequest = {
      orderNumber:
        value.orderNumber
          .trim()
          .toUpperCase(),

      supplierId:
      value.supplierId,
      warehouseId:
      value.warehouseId,
      expectedDeliveryDate:
        value.expectedDeliveryDate || null,
      notes:
        value.notes.trim() || null
    };


    this.purchaseOrderService
      .create(request)
      .pipe(finalize(() => {this.saving.set(false);}),
        takeUntilDestroyed(this.destroyRef))
      .subscribe({

        next: purchaseOrder => {
          this.snackBar.open(
            'Purchase order created successfully.',
            'Close',
            {
              duration: 3000
            }
          );

          this.router.navigate([
            '/procurement/purchase-orders',
            purchaseOrder.id
          ]);
        },

        error: error => {
          console.error(
            'Failed to create purchase order',
            error
          );
          this.snackBar.open(
            error?.error?.message ??
            'Unable to create purchase order.',
            'Close',
            {
              duration: 4000
            }
          );
        }
      });
  }

  cancel(): void {

    if (this.purchaseOrderId) {

      this.router.navigate([
        '/procurement/purchase-orders',
        this.purchaseOrderId
      ]);

      return;
    }

    this.router.navigate([
      '/procurement/purchase-orders'
    ]);
  }
}
