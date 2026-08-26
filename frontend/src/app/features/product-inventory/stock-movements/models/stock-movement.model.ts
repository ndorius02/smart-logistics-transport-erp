export type StockMovementType =
  | 'STOCK_IN'
  | 'STOCK_OUT'
  | 'ADJUSTMENT_IN'
  | 'ADJUSTMENT_OUT';

export interface StockMovement {
  id: string;

  reference: string;

  productId: string;
  productSku: string;
  productName: string;

  warehouseId: string;
  warehouseCode: string;
  warehouseName: string;

  movementType: StockMovementType;

  quantity: number;

  reason: string | null;
  notes: string | null;

  movementDate: string;
  createdBy: string;

  createdAt: string;
}
