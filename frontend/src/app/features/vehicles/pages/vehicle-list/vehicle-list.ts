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
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { Vehicle } from '../../models/vehicle.model';
import { VehicleStatus } from '../../models/vehicle-status.model';
import { VehicleService } from '../../services/vehicle.service';

import { AuthService } from '../../../../core/auth/auth';

@Component({
  selector: 'app-vehicle-list',
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
  templateUrl: './vehicle-list.html',
  styleUrl: './vehicle-list.scss'
})
export class VehicleList implements OnInit {

  private readonly vehicleService =
    inject(VehicleService);

  private readonly router =
    inject(Router);

  private readonly authService =
    inject(AuthService);

  private readonly snackBar =
    inject(MatSnackBar);

  readonly vehicles =
    signal<Vehicle[]>([]);

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
    signal<'registration' | 'brand'>('registration');

  readonly selectedStatus =
    signal<VehicleStatus | ''>('');

  readonly vehicleStatuses =
    Object.values(VehicleStatus);

  readonly displayedColumns: string[] = [
    'registrationNumber',
    'brand',
    'model',
    'vehicleType',
    'loadCapacity',
    'operationalStatus',
    'active',
    'actions'
  ];

  ngOnInit(): void {
    this.loadVehicles();
  }

  loadVehicles(): void {

    this.loading.set(true);

    const term =
      this.searchTerm().trim();

    const status =
      this.selectedStatus();

    let request$;

    if (status) {

      request$ =
        this.vehicleService.getByStatus(
          status,
          this.pageIndex(),
          this.pageSize()
        );

    } else if (term) {

      request$ =
        this.searchType() === 'registration'
          ? this.vehicleService.searchByRegistrationNumber(
            term,
            this.pageIndex(),
            this.pageSize()
          )
          : this.vehicleService.searchByBrand(
            term,
            this.pageIndex(),
            this.pageSize()
          );

    } else {

      request$ =
        this.vehicleService.getAll(
          this.pageIndex(),
          this.pageSize()
        );
    }

    request$.subscribe({
      next: response => {

        this.vehicles.set(
          response.content
        );

        this.totalElements.set(
          response.totalElements
        );

        this.loading.set(false);
      },

      error: error => {

        console.error(
          'Unable to load vehicles',
          error
        );

        this.loading.set(false);
      }
    });
  }

  search(): void {
    this.pageIndex.set(0);
    this.loadVehicles();
  }

  clearFilters(): void {
    this.searchTerm.set('');
    this.selectedStatus.set('');
    this.pageIndex.set(0);
    this.loadVehicles();
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

    this.loadVehicles();
  }

  createVehicle(): void {
    this.router.navigate([
      '/vehicles/new'
    ]);
  }

  editVehicle(
    vehicle: Vehicle
  ): void {

    this.router.navigate([
      '/vehicles',
      vehicle.id,
      'edit'
    ]);
  }

  activateVehicle(
    vehicle: Vehicle
  ): void {

    this.vehicleService
      .activate(vehicle.id)
      .subscribe({
        next: () => {

          this.snackBar.open(
            'Vehicle activated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.loadVehicles();
        },

        error: () => {

          this.snackBar.open(
            'Unable to activate vehicle',
            'Close',
            {
              duration: 4000
            }
          );
        }
      });
  }

  deactivateVehicle(
    vehicle: Vehicle
  ): void {

    this.vehicleService
      .deactivate(vehicle.id)
      .subscribe({
        next: () => {

          this.snackBar.open(
            'Vehicle deactivated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.loadVehicles();
        },

        error: () => {

          this.snackBar.open(
            'Unable to deactivate vehicle',
            'Close',
            {
              duration: 4000
            }
          );
        }
      });
  }

  canCreateVehicle(): boolean {
    return this.authService.hasAnyRole([
      'ROLE_ADMIN',
      'ROLE_TRANSPORT_COORDINATOR'
    ]);
  }

  canEditVehicle(): boolean {
    return this.authService.hasAnyRole([
      'ROLE_ADMIN',
      'ROLE_TRANSPORT_COORDINATOR'
    ]);
  }

  canChangeVehicleStatus(): boolean {
    return this.authService.hasAnyRole([
      'ROLE_ADMIN',
      'ROLE_TRANSPORT_COORDINATOR'
    ]);
  }
}
