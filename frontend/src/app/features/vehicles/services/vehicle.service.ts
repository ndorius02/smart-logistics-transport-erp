import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_ENDPOINTS } from '../../../core/constants/api-endpoints';
import { PageResponse } from '../../../core/models/page-response.model';

import { Vehicle } from '../models/vehicle.model';
import { VehicleCreateRequest } from '../models/vehicle-create-request.model';
import { VehicleUpdateRequest } from '../models/vehicle-update-request.model';
import { VehicleStatus } from '../models/vehicle-status.model';

@Injectable({
  providedIn: 'root'
})
export class VehicleService {

  private readonly http = inject(HttpClient);

  getAll(
    page = 0,
    size = 10,
    sort = 'registrationNumber,asc'
  ): Observable<PageResponse<Vehicle>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<Vehicle>>(
      API_ENDPOINTS.vehicles,
      { params }
    );
  }

  getById(id: string): Observable<Vehicle> {
    return this.http.get<Vehicle>(
      `${API_ENDPOINTS.vehicles}/${id}`
    );
  }

  create(
    request: VehicleCreateRequest
  ): Observable<Vehicle> {
    return this.http.post<Vehicle>(
      API_ENDPOINTS.vehicles,
      request
    );
  }

  update(
    id: string,
    request: VehicleUpdateRequest
  ): Observable<Vehicle> {
    return this.http.put<Vehicle>(
      `${API_ENDPOINTS.vehicles}/${id}`,
      request
    );
  }

  activate(id: string): Observable<Vehicle> {
    return this.http.patch<Vehicle>(
      `${API_ENDPOINTS.vehicles}/${id}/activate`,
      {}
    );
  }

  deactivate(id: string): Observable<Vehicle> {
    return this.http.patch<Vehicle>(
      `${API_ENDPOINTS.vehicles}/${id}/deactivate`,
      {}
    );
  }

  searchByRegistrationNumber(
    registrationNumber: string,
    page = 0,
    size = 10,
    sort = 'registrationNumber,asc'
  ): Observable<PageResponse<Vehicle>> {

    const params = new HttpParams()
      .set('registrationNumber', registrationNumber)
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<Vehicle>>(
      `${API_ENDPOINTS.vehicles}/search/registration`,
      { params }
    );
  }

  searchByBrand(
    brand: string,
    page = 0,
    size = 10,
    sort = 'brand,asc'
  ): Observable<PageResponse<Vehicle>> {

    const params = new HttpParams()
      .set('brand', brand)
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<Vehicle>>(
      `${API_ENDPOINTS.vehicles}/search/brand`,
      { params }
    );
  }

  getByStatus(
    status: VehicleStatus,
    page = 0,
    size = 10,
    sort = 'registrationNumber,asc'
  ): Observable<PageResponse<Vehicle>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<Vehicle>>(
      `${API_ENDPOINTS.vehicles}/status/${status}`,
      { params }
    );
  }
}
