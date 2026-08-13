import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_ENDPOINTS } from '../../../core/constants/api-endpoints';
import { PageResponse } from '../../../core/models/page-response.model';

import { Warehouse } from '../models/warehouse.model';
import { WarehouseCreateRequest } from '../models/warehouse-create-request.model';
import { WarehouseUpdateRequest } from '../models/warehouse-update-request.model';

@Injectable({
  providedIn: 'root'
})
export class WarehouseService {

  private readonly http = inject(HttpClient);

  getAll(
    page = 0,
    size = 10,
    sort = 'name,asc'
  ): Observable<PageResponse<Warehouse>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<Warehouse>>(
      API_ENDPOINTS.warehouses,
      { params }
    );
  }

  getById(id: string): Observable<Warehouse> {
    return this.http.get<Warehouse>(
      `${API_ENDPOINTS.warehouses}/${id}`
    );
  }

  create(
    request: WarehouseCreateRequest
  ): Observable<Warehouse> {
    return this.http.post<Warehouse>(
      API_ENDPOINTS.warehouses,
      request
    );
  }

  update(
    id: string,
    request: WarehouseUpdateRequest
  ): Observable<Warehouse> {
    return this.http.put<Warehouse>(
      `${API_ENDPOINTS.warehouses}/${id}`,
      request
    );
  }

  activate(id: string): Observable<Warehouse> {
    return this.http.patch<Warehouse>(
      `${API_ENDPOINTS.warehouses}/${id}/activate`,
      {}
    );
  }

  deactivate(id: string): Observable<Warehouse> {
    return this.http.patch<Warehouse>(
      `${API_ENDPOINTS.warehouses}/${id}/deactivate`,
      {}
    );
  }

  searchByName(
    name: string,
    page = 0,
    size = 10,
    sort = 'name,asc'
  ): Observable<PageResponse<Warehouse>> {

    const params = new HttpParams()
      .set('name', name)
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<Warehouse>>(
      `${API_ENDPOINTS.warehouses}/search`,
      { params }
    );
  }
}
