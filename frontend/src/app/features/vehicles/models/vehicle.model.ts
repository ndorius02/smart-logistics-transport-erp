import { VehicleType } from './vehicle-type.model';
import { VehicleStatus } from './vehicle-status.model';

export interface Vehicle {
  id: string;
  registrationNumber: string;
  brand: string;
  model: string;
  vehicleType: VehicleType;
  loadCapacity: number;
  operationalStatus: VehicleStatus;
  active: boolean;
  createdAt: string;
  updatedAt: string | null;
}
