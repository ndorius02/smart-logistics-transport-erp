export interface PurchaseOrderCreateRequest {
  orderNumber: string;
  supplierId: string;
  warehouseId: string;
  expectedDeliveryDate: string | null;
  notes: string | null;
}
