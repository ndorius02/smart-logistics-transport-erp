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

import { Driver } from '../../models/driver.model';
import { DriverStatus } from '../../models/driver-status.model';
import { DriverService } from '../../services/driver.service';

import { AuthService } from '../../../../core/auth/auth';

@Component({
  selector: 'app-driver-list',
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
  templateUrl: './driver-list.html',
  styleUrl: './driver-list.scss'
})
export class DriverList implements OnInit {

  private readonly driverService =
    inject(DriverService);

  private readonly router =
    inject(Router);

  private readonly authService =
    inject(AuthService);

  private readonly snackBar =
    inject(MatSnackBar);

  readonly drivers =
    signal<Driver[]>([]);

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
    signal<'license' | 'lastName'>('license');

  readonly selectedStatus =
    signal<DriverStatus | ''>('');

  readonly driverStatuses =
    Object.values(DriverStatus);

  readonly displayedColumns: string[] = [
    'firstName',
    'lastName',
    'licenseNumber',
    'phoneNumber',
    'status',
    'active',
    'actions'
  ];

  ngOnInit(): void {
    this.loadDrivers();
  }

  loadDrivers(): void {

    this.loading.set(true);

    const term =
      this.searchTerm().trim();

    const status =
      this.selectedStatus();

    let request$;

    if (status) {

      request$ =
        this.driverService.getByStatus(
          status,
          this.pageIndex(),
          this.pageSize()
        );

    } else if (term) {

      request$ =
        this.searchType() === 'license'
          ? this.driverService.searchByLicenseNumber(
            term,
            this.pageIndex(),
            this.pageSize()
          )
          : this.driverService.searchByLastName(
            term,
            this.pageIndex(),
            this.pageSize()
          );

    } else {

      request$ =
        this.driverService.getAll(
          this.pageIndex(),
          this.pageSize()
        );
    }

    request$.subscribe({
      next: response => {

        this.drivers.set(
          response.content
        );

        this.totalElements.set(
          response.totalElements
        );

        this.loading.set(false);
      },

      error: error => {

        console.error(
          'Unable to load drivers',
          error
        );

        this.loading.set(false);
      }
    });
  }

  search(): void {
    this.pageIndex.set(0);
    this.loadDrivers();
  }

  clearFilters(): void {
    this.searchTerm.set('');
    this.selectedStatus.set('');
    this.pageIndex.set(0);
    this.loadDrivers();
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

    this.loadDrivers();
  }

  createDriver(): void {
    this.router.navigate([
      '/drivers/new'
    ]);
  }

  editDriver(
    driver: Driver
  ): void {

    this.router.navigate([
      '/drivers',
      driver.id,
      'edit'
    ]);
  }

  activateDriver(
    driver: Driver
  ): void {

    this.driverService
      .activate(driver.id)
      .subscribe({
        next: () => {

          this.snackBar.open(
            'Driver activated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.loadDrivers();
        },

        error: () => {

          this.snackBar.open(
            'Unable to activate driver',
            'Close',
            {
              duration: 4000
            }
          );
        }
      });
  }

  deactivateDriver(
    driver: Driver
  ): void {

    this.driverService
      .deactivate(driver.id)
      .subscribe({
        next: () => {

          this.snackBar.open(
            'Driver deactivated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.loadDrivers();
        },

        error: () => {

          this.snackBar.open(
            'Unable to deactivate driver',
            'Close',
            {
              duration: 4000
            }
          );
        }
      });
  }

  canCreateDriver(): boolean {
    return this.authService.hasAnyRole([
      'ROLE_ADMIN',
      'ROLE_TRANSPORT_COORDINATOR'
    ]);
  }

  canEditDriver(): boolean {
    return this.authService.hasAnyRole([
      'ROLE_ADMIN',
      'ROLE_TRANSPORT_COORDINATOR'
    ]);
  }

  canChangeDriverStatus(): boolean {
    return this.authService.hasAnyRole([
      'ROLE_ADMIN',
      'ROLE_TRANSPORT_COORDINATOR'
    ]);
  }
}
