export interface ProductCategory {
  id: string;
  code: string;
  name: string;
  description: string | null;
  active: boolean;
  createdAt: string;
  updatedAt: string | null;
}
