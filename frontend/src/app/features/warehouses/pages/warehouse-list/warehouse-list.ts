import {
  Component,
  inject,
  OnInit,
  signal
} from '@angular/core';

import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { MatTableModule } from '@angular/material/table';
import {
  MatPaginatorModule,
  PageEvent
} from '@angular/material/paginator';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { Warehouse } from '../../models/warehouse.model';
import { WarehouseService } from '../../services/warehouse.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../../../../core/auth/auth';

@Component({
  selector: 'app-warehouse-list',
  standalone: true,
  imports: [
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule
  ],
  templateUrl: './warehouse-list.html',
  styleUrl: './warehouse-list.scss'
})
export class WarehouseList implements OnInit {

  private readonly warehouseService = inject(WarehouseService);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);
  private readonly authService = inject(AuthService);

  readonly warehouses = signal<Warehouse[]>([]);

  readonly loading = signal(false);

  readonly totalElements = signal(0);

  readonly pageSize = signal(10);

  readonly pageIndex = signal(0);

  readonly searchTerm = signal('');

  canCreateWarehouse(): boolean {
    return this.authService.hasAnyRole([
      'ROLE_ADMIN',
      'ROLE_MANAGER'
    ]);
  }

  canEditWarehouse(): boolean {
    return this.authService.hasAnyRole([
      'ROLE_ADMIN',
      'ROLE_MANAGER'
    ]);
  }

  canChangeWarehouseStatus(): boolean {
    return this.authService.hasAnyRole([
      'ROLE_ADMIN',
      'ROLE_MANAGER'
    ]);
  }

  readonly displayedColumns: string[] = [
    'code',
    'name',
    'city',
    'country',
    'capacity',
    'status',
    'actions'
  ];

  ngOnInit(): void {
    this.loadWarehouses();
  }

  loadWarehouses(): void {

    this.loading.set(true);

    const name =
      this.searchTerm().trim();

    const request$ = name
      ? this.warehouseService.searchByName(
        name,
        this.pageIndex(),
        this.pageSize()
      )
      : this.warehouseService.getAll(
        this.pageIndex(),
        this.pageSize()
      );

    request$.subscribe({
      next: response => {

        this.warehouses.set(
          response.content
        );

        this.totalElements.set(
          response.totalElements
        );

        this.loading.set(false);
      },

      error: error => {

        console.error(
          'Unable to load warehouses',
          error
        );

        this.loading.set(false);
      }
    });
  }

  search(): void {

    this.pageIndex.set(0);

    this.loadWarehouses();
  }

  clearSearch(): void {

    this.searchTerm.set('');

    this.pageIndex.set(0);

    this.loadWarehouses();
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

    this.loadWarehouses();
  }

  createWarehouse(): void {

    this.router.navigate([
      '/warehouses/new'
    ]);
  }

  editWarehouse(
    warehouse: Warehouse
  ): void {

    this.router.navigate([
      '/warehouses',
      warehouse.id,
      'edit'
    ]);
  }

  activateWarehouse(
    warehouse: Warehouse
  ): void {

    this.warehouseService
      .activate(warehouse.id)
      .subscribe({
        next: () => {
          this.snackBar.open(
            'Warehouse activated successfully',
            'Close',
            {
              duration: 3000
            }
          );
          this.loadWarehouses();
        },

        error: error => {
          console.error(
            'Unable to activate warehouse',
            error
          );
        }
      });
  }

  deactivateWarehouse(
    warehouse: Warehouse
  ): void {

    this.warehouseService
      .deactivate(warehouse.id)
      .subscribe({
        next: () => {
          this.snackBar.open(
            'Warehouse deactivated successfully',
            'Close',
            {
              duration: 3000
            }
          );
          this.loadWarehouses();
        },

        error: error => {
          this.snackBar.open(
            'Unable to deactivate warehouse',
            'Close',
            {
              duration: 4000
            }
          );
        }
      });
  }
}
