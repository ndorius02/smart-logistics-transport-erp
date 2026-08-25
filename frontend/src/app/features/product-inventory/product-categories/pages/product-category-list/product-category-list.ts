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

import { ProductCategory } from '../../models/product-category.model';
import { ProductCategoryService } from '../../services/product-category.service';
import { AuthService } from '../../../../../core/auth/auth';

@Component({
  selector: 'app-product-category-list',
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
  templateUrl: './product-category-list.html',
  styleUrl: './product-category-list.scss'
})
export class ProductCategoryList implements OnInit {

  private readonly productCategoryService =
    inject(ProductCategoryService);

  private readonly router =
    inject(Router);

  private readonly authService =
    inject(AuthService);

  private readonly snackBar =
    inject(MatSnackBar);

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
    signal<'name' | 'code'>('name');

  readonly displayedColumns: string[] = [
    'code',
    'name',
    'description',
    'status',
    'actions'
  ];

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {

    this.loading.set(true);

    const term =
      this.searchTerm().trim();

    let request$;

    if (term) {

      if (this.searchType() === 'code') {

        request$ =
          this.productCategoryService.searchByCode(
            term,
            this.pageIndex(),
            this.pageSize()
          );

      } else {

        request$ =
          this.productCategoryService.searchByName(
            term,
            this.pageIndex(),
            this.pageSize()
          );
      }

    } else {

      request$ =
        this.productCategoryService.getAll(
          this.pageIndex(),
          this.pageSize()
        );
    }

    request$.subscribe({

      next: response => {

        this.categories.set(
          response.content
        );

        this.totalElements.set(
          response.totalElements
        );

        this.loading.set(false);
      },

      error: error => {

        console.error(
          'Unable to load product categories',
          error
        );

        this.loading.set(false);

        this.snackBar.open(
          'Unable to load product categories',
          'Close',
          {
            duration: 4000
          }
        );
      }
    });
  }

  search(): void {
    this.pageIndex.set(0);
    this.loadCategories();
  }

  clearFilters(): void {

    this.searchTerm.set('');

    this.searchType.set(
      'name'
    );

    this.pageIndex.set(0);

    this.loadCategories();
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

    this.loadCategories();
  }

  createCategory(): void {

    this.router.navigate([
      '/product-inventory/product-categories/new'
    ]);
  }

  editCategory(
    category: ProductCategory
  ): void {

    this.router.navigate([
      '/product-inventory/product-categories',
      category.id,
      'edit'
    ]);
  }

  activateCategory(
    category: ProductCategory
  ): void {

    this.productCategoryService
      .activate(category.id)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Product category activated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.loadCategories();
        },

        error: error => {

          this.showBackendError(
            error,
            'Unable to activate product category'
          );
        }
      });
  }

  deactivateCategory(
    category: ProductCategory
  ): void {

    this.productCategoryService
      .deactivate(category.id)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Product category deactivated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.loadCategories();
        },

        error: error => {

          this.showBackendError(
            error,
            'Unable to deactivate product category'
          );
        }
      });
  }

  canManageCategories(): boolean {

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
