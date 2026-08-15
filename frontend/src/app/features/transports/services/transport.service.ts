import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_ENDPOINTS } from '../../../core/constants/api-endpoints';
import { PageResponse } from '../../../core/models/page-response.model';

import { Transport } from '../models/transport.model';
import { TransportCreateRequest } from '../models/transport-create-request.model';
import { TransportUpdateRequest } from '../models/transport-update-request.model';
import { TransportStatus } from '../models/transport-status.model';

@Injectable({
  providedIn: 'root'
})
export class TransportService {

  private readonly http = inject(HttpClient);

  getAll(
    page = 0,
    size = 10,
    sort = 'plannedDepartureAt,asc'
  ): Observable<PageResponse<Transport>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<Transport>>(
      API_ENDPOINTS.transports,
      { params }
    );
  }

  getById(id: string): Observable<Transport> {
    return this.http.get<Transport>(
      `${API_ENDPOINTS.transports}/${id}`
    );
  }

  create(
    request: TransportCreateRequest
  ): Observable<Transport> {
    return this.http.post<Transport>(
      API_ENDPOINTS.transports,
      request
    );
  }

  update(
    id: string,
    request: TransportUpdateRequest
  ): Observable<Transport> {
    return this.http.put<Transport>(
      `${API_ENDPOINTS.transports}/${id}`,
      request
    );
  }

  start(id: string): Observable<Transport> {
    return this.http.patch<Transport>(
      `${API_ENDPOINTS.transports}/${id}/start`,
      {}
    );
  }

  complete(id: string): Observable<Transport> {
    return this.http.patch<Transport>(
      `${API_ENDPOINTS.transports}/${id}/complete`,
      {}
    );
  }

  cancel(id: string): Observable<Transport> {
    return this.http.patch<Transport>(
      `${API_ENDPOINTS.transports}/${id}/cancel`,
      {}
    );
  }

  searchByCode(
    code: string,
    page = 0,
    size = 10,
    sort = 'plannedDepartureAt,asc'
  ): Observable<PageResponse<Transport>> {

    const params = new HttpParams()
      .set('code', code)
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<Transport>>(
      `${API_ENDPOINTS.transports}/search/code`,
      { params }
    );
  }

  getByStatus(
    status: TransportStatus,
    page = 0,
    size = 10,
    sort = 'plannedDepartureAt,asc'
  ): Observable<PageResponse<Transport>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<Transport>>(
      `${API_ENDPOINTS.transports}/status/${status}`,
      { params }
    );
  }
}
