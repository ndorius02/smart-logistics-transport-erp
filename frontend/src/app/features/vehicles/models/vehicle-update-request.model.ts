import { VehicleType } from './vehicle-type.model';
import { VehicleStatus } from './vehicle-status.model';

export interface VehicleUpdateRequest {
  registrationNumber: string;
  brand: string;
  model: string;
  vehicleType: VehicleType;
  loadCapacity: number;
  operationalStatus: VehicleStatus;
}
