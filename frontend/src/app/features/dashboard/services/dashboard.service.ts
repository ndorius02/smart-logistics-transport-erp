import { inject, Injectable } from '@angular/core';
import { forkJoin, map, Observable } from 'rxjs';

import { WarehouseService } from '../../warehouses/services/warehouse.service';
import { VehicleService } from '../../vehicles/services/vehicle.service';
import { VehicleStatus } from '../../vehicles/models/vehicle-status.model';
import { DriverService } from '../../drivers/services/driver.service';
import { DriverStatus } from '../../drivers/models/driver-status.model';
import { TransportService } from '../../transports/services/transport.service';
import { TransportStatus } from '../../transports/models/transport-status.model';
import { Transport } from '../../transports/models/transport.model';

export interface DashboardStats {
  totalWarehouses: number;
  availableVehicles: number;
  availableDrivers: number;
  plannedTransports: number;
  inProgressTransports: number;
  completedTransports: number;
  recentTransports: Transport[];
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private readonly warehouseService =
    inject(WarehouseService);

  private readonly vehicleService =
    inject(VehicleService);

  private readonly driverService =
    inject(DriverService);

  private readonly transportService =
    inject(TransportService);

  getStats(): Observable<DashboardStats> {

    return forkJoin({
      warehouses:
        this.warehouseService.getAll(0, 1),

      availableVehicles:
        this.vehicleService.getByStatus(
          VehicleStatus.AVAILABLE,
          0,
          1
        ),

      availableDrivers:
        this.driverService.getByStatus(
          DriverStatus.AVAILABLE,
          0,
          1
        ),

      plannedTransports:
        this.transportService.getByStatus(
          TransportStatus.PLANNED,
          0,
          1
        ),

      inProgressTransports:
        this.transportService.getByStatus(
          TransportStatus.IN_PROGRESS,
          0,
          1
        ),

      completedTransports:
        this.transportService.getByStatus(
          TransportStatus.COMPLETED,
          0,
          1
        ),

      recentTransports:
        this.transportService.getAll(
          0,
          5,
          'createdAt,desc'
        )
    }).pipe(
      map(response => ({
        totalWarehouses:
        response.warehouses.totalElements,

        availableVehicles:
        response.availableVehicles.totalElements,

        availableDrivers:
        response.availableDrivers.totalElements,

        plannedTransports:
        response.plannedTransports.totalElements,

        inProgressTransports:
        response.inProgressTransports.totalElements,

        completedTransports:
        response.completedTransports.totalElements,

        recentTransports:
        response.recentTransports.content
      }))
    );
  }
}
