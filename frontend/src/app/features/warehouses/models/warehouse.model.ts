export interface Warehouse {
  id: string;
  code: string;
  name: string;
  address: string;
  city: string;
  country: string;
  capacity: number;
  active: boolean;
  createdAt: string;
  updatedAt: string | null;
}
