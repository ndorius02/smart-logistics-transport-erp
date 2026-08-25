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

import {
  MatSnackBar,
  MatSnackBarModule
} from '@angular/material/snack-bar';

import { Inventory } from '../../models/inventory.model';
import { InventoryMinimumStockUpdateRequest } from '../../models/inventory-minimum-stock-update-request.model';
import { InventoryService } from '../../services/inventory.service';

@Component({
  selector: 'app-inventory-minimum-stock-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './inventory-minimum-stock-form.html',
  styleUrl: './inventory-minimum-stock-form.scss'
})
export class InventoryMinimumStockForm implements OnInit {

  private readonly fb =
    inject(FormBuilder);

  private readonly inventoryService =
    inject(InventoryService);

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly snackBar =
    inject(MatSnackBar);

  readonly loading =
    signal(true);

  readonly saving =
    signal(false);

  readonly inventory =
    signal<Inventory | null>(null);

  private inventoryId: string | null = null;

  readonly form = this.fb.nonNullable.group({

    minimumStockLevel: [
      0,
      [
        Validators.required,
        Validators.min(0)
      ]
    ]

  });

  ngOnInit(): void {

    this.inventoryId =
      this.route.snapshot.paramMap.get('id');

    if (!this.inventoryId) {

      this.snackBar.open(
        'Inventory identifier is missing',
        'Close',
        {
          duration: 4000
        }
      );

      this.cancel();

      return;
    }

    this.loadInventory(
      this.inventoryId
    );
  }

  private loadInventory(
    id: string
  ): void {

    this.loading.set(true);

    this.inventoryService
      .getById(id)
      .subscribe({

        next: inventory => {

          this.inventory.set(
            inventory
          );

          this.form.patchValue({
            minimumStockLevel:
            inventory.minimumStockLevel
          });

          this.loading.set(false);
        },

        error: error => {

          this.loading.set(false);

          this.showBackendError(
            error,
            'Unable to load inventory position'
          );
        }
      });
  }

  save(): void {

    if (this.form.invalid) {

      this.form.markAllAsTouched();

      return;
    }

    if (
      !this.inventoryId
      || this.saving()
    ) {
      return;
    }

    const request:
      InventoryMinimumStockUpdateRequest = {

      minimumStockLevel:
      this.form.getRawValue()
        .minimumStockLevel
    };

    this.saving.set(true);

    this.inventoryService
      .updateMinimumStockLevel(
        this.inventoryId,
        request
      )
      .subscribe({

        next: () => {

          this.saving.set(false);

          this.snackBar.open(
            'Minimum stock level updated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.router.navigate([
            '/product-inventory/inventory'
          ]);
        },

        error: error => {

          this.saving.set(false);

          this.showBackendError(
            error,
            'Unable to update minimum stock level'
          );
        }
      });
  }

  cancel(): void {

    this.router.navigate([
      '/product-inventory/inventory'
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
