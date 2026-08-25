import { UnitOfMeasure } from './product.model';

export interface ProductCreateRequest {
  sku: string;
  name: string;
  description: string | null;

  categoryId: string;

  unitOfMeasure: UnitOfMeasure;

  weight: number | null;
}
