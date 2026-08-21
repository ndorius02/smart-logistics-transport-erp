import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_ENDPOINTS } from '../../../../core/constants/api-endpoints';
import { PageResponse } from '../../../../core/models/page-response.model';

import { Supplier } from '../models/supplier.model';
import { SupplierCreateRequest } from '../models/supplier-create-request.model';
import { SupplierUpdateRequest } from '../models/supplier-update-request.model';

@Injectable({
  providedIn: 'root'
})
export class SupplierService {

  private readonly http =
    inject(HttpClient);

  getAll(
    page = 0,
    size = 10,
    sort = 'companyName,asc'
  ): Observable<PageResponse<Supplier>> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size)
        .set('sort', sort);

    return this.http.get<PageResponse<Supplier>>(
      API_ENDPOINTS.suppliers,
      { params }
    );
  }

  getById(
    id: string
  ): Observable<Supplier> {

    return this.http.get<Supplier>(
      `${API_ENDPOINTS.suppliers}/${id}`
    );
  }

  create(
    request: SupplierCreateRequest
  ): Observable<Supplier> {

    return this.http.post<Supplier>(
      API_ENDPOINTS.suppliers,
      request
    );
  }

  update(
    id: string,
    request: SupplierUpdateRequest
  ): Observable<Supplier> {

    return this.http.put<Supplier>(
      `${API_ENDPOINTS.suppliers}/${id}`,
      request
    );
  }

  activate(
    id: string
  ): Observable<Supplier> {

    return this.http.patch<Supplier>(
      `${API_ENDPOINTS.suppliers}/${id}/activate`,
      {}
    );
  }

  deactivate(
    id: string
  ): Observable<Supplier> {

    return this.http.patch<Supplier>(
      `${API_ENDPOINTS.suppliers}/${id}/deactivate`,
      {}
    );
  }

  searchByCompanyName(
    companyName: string,
    page = 0,
    size = 10,
    sort = 'companyName,asc'
  ): Observable<PageResponse<Supplier>> {

    const params =
      new HttpParams()
        .set('companyName', companyName)
        .set('page', page)
        .set('size', size)
        .set('sort', sort);

    return this.http.get<PageResponse<Supplier>>(
      `${API_ENDPOINTS.suppliers}/search/company-name`,
      { params }
    );
  }

  searchByCode(
    code: string,
    page = 0,
    size = 10,
    sort = 'companyName,asc'
  ): Observable<PageResponse<Supplier>> {

    const params =
      new HttpParams()
        .set('code', code)
        .set('page', page)
        .set('size', size)
        .set('sort', sort);

    return this.http.get<PageResponse<Supplier>>(
      `${API_ENDPOINTS.suppliers}/search/code`,
      { params }
    );
  }
}
