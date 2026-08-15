import { DriverStatus } from './driver-status.model';

export interface DriverUpdateRequest {
  firstName: string;
  lastName: string;
  licenseNumber: string;
  phoneNumber: string;
  status: DriverStatus;
}
