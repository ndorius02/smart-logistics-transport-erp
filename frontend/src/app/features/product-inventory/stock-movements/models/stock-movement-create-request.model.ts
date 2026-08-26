import { StockMovementType } from './stock-movement.model';

export interface StockMovementCreateRequest {
  reference: string;

  productId: string;
  warehouseId: string;

  movementType: StockMovementType;

  quantity: number;

  reason: string | null;
  notes: string | null;
}
