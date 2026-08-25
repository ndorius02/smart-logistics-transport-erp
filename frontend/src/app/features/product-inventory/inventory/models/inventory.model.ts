export interface Inventory {
  id: string;

  productId: string;
  productSku: string;
  productName: string;

  warehouseId: string;
  warehouseCode: string;
  warehouseName: string;

  quantity: number;
  reservedQuantity: number;
  availableQuantity: number;

  minimumStockLevel: number;

  lowStock: boolean;

  createdAt: string;
  updatedAt: string | null;
}
