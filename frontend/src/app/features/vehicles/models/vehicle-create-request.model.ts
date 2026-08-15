import { VehicleType } from './vehicle-type.model';

export interface VehicleCreateRequest {
  registrationNumber: string;
  brand: string;
  model: string;
  vehicleType: VehicleType;
  loadCapacity: number;
}
