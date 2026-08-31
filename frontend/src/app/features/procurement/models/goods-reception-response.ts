export interface GoodsReceptionResponse {
  id: string;
  reference: string;

  purchaseOrderId: string;
  purchaseOrderNumber: string;

  purchaseOrderItemId: string;

  productId: string;
  productSku: string;
  productName: string;

  warehouseId: string;
  warehouseCode: string;
  warehouseName: string;

  quantity: number;

  notes: string | null;

  receptionDate: string;
  createdBy: string;
  createdAt: string;
}
