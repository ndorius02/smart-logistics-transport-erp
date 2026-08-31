import { PurchaseOrderStatus } from './purchase-order-status';
import { PurchaseOrderItemResponse } from './purchase-order-item-response';

export interface PurchaseOrderResponse {
  id: string;

  orderNumber: string;

  supplierId: string;
  supplierCode: string;
  supplierName: string;

  warehouseId: string;
  warehouseCode: string;
  warehouseName: string;

  orderDate: string;
  expectedDeliveryDate: string | null;

  status: PurchaseOrderStatus;

  notes: string | null;

  items: PurchaseOrderItemResponse[];

  totalAmount: number;

  createdAt: string;
  updatedAt: string | null;
}
