import { DriverStatus } from './driver-status.model';

export interface Driver {
  id: string;
  firstName: string;
  lastName: string;
  licenseNumber: string;
  phoneNumber: string;
  status: DriverStatus;
  active: boolean;
  createdAt: string;
  updatedAt: string | null;
}
