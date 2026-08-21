import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_ENDPOINTS } from '../../../../core/constants/api-endpoints';
import { PageResponse } from '../../../../core/models/page-response.model';

import { Carrier } from '../models/carrier.model';
import { CarrierCreateRequest } from '../models/carrier-create-request.model';
import { CarrierUpdateRequest } from '../models/carrier-update-request.model';

@Injectable({
  providedIn: 'root'
})
export class CarrierService {

  private readonly http = inject(HttpClient);

  getAll(
    page = 0,
    size = 10,
    sort = 'companyName,asc'
  ): Observable<PageResponse<Carrier>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<Carrier>>(
      API_ENDPOINTS.carriers,
      { params }
    );
  }

  getById(
    id: string
  ): Observable<Carrier> {

    return this.http.get<Carrier>(
      `${API_ENDPOINTS.carriers}/${id}`
    );
  }

  create(
    request: CarrierCreateRequest
  ): Observable<Carrier> {

    return this.http.post<Carrier>(
      API_ENDPOINTS.carriers,
      request
    );
  }

  update(
    id: string,
    request: CarrierUpdateRequest
  ): Observable<Carrier> {

    return this.http.put<Carrier>(
      `${API_ENDPOINTS.carriers}/${id}`,
      request
    );
  }

  activate(
    id: string
  ): Observable<Carrier> {

    return this.http.patch<Carrier>(
      `${API_ENDPOINTS.carriers}/${id}/activate`,
      {}
    );
  }

  deactivate(
    id: string
  ): Observable<Carrier> {

    return this.http.patch<Carrier>(
      `${API_ENDPOINTS.carriers}/${id}/deactivate`,
      {}
    );
  }

  searchByCompanyName(
    companyName: string,
    page = 0,
    size = 10,
    sort = 'companyName,asc'
  ): Observable<PageResponse<Carrier>> {

    const params = new HttpParams()
      .set('companyName', companyName)
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<Carrier>>(
      `${API_ENDPOINTS.carriers}/search/company-name`,
      { params }
    );
  }

  searchByCode(
    code: string,
    page = 0,
    size = 10,
    sort = 'companyName,asc'
  ): Observable<PageResponse<Carrier>> {

    const params = new HttpParams()
      .set('code', code)
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<Carrier>>(
      `${API_ENDPOINTS.carriers}/search/code`,
      { params }
    );
  }

  searchByLicenseNumber(
    licenseNumber: string,
    page = 0,
    size = 10,
    sort = 'companyName,asc'
  ): Observable<PageResponse<Carrier>> {

    const params = new HttpParams()
      .set('licenseNumber', licenseNumber)
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<Carrier>>(
      `${API_ENDPOINTS.carriers}/search/license`,
      { params }
    );
  }
}
