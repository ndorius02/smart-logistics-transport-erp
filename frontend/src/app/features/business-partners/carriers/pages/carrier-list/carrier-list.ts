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

import { Carrier } from '../../models/carrier.model';
import { CarrierService } from '../../services/carrier.service';

import { AuthService } from '../../../../../core/auth/auth';

@Component({
  selector: 'app-carrier-list',
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
  templateUrl: './carrier-list.html',
  styleUrl: './carrier-list.scss'
})
export class CarrierList implements OnInit {

  private readonly carrierService =
    inject(CarrierService);

  private readonly router =
    inject(Router);

  private readonly authService =
    inject(AuthService);

  private readonly snackBar =
    inject(MatSnackBar);

  readonly carriers =
    signal<Carrier[]>([]);

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
    signal<
      'companyName'
      | 'code'
      | 'licenseNumber'
    >('companyName');

  readonly displayedColumns: string[] = [
    'code',
    'companyName',
    'contact',
    'location',
    'licenseNumber',
    'status',
    'actions'
  ];

  ngOnInit(): void {
    this.loadCarriers();
  }

  loadCarriers(): void {

    this.loading.set(true);

    const term =
      this.searchTerm().trim();

    let request$;

    if (term) {

      switch (this.searchType()) {

        case 'code':

          request$ =
            this.carrierService.searchByCode(
              term,
              this.pageIndex(),
              this.pageSize()
            );

          break;

        case 'licenseNumber':

          request$ =
            this.carrierService.searchByLicenseNumber(
              term,
              this.pageIndex(),
              this.pageSize()
            );

          break;

        default:

          request$ =
            this.carrierService.searchByCompanyName(
              term,
              this.pageIndex(),
              this.pageSize()
            );
      }

    } else {

      request$ =
        this.carrierService.getAll(
          this.pageIndex(),
          this.pageSize()
        );
    }

    request$.subscribe({

      next: response => {

        this.carriers.set(
          response.content
        );

        this.totalElements.set(
          response.totalElements
        );

        this.loading.set(false);
      },

      error: error => {

        console.error(
          'Unable to load carriers',
          error
        );

        this.loading.set(false);

        this.snackBar.open(
          'Unable to load carriers',
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

    this.loadCarriers();
  }

  clearFilters(): void {

    this.searchTerm.set('');

    this.searchType.set(
      'companyName'
    );

    this.pageIndex.set(0);

    this.loadCarriers();
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

    this.loadCarriers();
  }

  createCarrier(): void {

    this.router.navigate([
      '/business-partners/carriers/new'
    ]);
  }

  editCarrier(
    carrier: Carrier
  ): void {

    this.router.navigate([
      '/business-partners/carriers',
      carrier.id,
      'edit'
    ]);
  }

  activateCarrier(
    carrier: Carrier
  ): void {

    this.carrierService
      .activate(carrier.id)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Carrier activated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.loadCarriers();
        },

        error: error => {

          this.showBackendError(
            error,
            'Unable to activate carrier'
          );
        }
      });
  }

  deactivateCarrier(
    carrier: Carrier
  ): void {

    this.carrierService
      .deactivate(carrier.id)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Carrier deactivated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.loadCarriers();
        },

        error: error => {

          this.showBackendError(
            error,
            'Unable to deactivate carrier'
          );
        }
      });
  }

  canManageCarriers(): boolean {

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
