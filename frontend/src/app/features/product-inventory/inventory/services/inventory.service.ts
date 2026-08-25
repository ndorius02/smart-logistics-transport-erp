import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_ENDPOINTS } from '../../../../core/constants/api-endpoints';
import { PageResponse } from '../../../../core/models/page-response.model';

import { Inventory } from '../models/inventory.model';
import { InventoryCreateRequest } from '../models/inventory-create-request.model';
import { InventoryMinimumStockUpdateRequest } from '../models/inventory-minimum-stock-update-request.model';

@Injectable({
  providedIn: 'root'
})
export class InventoryService {

  private readonly http =
    inject(HttpClient);

  getAll(
    page = 0,
    size = 10,
    sort = 'product.name,asc'
  ): Observable<PageResponse<Inventory>> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size)
        .set('sort', sort);

    return this.http.get<PageResponse<Inventory>>(
      API_ENDPOINTS.inventory,
      { params }
    );
  }

  getById(
    id: string
  ): Observable<Inventory> {

    return this.http.get<Inventory>(
      `${API_ENDPOINTS.inventory}/${id}`
    );
  }

  create(
    request: InventoryCreateRequest
  ): Observable<Inventory> {

    return this.http.post<Inventory>(
      API_ENDPOINTS.inventory,
      request
    );
  }

  getByProduct(
    productId: string,
    page = 0,
    size = 10
  ): Observable<PageResponse<Inventory>> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size);

    return this.http.get<PageResponse<Inventory>>(
      `${API_ENDPOINTS.inventory}/product/${productId}`,
      { params }
    );
  }

  getByWarehouse(
    warehouseId: string,
    page = 0,
    size = 10
  ): Observable<PageResponse<Inventory>> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size);

    return this.http.get<PageResponse<Inventory>>(
      `${API_ENDPOINTS.inventory}/warehouse/${warehouseId}`,
      { params }
    );
  }

  getByProductAndWarehouse(
    productId: string,
    warehouseId: string
  ): Observable<Inventory> {

    return this.http.get<Inventory>(
      `${API_ENDPOINTS.inventory}/product/${productId}/warehouse/${warehouseId}`
    );
  }

  getLowStock(
    page = 0,
    size = 10
  ): Observable<PageResponse<Inventory>> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size);

    return this.http.get<PageResponse<Inventory>>(
      `${API_ENDPOINTS.inventory}/low-stock`,
      { params }
    );
  }

  updateMinimumStockLevel(
    id: string,
    request: InventoryMinimumStockUpdateRequest
  ): Observable<Inventory> {

    return this.http.patch<Inventory>(
      `${API_ENDPOINTS.inventory}/${id}/minimum-stock-level`,
      request
    );
  }
}
