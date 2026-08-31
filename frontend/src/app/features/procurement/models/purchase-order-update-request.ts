export interface PurchaseOrderUpdateRequest {
  supplierId: string;
  warehouseId: string;
  expectedDeliveryDate: string | null;
  notes: string | null;
}
