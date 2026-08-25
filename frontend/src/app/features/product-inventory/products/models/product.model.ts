export type UnitOfMeasure =
  | 'PIECE'
  | 'BOX'
  | 'PALLET'
  | 'KILOGRAM'
  | 'LITER'
  | 'METER';

export interface Product {
  id: string;

  sku: string;
  name: string;
  description: string | null;

  categoryId: string;
  categoryCode: string;
  categoryName: string;

  unitOfMeasure: UnitOfMeasure;

  weight: number | null;

  active: boolean;

  createdAt: string;
  updatedAt: string | null;
}
