import {
  Component,
  inject,
  OnInit,
  signal
} from '@angular/core';

import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  ActivatedRoute,
  Router
} from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

import { WarehouseService } from '../../services/warehouse.service';
import { WarehouseCreateRequest } from '../../models/warehouse-create-request.model';
import { WarehouseUpdateRequest } from '../../models/warehouse-update-request.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-warehouse-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSlideToggleModule,
    MatSnackBarModule
  ],
  templateUrl: './warehouse-form.html',
  styleUrl: './warehouse-form.scss'
})
export class WarehouseForm implements OnInit {

  private readonly fb = inject(FormBuilder);
  private readonly warehouseService = inject(WarehouseService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  readonly loading = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly warehouseId = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    code: [
      '',
      [
        Validators.required,
        Validators.maxLength(50)
      ]
    ],

    name: [
      '',
      [
        Validators.required,
        Validators.maxLength(150)
      ]
    ],

    address: [
      '',
      [
        Validators.required,
        Validators.maxLength(255)
      ]
    ],

    city: [
      '',
      [
        Validators.required,
        Validators.maxLength(100)
      ]
    ],

    country: [
      '',
      [
        Validators.required,
        Validators.maxLength(100)
      ]
    ],

    capacity: [
      1,
      [
        Validators.required,
        Validators.min(1)
      ]
    ],

    active: [true]
  });

  get isEditMode(): boolean {
    return this.warehouseId() !== null;
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.warehouseId.set(id);
      this.loadWarehouse(id);
    }
  }

  submit(): void {

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set(null);

    if (this.isEditMode) {
      this.updateWarehouse();
    } else {
      this.createWarehouse();
    }
  }

  cancel(): void {
    this.router.navigate(['/warehouses']);
  }

  private loadWarehouse(id: string): void {

    this.loading.set(true);

    this.warehouseService
      .getById(id)
      .subscribe({
        next: warehouse => {

          this.form.patchValue({
            code: warehouse.code,
            name: warehouse.name,
            address: warehouse.address,
            city: warehouse.city,
            country: warehouse.country,
            capacity: warehouse.capacity,
            active: warehouse.active
          });

          this.loading.set(false);
        },

        error: () => {
          this.errorMessage.set(
            'Unable to load warehouse'
          );

          this.loading.set(false);
        }
      });
  }

  private createWarehouse(): void {

    const value = this.form.getRawValue();

    const request: WarehouseCreateRequest = {
      code: value.code,
      name: value.name,
      address: value.address,
      city: value.city,
      country: value.country,
      capacity: value.capacity
    };

    this.warehouseService
      .create(request)
      .subscribe({
        next: () => {
          this.snackBar.open(
            'Warehouse created successfully',
            'Close',
            {
              duration: 3000
            }
          );
          this.router.navigate(['/warehouses']);
        },

        error: error => {
          this.handleError(error);
        }
      });
  }

  private updateWarehouse(): void {

    const id = this.warehouseId();

    if (!id) {
      return;
    }

    const value = this.form.getRawValue();

    const request: WarehouseUpdateRequest = {
      code: value.code,
      name: value.name,
      address: value.address,
      city: value.city,
      country: value.country,
      capacity: value.capacity,
      active: value.active
    };

    this.warehouseService
      .update(id, request)
      .subscribe({
        next: () => {
          this.snackBar.open(
            'Warehouse updated successfully',
            'Close',
            {
              duration: 3000
            }
          );
          this.router.navigate(['/warehouses']);
        },

        error: error => {
          this.handleError(error);
        }
      });
  }

  private handleError(error: any): void {

    this.loading.set(false);

    const backendMessage =
      error?.error?.message;

    this.errorMessage.set(
      backendMessage ??
      'Unable to save warehouse'
    );
  }
}
