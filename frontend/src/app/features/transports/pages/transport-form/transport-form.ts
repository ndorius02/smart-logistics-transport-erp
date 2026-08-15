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

import { forkJoin } from 'rxjs';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import {
  MatSnackBar,
  MatSnackBarModule
} from '@angular/material/snack-bar';

import { TransportService } from '../../services/transport.service';
import { TransportCreateRequest } from '../../models/transport-create-request.model';
import { TransportUpdateRequest } from '../../models/transport-update-request.model';

import { WarehouseService } from '../../../warehouses/services/warehouse.service';
import { Warehouse } from '../../../warehouses/models/warehouse.model';

import { VehicleService } from '../../../vehicles/services/vehicle.service';
import { Vehicle } from '../../../vehicles/models/vehicle.model';
import { VehicleStatus } from '../../../vehicles/models/vehicle-status.model';

import { DriverService } from '../../../drivers/services/driver.service';
import { Driver } from '../../../drivers/models/driver.model';
import { DriverStatus } from '../../../drivers/models/driver-status.model';

@Component({
  selector: 'app-transport-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './transport-form.html',
  styleUrl: './transport-form.scss'
})
export class TransportForm implements OnInit {

  private readonly fb =
    inject(FormBuilder);

  private readonly transportService =
    inject(TransportService);

  private readonly warehouseService =
    inject(WarehouseService);

  private readonly vehicleService =
    inject(VehicleService);

  private readonly driverService =
    inject(DriverService);

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly snackBar =
    inject(MatSnackBar);

  readonly loading =
    signal(false);

  readonly loadingResources =
    signal(false);

  readonly errorMessage =
    signal<string | null>(null);

  readonly transportId =
    signal<string | null>(null);

  readonly warehouses =
    signal<Warehouse[]>([]);

  readonly vehicles =
    signal<Vehicle[]>([]);

  readonly drivers =
    signal<Driver[]>([]);

  readonly form =
    this.fb.nonNullable.group({

      code: [
        '',
        [
          Validators.required,
          Validators.maxLength(50)
        ]
      ],

      originWarehouseId: [
        '',
        [
          Validators.required
        ]
      ],

      destinationWarehouseId: [
        '',
        [
          Validators.required
        ]
      ],

      vehicleId: [
        '',
        [
          Validators.required
        ]
      ],

      driverId: [
        '',
        [
          Validators.required
        ]
      ],

      plannedDepartureAt: [
        '',
        [
          Validators.required
        ]
      ],

      plannedArrivalAt: [
        '',
        [
          Validators.required
        ]
      ]
    });

  get isEditMode(): boolean {
    return this.transportId() !== null;
  }

  ngOnInit(): void {

    this.loadResources();

    const id =
      this.route.snapshot.paramMap.get('id');

    if (id) {
      this.transportId.set(id);
      this.loadTransport(id);
    }
  }

  submit(): void {

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    if (!this.validateBusinessRules()) {
      return;
    }

    this.loading.set(true);
    this.errorMessage.set(null);

    if (this.isEditMode) {
      this.updateTransport();
    } else {
      this.createTransport();
    }
  }

  cancel(): void {
    this.router.navigate([
      '/transports'
    ]);
  }

  private loadResources(): void {

    this.loadingResources.set(true);

    forkJoin({
      warehouses:
        this.warehouseService.getAll(
          0,
          100,
          'name,asc'
        ),

      vehicles:
        this.vehicleService.getAll(
          0,
          100,
          'registrationNumber,asc'
        ),

      drivers:
        this.driverService.getAll(
          0,
          100,
          'lastName,asc'
        )
    })
      .subscribe({

        next: response => {

          this.warehouses.set(
            response.warehouses.content
              .filter(
                warehouse =>
                  warehouse.active
              )
          );

          this.vehicles.set(
            response.vehicles.content
              .filter(
                vehicle =>
                  vehicle.active &&
                  vehicle.operationalStatus ===
                  VehicleStatus.AVAILABLE
              )
          );

          this.drivers.set(
            response.drivers.content
              .filter(
                driver =>
                  driver.active &&
                  driver.status ===
                  DriverStatus.AVAILABLE
              )
          );

          this.loadingResources.set(false);
        },

        error: error => {

          console.error(
            'Unable to load transport resources',
            error
          );

          this.errorMessage.set(
            'Unable to load warehouses, vehicles or drivers'
          );

          this.loadingResources.set(false);
        }
      });
  }

