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
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

import {
  MatSnackBar,
  MatSnackBarModule
} from '@angular/material/snack-bar';

import { Product } from '../../models/product.model';
import { ProductService } from '../../services/product.service';

import { ProductCategory } from '../../../product-categories/models/product-category.model';
import { ProductCategoryService } from '../../../product-categories/services/product-category.service';

import { AuthService } from '../../../../../core/auth/auth';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatSnackBarModule
  ],
  templateUrl: './product-list.html',
  styleUrl: './product-list.scss'
})
export class ProductList implements OnInit {

  private readonly productService =
    inject(ProductService);

  private readonly productCategoryService =
    inject(ProductCategoryService);

  private readonly router =
    inject(Router);

  private readonly authService =
    inject(AuthService);

  private readonly snackBar =
    inject(MatSnackBar);

  readonly products =
    signal<Product[]>([]);

  readonly categories =
    signal<ProductCategory[]>([]);

  readonly loading =
    signal(false);

  readonly totalElements =
    signal(0);

  readonly pageIndex =
    signal(0);

  readonly pageSize =
    signal(10);

  readonly searchTerm =
    signal('');

  readonly searchType =
    signal<'name' | 'sku'>('name');

  readonly selectedCategoryId =
    signal('');

  readonly displayedColumns: string[] = [
    'sku',
    'name',
    'category',
    'unitOfMeasure',
    'weight',
    'status',
    'actions'
  ];

  ngOnInit(): void {
    this.loadCategories();
    this.loadProducts();
  }

  loadProducts(): void {

    this.loading.set(true);

    const term =
      this.searchTerm().trim();

    const categoryId =
      this.selectedCategoryId();

    let request$;

    /*
     * For V1 we apply one server-side filter at a time.
     * Category has priority over text search.
     */
    if (categoryId) {

      request$ =
        this.productService.getByCategory(
          categoryId,
          this.pageIndex(),
          this.pageSize()
        );

    } else if (term) {

      if (this.searchType() === 'sku') {

        request$ =
          this.productService.searchBySku(
            term,
            this.pageIndex(),
            this.pageSize()
          );

      } else {

        request$ =
          this.productService.searchByName(
            term,
            this.pageIndex(),
            this.pageSize()
          );
      }

    } else {

      request$ =
        this.productService.getAll(
          this.pageIndex(),
          this.pageSize()
        );
    }

    request$.subscribe({

      next: response => {

        this.products.set(
          response.content
        );

        this.totalElements.set(
          response.totalElements
        );

        this.loading.set(false);
      },

      error: error => {

        console.error(
          'Unable to load products',
          error
        );

        this.loading.set(false);

        this.showBackendError(
          error,
          'Unable to load products'
        );
      }
    });
  }

  loadCategories(): void {

    /*
     * We only need reference data for the filter.
     * Six categories currently exist, so 100 is
     * sufficient for this V1 screen.
     */
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
        }
      });
  }

  search(): void {
    this.pageIndex.set(0);
    this.loadProducts();
  }

  onCategoryChange(
    categoryId: string
  ): void {

    this.selectedCategoryId.set(
      categoryId
    );

    this.pageIndex.set(0);

    this.loadProducts();
  }

  clearFilters(): void {

    this.searchTerm.set('');
    this.searchType.set('name');
    this.selectedCategoryId.set('');

    this.pageIndex.set(0);

    this.loadProducts();
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

    this.loadProducts();
  }

  createProduct(): void {

    this.router.navigate([
      '/product-inventory/products/new'
    ]);
  }

  editProduct(
    product: Product
  ): void {

    this.router.navigate([
      '/product-inventory/products',
      product.id,
      'edit'
    ]);
  }

  activateProduct(
    product: Product
  ): void {

    this.productService
      .activate(product.id)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Product activated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.loadProducts();
        },

        error: error => {

          this.showBackendError(
            error,
            'Unable to activate product'
          );
        }
      });
  }

  deactivateProduct(
    product: Product
  ): void {

    this.productService
      .deactivate(product.id)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Product deactivated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.loadProducts();
        },

        error: error => {

          this.showBackendError(
            error,
            'Unable to deactivate product'
          );
        }
      });
  }

  canManageProducts(): boolean {

    return this.authService.hasAnyRole([
      'ROLE_ADMIN',
      'ROLE_MANAGER'
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
