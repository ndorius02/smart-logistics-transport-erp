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

import { Supplier } from '../../models/supplier.model';
import { SupplierService } from '../../services/supplier.service';

import { AuthService } from '../../../../../core/auth/auth';

@Component({
  selector: 'app-supplier-list',
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
  templateUrl: './supplier-list.html',
  styleUrl: './supplier-list.scss'
})
export class SupplierList implements OnInit {

  private readonly supplierService =
    inject(SupplierService);

  private readonly router =
    inject(Router);

  private readonly authService =
    inject(AuthService);

  private readonly snackBar =
    inject(MatSnackBar);

  readonly suppliers =
    signal<Supplier[]>([]);

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
    signal<'companyName' | 'code'>('companyName');

  readonly displayedColumns: string[] = [
    'code',
    'companyName',
    'contact',
    'location',
    'vatNumber',
    'status',
    'actions'
  ];

  ngOnInit(): void {
    this.loadSuppliers();
  }

  loadSuppliers(): void {

    this.loading.set(true);

    const term =
      this.searchTerm().trim();

    let request$;

    if (term) {

      if (this.searchType() === 'code') {

        request$ =
          this.supplierService.searchByCode(
            term,
            this.pageIndex(),
            this.pageSize()
          );

      } else {

        request$ =
          this.supplierService.searchByCompanyName(
            term,
            this.pageIndex(),
            this.pageSize()
          );
      }

    } else {

      request$ =
        this.supplierService.getAll(
          this.pageIndex(),
          this.pageSize()
        );
    }

    request$.subscribe({

      next: response => {

        this.suppliers.set(
          response.content
        );

        this.totalElements.set(
          response.totalElements
        );

        this.loading.set(false);
      },

      error: error => {

        console.error(
          'Unable to load suppliers',
          error
        );

        this.loading.set(false);

        this.snackBar.open(
          'Unable to load suppliers',
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
    this.loadSuppliers();
  }

  clearFilters(): void {

    this.searchTerm.set('');

    this.searchType.set(
      'companyName'
    );

    this.pageIndex.set(0);

    this.loadSuppliers();
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

    this.loadSuppliers();
  }

  createSupplier(): void {

    this.router.navigate([
      '/business-partners/suppliers/new'
    ]);
  }

  editSupplier(
    supplier: Supplier
  ): void {

    this.router.navigate([
      '/business-partners/suppliers',
      supplier.id,
      'edit'
    ]);
  }

  activateSupplier(
    supplier: Supplier
  ): void {

    this.supplierService
      .activate(supplier.id)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Supplier activated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.loadSuppliers();
        },

        error: error => {

          this.showBackendError(
            error,
            'Unable to activate supplier'
          );
        }
      });
  }

  deactivateSupplier(
    supplier: Supplier
  ): void {

    this.supplierService
      .deactivate(supplier.id)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Supplier deactivated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.loadSuppliers();
        },

        error: error => {

          this.showBackendError(
            error,
            'Unable to deactivate supplier'
          );
        }
      });
  }

  canManageSuppliers(): boolean {

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
