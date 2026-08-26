import {
  Component,
  inject,
  OnInit,
  signal
} from '@angular/core';

import { Router } from '@angular/router';

import { MatTableModule } from '@angular/material/table';
import {
  MatPaginatorModule,
  PageEvent
} from '@angular/material/paginator';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';

import {
  MatSnackBar,
  MatSnackBarModule
} from '@angular/material/snack-bar';

import {
  StockMovement,
  StockMovementType
} from '../../models/stock-movement.model';

import { StockMovementService } from '../../services/stock-movement.service';

import { Product } from '../../../products/models/product.model';
import { ProductService } from '../../../products/services/product.service';

import { Warehouse } from '../../../../warehouses/models/warehouse.model';
import { WarehouseService } from '../../../../warehouses/services/warehouse.service';

import { AuthService } from '../../../../../core/auth/auth';

@Component({
  selector: 'app-stock-movement-list',
  standalone: true,
  imports: [
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatSelectModule,
    MatSnackBarModule
  ],
  templateUrl: './stock-movement-list.html',
  styleUrl: './stock-movement-list.scss'
})
export class StockMovementList implements OnInit {

  private readonly stockMovementService =
    inject(StockMovementService);

  private readonly productService =
    inject(ProductService);

  private readonly warehouseService =
    inject(WarehouseService);

  private readonly router =
    inject(Router);

  private readonly authService =
    inject(AuthService);

  private readonly snackBar =
    inject(MatSnackBar);

  readonly movements =
    signal<StockMovement[]>([]);

  readonly products =
    signal<Product[]>([]);

  readonly warehouses =
    signal<Warehouse[]>([]);

  readonly loading =
    signal(false);

  readonly totalElements =
    signal(0);

  readonly pageIndex =
    signal(0);

  readonly pageSize =
    signal(10);

  readonly selectedProductId =
    signal('');

  readonly selectedWarehouseId =
    signal('');

  readonly selectedMovementType =
    signal<StockMovementType | ''>('');

  readonly movementTypes: StockMovementType[] = [
    'STOCK_IN',
    'STOCK_OUT',
    'ADJUSTMENT_IN',
    'ADJUSTMENT_OUT'
  ];

  readonly displayedColumns: string[] = [
    'reference',
    'product',
    'warehouse',
    'movementType',
    'quantity',
    'reason',
    'movementDate',
    'createdBy'
  ];

  ngOnInit(): void {
    this.loadReferenceData();
    this.loadMovements();
  }

  loadMovements(): void {

    this.loading.set(true);

    const productId =
      this.selectedProductId();

    const warehouseId =
      this.selectedWarehouseId();

    const movementType =
      this.selectedMovementType();

    let request$;

    if (
      productId
      && warehouseId
    ) {

      request$ =
        this.stockMovementService
          .getByProductAndWarehouse(
            productId,
            warehouseId,
            this.pageIndex(),
            this.pageSize()
          );

    } else if (productId) {

      request$ =
        this.stockMovementService
          .getByProduct(
            productId,
            this.pageIndex(),
            this.pageSize()
          );

    } else if (warehouseId) {

      request$ =
        this.stockMovementService
          .getByWarehouse(
            warehouseId,
            this.pageIndex(),
            this.pageSize()
          );

    } else if (movementType) {

      request$ =
        this.stockMovementService
          .getByType(
            movementType,
            this.pageIndex(),
            this.pageSize()
          );

    } else {

      request$ =
        this.stockMovementService
          .getAll(
            this.pageIndex(),
            this.pageSize()
          );
    }

    request$.subscribe({

      next: response => {

        this.movements.set(
          response.content
        );

        this.totalElements.set(
          response.totalElements
        );

        this.loading.set(false);
      },

      error: error => {

        console.error(
          'Unable to load stock movements',
          error
        );

        this.loading.set(false);

        this.showBackendError(
          error,
          'Unable to load stock movements'
        );
      }
    });
  }

  private loadReferenceData(): void {

    this.productService
      .getAll(
        0,
        100,
        'name,asc'
      )
      .subscribe({

        next: response => {

          this.products.set(
            response.content
          );
        },

        error: error => {

          console.error(
            'Unable to load products',
            error
          );
        }
      });

    this.warehouseService
      .getAll(
        0,
        100
      )
      .subscribe({

        next: response => {

          this.warehouses.set(
            response.content
          );
        },

        error: error => {

          console.error(
            'Unable to load warehouses',
            error
          );
        }
      });
  }

  applyFilters(): void {
    this.pageIndex.set(0);
    this.loadMovements();
  }

  clearFilters(): void {

    this.selectedProductId.set('');
    this.selectedWarehouseId.set('');
    this.selectedMovementType.set('');

    this.pageIndex.set(0);

    this.loadMovements();
  }

  onPageChange(
    event: PageEvent
  ): void {

    this.pageIndex.set(
      event.pageIndex
    );

    this.pageSize.set(
      event.pageSize
    );

    this.loadMovements();
  }

  createMovement(): void {

    this.router.navigate([
      '/product-inventory/stock-movements/new'
    ]);
  }

  canManageStockMovements(): boolean {

    return this.authService.hasAnyRole([
      'ROLE_ADMIN',
      'ROLE_MANAGER',
      'ROLE_WAREHOUSE_OFFICER'
    ]);
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
