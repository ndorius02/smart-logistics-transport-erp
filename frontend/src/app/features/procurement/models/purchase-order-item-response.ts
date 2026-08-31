export interface PurchaseOrderItemResponse {
  id: string;

  productId: string;
  productSku: string;
  productName: string;

  orderedQuantity: number;
  receivedQuantity: number;
  remainingQuantity: number;

  unitPrice: number;
  lineTotal: number;
}
