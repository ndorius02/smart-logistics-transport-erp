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

import { Inventory } from '../../models/inventory.model';
import { InventoryService } from '../../services/inventory.service';

import { Product } from '../../../products/models/product.model';
import { ProductService } from '../../../products/services/product.service';

import { AuthService } from '../../../../../core/auth/auth';

@Component({
  selector: 'app-inventory-list',
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
  templateUrl: './inventory-list.html',
  styleUrl: './inventory-list.scss'
})
export class InventoryList implements OnInit {

  private readonly inventoryService =
    inject(InventoryService);

  private readonly productService =
    inject(ProductService);

  private readonly router =
    inject(Router);

  private readonly authService =
    inject(AuthService);

  private readonly snackBar =
    inject(MatSnackBar);

  readonly inventory =
    signal<Inventory[]>([]);

  readonly products =
    signal<Product[]>([]);

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

  readonly lowStockOnly =
    signal(false);

  readonly displayedColumns: string[] = [
    'product',
    'warehouse',
    'quantity',
    'reservedQuantity',
    'availableQuantity',
    'minimumStockLevel',
    'stockStatus',
    'actions'
  ];

  ngOnInit(): void {
    this.loadProducts();
    this.loadInventory();
  }

  loadInventory(): void {

    this.loading.set(true);

    let request$;

    if (this.lowStockOnly()) {

      request$ =
        this.inventoryService.getLowStock(
          this.pageIndex(),
          this.pageSize()
        );

    } else if (this.selectedProductId()) {

      request$ =
        this.inventoryService.getByProduct(
          this.selectedProductId(),
          this.pageIndex(),
          this.pageSize()
        );

    } else {

      request$ =
        this.inventoryService.getAll(
          this.pageIndex(),
          this.pageSize()
        );
    }

    request$.subscribe({

      next: response => {

        this.inventory.set(
          response.content
        );

        this.totalElements.set(
          response.totalElements
        );

        this.loading.set(false);
      },

      error: error => {

        console.error(
          'Unable to load inventory',
          error
        );

        this.loading.set(false);

        this.showBackendError(
          error,
          'Unable to load inventory'
        );
      }
    });
  }

  private loadProducts(): void {

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
        },

        error: error => {

          console.error(
            'Unable to load products',
            error
          );
        }
      });
  }

  onProductChange(
    productId: string
  ): void {

    this.selectedProductId.set(
      productId
    );

    this.lowStockOnly.set(false);

    this.pageIndex.set(0);

    this.loadInventory();
  }

  showLowStock(): void {

    this.selectedProductId.set('');

    this.lowStockOnly.set(true);

    this.pageIndex.set(0);

    this.loadInventory();
  }

  showAll(): void {

    this.selectedProductId.set('');

    this.lowStockOnly.set(false);

    this.pageIndex.set(0);

    this.loadInventory();
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

    this.loadInventory();
  }

  createInventoryPosition(): void {

    this.router.navigate([
      '/product-inventory/inventory/new'
    ]);
  }

  updateMinimumStock(
    item: Inventory
  ): void {

    this.router.navigate([
      '/product-inventory/inventory',
      item.id,
      'minimum-stock'
    ]);
  }

  canManageInventory(): boolean {

    return this.authService.hasAnyRole([
      'ROLE_ADMIN',
      'ROLE_MANAGER',
      'ROLE_WAREHOUSE_OFFICER'
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
