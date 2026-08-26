import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_ENDPOINTS } from '../../../../core/constants/api-endpoints';
import { PageResponse } from '../../../../core/models/page-response.model';

import {
  StockMovement,
  StockMovementType
} from '../models/stock-movement.model';

import {
  StockMovementCreateRequest
} from '../models/stock-movement-create-request.model';

@Injectable({
  providedIn: 'root'
})
export class StockMovementService {

  private readonly http =
    inject(HttpClient);

  getAll(
    page = 0,
    size = 10,
    sort = 'movementDate,desc'
  ): Observable<PageResponse<StockMovement>> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size)
        .set('sort', sort);

    return this.http.get<PageResponse<StockMovement>>(
      API_ENDPOINTS.stockMovements,
      { params }
    );
  }

  getById(
    id: string
  ): Observable<StockMovement> {

    return this.http.get<StockMovement>(
      `${API_ENDPOINTS.stockMovements}/${id}`
    );
  }

  create(
    request: StockMovementCreateRequest
  ): Observable<StockMovement> {

    return this.http.post<StockMovement>(
      API_ENDPOINTS.stockMovements,
      request
    );
  }

  getByProduct(
    productId: string,
    page = 0,
    size = 10
  ): Observable<PageResponse<StockMovement>> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size);

    return this.http.get<PageResponse<StockMovement>>(
      `${API_ENDPOINTS.stockMovements}/product/${productId}`,
      { params }
    );
  }

  getByWarehouse(
    warehouseId: string,
    page = 0,
    size = 10
  ): Observable<PageResponse<StockMovement>> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size);

    return this.http.get<PageResponse<StockMovement>>(
      `${API_ENDPOINTS.stockMovements}/warehouse/${warehouseId}`,
      { params }
    );
  }

  getByProductAndWarehouse(
    productId: string,
    warehouseId: string,
    page = 0,
    size = 10
  ): Observable<PageResponse<StockMovement>> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size);

    return this.http.get<PageResponse<StockMovement>>(
      `${API_ENDPOINTS.stockMovements}/product/${productId}/warehouse/${warehouseId}`,
      { params }
    );
  }

  getByType(
    movementType: StockMovementType,
    page = 0,
    size = 10
  ): Observable<PageResponse<StockMovement>> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size);

    return this.http.get<PageResponse<StockMovement>>(
      `${API_ENDPOINTS.stockMovements}/type/${movementType}`,
      { params }
    );
  }
}
