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

import { Customer } from '../../models/customer.model';
import { CustomerService } from '../../services/customer.service';

import { AuthService } from '../../../../../core/auth/auth';

@Component({
  selector: 'app-customer-list',
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
  templateUrl: './customer-list.html',
  styleUrl: './customer-list.scss'
})
export class CustomerList implements OnInit {

  private readonly customerService =
    inject(CustomerService);

  private readonly router =
    inject(Router);

  private readonly authService =
    inject(AuthService);

  private readonly snackBar =
    inject(MatSnackBar);

  readonly customers =
    signal<Customer[]>([]);

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
    this.loadCustomers();
  }

  loadCustomers(): void {

    this.loading.set(true);

    const term =
      this.searchTerm().trim();

    let request$;

    if (term) {

      if (
        this.searchType() === 'code'
      ) {

        request$ =
          this.customerService.searchByCode(
            term,
            this.pageIndex(),
            this.pageSize()
          );

      } else {

        request$ =
          this.customerService.searchByCompanyName(
            term,
            this.pageIndex(),
            this.pageSize()
          );
      }

    } else {

      request$ =
        this.customerService.getAll(
          this.pageIndex(),
          this.pageSize()
        );
    }

    request$.subscribe({

      next: response => {

        this.customers.set(
          response.content
        );

        this.totalElements.set(
          response.totalElements
        );

        this.loading.set(false);
      },

      error: error => {

        console.error(
          'Unable to load customers',
          error
        );

        this.loading.set(false);

        this.snackBar.open(
          'Unable to load customers',
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

    this.loadCustomers();
  }

  clearFilters(): void {

    this.searchTerm.set('');

    this.searchType.set(
      'companyName'
    );

    this.pageIndex.set(0);

    this.loadCustomers();
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

    this.loadCustomers();
  }

  createCustomer(): void {

    this.router.navigate([
      '/business-partners/customers/new'
    ]);
  }

  editCustomer(
    customer: Customer
  ): void {

    this.router.navigate([
      '/business-partners/customers',
      customer.id,
      'edit'
    ]);
  }

  activateCustomer(
    customer: Customer
  ): void {

    this.customerService
      .activate(customer.id)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Customer activated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.loadCustomers();
        },

        error: error => {

          this.showBackendError(
            error,
            'Unable to activate customer'
          );
        }
      });
  }

  deactivateCustomer(
    customer: Customer
  ): void {

    this.customerService
      .deactivate(customer.id)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Customer deactivated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.loadCustomers();
        },

        error: error => {

          this.showBackendError(
            error,
            'Unable to deactivate customer'
          );
        }
      });
  }

  canManageCustomers(): boolean {

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
