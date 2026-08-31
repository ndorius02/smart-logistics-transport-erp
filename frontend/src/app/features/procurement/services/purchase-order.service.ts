import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_ENDPOINTS } from '../../../core/constants/api-endpoints';
import { PageResponse } from '../../../core/models/page-response.model';

import { PurchaseOrderResponse } from '../models/purchase-order-response';
import { PurchaseOrderCreateRequest } from '../models/purchase-order-create-request';
import { PurchaseOrderUpdateRequest } from '../models/purchase-order-update-request';
import { PurchaseOrderItemCreateRequest } from '../models/purchase-order-item-create-request';
import { PurchaseOrderItemUpdateRequest } from '../models/purchase-order-item-update-request';
import { PurchaseOrderStatus } from '../models/purchase-order-status';

@Injectable({
  providedIn: 'root'
})
export class PurchaseOrderService {

  private readonly http = inject(HttpClient);

  getAll(
    page = 0,
    size = 10
  ): Observable<PageResponse<PurchaseOrderResponse>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<PageResponse<PurchaseOrderResponse>>(
      API_ENDPOINTS.purchaseOrders,
      { params }
    );
  }

  getById(
    id: string
  ): Observable<PurchaseOrderResponse> {

    return this.http.get<PurchaseOrderResponse>(
      `${API_ENDPOINTS.purchaseOrders}/${id}`
    );
  }

  create(
    request: PurchaseOrderCreateRequest
  ): Observable<PurchaseOrderResponse> {

    return this.http.post<PurchaseOrderResponse>(
      API_ENDPOINTS.purchaseOrders,
      request
    );
  }

  update(
    id: string,
    request: PurchaseOrderUpdateRequest
  ): Observable<PurchaseOrderResponse> {

    return this.http.put<PurchaseOrderResponse>(
      `${API_ENDPOINTS.purchaseOrders}/${id}`,
      request
    );
  }

  addItem(
    purchaseOrderId: string,
    request: PurchaseOrderItemCreateRequest
  ): Observable<PurchaseOrderResponse> {

    return this.http.post<PurchaseOrderResponse>(
      `${API_ENDPOINTS.purchaseOrders}/${purchaseOrderId}/items`,
      request
    );
  }

  updateItem(
    purchaseOrderId: string,
    itemId: string,
    request: PurchaseOrderItemUpdateRequest
  ): Observable<PurchaseOrderResponse> {

    return this.http.put<PurchaseOrderResponse>(
      `${API_ENDPOINTS.purchaseOrders}/${purchaseOrderId}/items/${itemId}`,
      request
    );
  }

  removeItem(
    purchaseOrderId: string,
    itemId: string
  ): Observable<PurchaseOrderResponse> {

    return this.http.delete<PurchaseOrderResponse>(
      `${API_ENDPOINTS.purchaseOrders}/${purchaseOrderId}/items/${itemId}`
    );
  }

  submit(
    id: string
  ): Observable<PurchaseOrderResponse> {

    return this.http.patch<PurchaseOrderResponse>(
      `${API_ENDPOINTS.purchaseOrders}/${id}/submit`,
      {}
    );
  }

  approve(
    id: string
  ): Observable<PurchaseOrderResponse> {

    return this.http.patch<PurchaseOrderResponse>(
      `${API_ENDPOINTS.purchaseOrders}/${id}/approve`,
      {}
    );
  }

  cancel(
    id: string
  ): Observable<PurchaseOrderResponse> {

    return this.http.patch<PurchaseOrderResponse>(
      `${API_ENDPOINTS.purchaseOrders}/${id}/cancel`,
      {}
    );
  }

  getByStatus(
    status: PurchaseOrderStatus,
    page = 0,
    size = 10
  ): Observable<PageResponse<PurchaseOrderResponse>> {

    const params = new HttpParams()
      .set('status', status)
      .set('page', page)
      .set('size', size);

    return this.http.get<PageResponse<PurchaseOrderResponse>>(
      `${API_ENDPOINTS.purchaseOrders}/status`,
      { params }
    );
  }
}