  private loadTransport(
    id: string
  ): void {

    this.loading.set(true);

    this.transportService
      .getById(id)
      .subscribe({

        next: transport => {

          this.form.patchValue({

            code:
            transport.code,

            originWarehouseId:
            transport.originWarehouseId,

            destinationWarehouseId:
            transport.destinationWarehouseId,

            vehicleId:
            transport.vehicleId,

            driverId:
            transport.driverId,

            plannedDepartureAt:
              this.toDateTimeLocal(
                transport.plannedDepartureAt
              ),

            plannedArrivalAt:
              this.toDateTimeLocal(
                transport.plannedArrivalAt
              )
          });

          this.loading.set(false);
        },

        error: () => {

          this.errorMessage.set(
            'Unable to load transport'
          );

          this.loading.set(false);
        }
      });
  }

  private createTransport(): void {

    const value =
      this.form.getRawValue();

    const request:
      TransportCreateRequest = {

      code:
      value.code,

      originWarehouseId:
      value.originWarehouseId,

      destinationWarehouseId:
      value.destinationWarehouseId,

      vehicleId:
      value.vehicleId,

      driverId:
      value.driverId,

      plannedDepartureAt:
        this.toBackendDateTime(
          value.plannedDepartureAt
        ),

      plannedArrivalAt:
        this.toBackendDateTime(
          value.plannedArrivalAt
        )
    };

    this.transportService
      .create(request)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Transport created successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.router.navigate([
            '/transports'
          ]);
        },

        error: error => {

          this.handleError(
            error,
            'Unable to create transport'
          );
        }
      });
  }

  private updateTransport(): void {

    const id =
      this.transportId();

    if (!id) {
      return;
    }

    const value =
      this.form.getRawValue();

    const request:
      TransportUpdateRequest = {

      code:
      value.code,

      originWarehouseId:
      value.originWarehouseId,

      destinationWarehouseId:
      value.destinationWarehouseId,

      vehicleId:
      value.vehicleId,

      driverId:
      value.driverId,

      plannedDepartureAt:
        this.toBackendDateTime(
          value.plannedDepartureAt
        ),

      plannedArrivalAt:
        this.toBackendDateTime(
          value.plannedArrivalAt
        )
    };

    this.transportService
      .update(
        id,
        request
      )
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Transport updated successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.router.navigate([
            '/transports'
          ]);
        },

        error: error => {

          this.handleError(
            error,
            'Unable to update transport'
          );
        }
      });
  }

  private validateBusinessRules(): boolean {

    const value =
      this.form.getRawValue();

    if (
      value.originWarehouseId ===
      value.destinationWarehouseId
    ) {

      this.errorMessage.set(
        'Origin and destination warehouses must be different'
      );

      return false;
    }

    const departure =
      new Date(
        value.plannedDepartureAt
      );

    const arrival =
      new Date(
        value.plannedArrivalAt
      );

    if (
      departure >= arrival
    ) {

      this.errorMessage.set(
        'Planned departure must be before planned arrival'
      );

      return false;
    }

    return true;
  }

  private toBackendDateTime(
    value: string
  ): string {

    return value.length === 16
      ? `${value}:00`
      : value;
  }

  private toDateTimeLocal(
    value: string
  ): string {

    return value.substring(
      0,
      16
    );
  }

  private handleError(
    error: any,
    fallbackMessage: string
  ): void {

    this.loading.set(false);

    const backendMessage =
      error?.error?.message;

    this.errorMessage.set(
      backendMessage ??
      fallbackMessage
    );
  }
}
