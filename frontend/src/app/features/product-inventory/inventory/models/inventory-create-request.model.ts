export interface InventoryCreateRequest {
  productId: string;
  warehouseId: string;
  quantity: number;
  reservedQuantity: number;
  minimumStockLevel: number;
}
