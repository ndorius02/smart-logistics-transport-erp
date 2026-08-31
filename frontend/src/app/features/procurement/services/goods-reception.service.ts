import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_ENDPOINTS } from '../../../core/constants/api-endpoints';
import { PageResponse } from '../../../core/models/page-response.model';

import { GoodsReceptionCreateRequest } from '../models/goods-reception-create-request';
import { GoodsReceptionResponse } from '../models/goods-reception-response';

@Injectable({
  providedIn: 'root'
})
export class GoodsReceptionService {

  private readonly http = inject(HttpClient);

  getAll(
    page = 0,
    size = 10
  ): Observable<PageResponse<GoodsReceptionResponse>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<PageResponse<GoodsReceptionResponse>>(
      API_ENDPOINTS.goodsReceptions,
      { params }
    );
  }

  getById(
    id: string
  ): Observable<GoodsReceptionResponse> {

    return this.http.get<GoodsReceptionResponse>(
      `${API_ENDPOINTS.goodsReceptions}/${id}`
    );
  }

  receive(
    request: GoodsReceptionCreateRequest
  ): Observable<GoodsReceptionResponse> {

    return this.http.post<GoodsReceptionResponse>(
      API_ENDPOINTS.goodsReceptions,
      request
    );
  }

  getByPurchaseOrder(
    purchaseOrderId: string,
    page = 0,
    size = 10
  ): Observable<PageResponse<GoodsReceptionResponse>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<PageResponse<GoodsReceptionResponse>>(
      `${API_ENDPOINTS.goodsReceptions}/purchase-order/${purchaseOrderId}`,
      { params }
    );
  }
}
