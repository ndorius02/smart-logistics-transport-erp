import { TransportStatus } from './transport-status.model';

export interface Transport {
  id: string;

  code: string;

  originWarehouseId: string;
  originWarehouseCode: string;
  originWarehouseName: string;

  destinationWarehouseId: string;
  destinationWarehouseCode: string;
  destinationWarehouseName: string;

  vehicleId: string;
  vehicleRegistrationNumber: string;

  driverId: string;
  driverFirstName: string;
  driverLastName: string;

  plannedDepartureAt: string;
  plannedArrivalAt: string;

  actualDepartureAt: string | null;
  actualArrivalAt: string | null;

  status: TransportStatus;

  createdAt: string;
  updatedAt: string | null;
}
