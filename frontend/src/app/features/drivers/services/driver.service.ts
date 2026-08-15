import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_ENDPOINTS } from '../../../core/constants/api-endpoints';
import { PageResponse } from '../../../core/models/page-response.model';

import { Driver } from '../models/driver.model';
import { DriverCreateRequest } from '../models/driver-create-request.model';
import { DriverUpdateRequest } from '../models/driver-update-request.model';
import { DriverStatus } from '../models/driver-status.model';

@Injectable({
  providedIn: 'root'
})
export class DriverService {

  private readonly http = inject(HttpClient);

  getAll(
    page = 0,
    size = 10,
    sort = 'lastName,asc'
  ): Observable<PageResponse<Driver>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<Driver>>(
      API_ENDPOINTS.drivers,
      { params }
    );
  }

  getById(id: string): Observable<Driver> {
    return this.http.get<Driver>(
      `${API_ENDPOINTS.drivers}/${id}`
    );
  }

  create(
    request: DriverCreateRequest
  ): Observable<Driver> {
    return this.http.post<Driver>(
      API_ENDPOINTS.drivers,
      request
    );
  }

  update(
    id: string,
    request: DriverUpdateRequest
  ): Observable<Driver> {
    return this.http.put<Driver>(
      `${API_ENDPOINTS.drivers}/${id}`,
      request
    );
  }

  activate(id: string): Observable<Driver> {
    return this.http.patch<Driver>(
      `${API_ENDPOINTS.drivers}/${id}/activate`,
      {}
    );
  }

  deactivate(id: string): Observable<Driver> {
    return this.http.patch<Driver>(
      `${API_ENDPOINTS.drivers}/${id}/deactivate`,
      {}
    );
  }

  searchByLicenseNumber(
    licenseNumber: string,
    page = 0,
    size = 10,
    sort = 'licenseNumber,asc'
  ): Observable<PageResponse<Driver>> {

    const params = new HttpParams()
      .set('licenseNumber', licenseNumber)
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<Driver>>(
      `${API_ENDPOINTS.drivers}/search/license`,
      { params }
    );
  }

  searchByLastName(
    lastName: string,
    page = 0,
    size = 10,
    sort = 'lastName,asc'
  ): Observable<PageResponse<Driver>> {

    const params = new HttpParams()
      .set('lastName', lastName)
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<Driver>>(
      `${API_ENDPOINTS.drivers}/search/last-name`,
      { params }
    );
  }

  getByStatus(
    status: DriverStatus,
    page = 0,
    size = 10,
    sort = 'lastName,asc'
  ): Observable<PageResponse<Driver>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<Driver>>(
      `${API_ENDPOINTS.drivers}/status/${status}`,
      { params }
    );
  }
}
