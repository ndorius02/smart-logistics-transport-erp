export interface GoodsReceptionCreateRequest {
  reference: string;
  purchaseOrderItemId: string;
  quantity: number;
  notes: string | null;
}
