import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_ENDPOINTS } from '../../../../core/constants/api-endpoints';
import { PageResponse } from '../../../../core/models/page-response.model';

import { Customer } from '../models/customer.model';
import { CustomerCreateRequest } from '../models/customer-create-request.model';
import { CustomerUpdateRequest } from '../models/customer-update-request.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private readonly http =
    inject(HttpClient);

  getAll(
    page = 0,
    size = 10,
    sort = 'companyName,asc'
  ): Observable<PageResponse<Customer>> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size)
        .set('sort', sort);

    return this.http.get<PageResponse<Customer>>(
      API_ENDPOINTS.customers,
      { params }
    );
  }

  getById(
    id: string
  ): Observable<Customer> {

    return this.http.get<Customer>(
      `${API_ENDPOINTS.customers}/${id}`
    );
  }

  create(
    request: CustomerCreateRequest
  ): Observable<Customer> {

    return this.http.post<Customer>(
      API_ENDPOINTS.customers,
      request
    );
  }

  update(
    id: string,
    request: CustomerUpdateRequest
  ): Observable<Customer> {

    return this.http.put<Customer>(
      `${API_ENDPOINTS.customers}/${id}`,
      request
    );
  }

  activate(
    id: string
  ): Observable<Customer> {

    return this.http.patch<Customer>(
      `${API_ENDPOINTS.customers}/${id}/activate`,
      {}
    );
  }

  deactivate(
    id: string
  ): Observable<Customer> {

    return this.http.patch<Customer>(
      `${API_ENDPOINTS.customers}/${id}/deactivate`,
      {}
    );
  }

  searchByCompanyName(
    companyName: string,
    page = 0,
    size = 10,
    sort = 'companyName,asc'
  ): Observable<PageResponse<Customer>> {

    const params =
      new HttpParams()
        .set('companyName', companyName)
        .set('page', page)
        .set('size', size)
        .set('sort', sort);

    return this.http.get<PageResponse<Customer>>(
      `${API_ENDPOINTS.customers}/search/company-name`,
      { params }
    );
  }

  searchByCode(
    code: string,
    page = 0,
    size = 10,
    sort = 'companyName,asc'
  ): Observable<PageResponse<Customer>> {

    const params =
      new HttpParams()
        .set('code', code)
        .set('page', page)
        .set('size', size)
        .set('sort', sort);

    return this.http.get<PageResponse<Customer>>(
      `${API_ENDPOINTS.customers}/search/code`,
      { params }
    );
  }
}
