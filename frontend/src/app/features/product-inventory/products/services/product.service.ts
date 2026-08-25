import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_ENDPOINTS } from '../../../../core/constants/api-endpoints';
import { PageResponse } from '../../../../core/models/page-response.model';

import { Product } from '../models/product.model';
import { ProductCreateRequest } from '../models/product-create-request.model';
import { ProductUpdateRequest } from '../models/product-update-request.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private readonly http =
    inject(HttpClient);

  getAll(
    page = 0,
    size = 10,
    sort = 'name,asc'
  ): Observable<PageResponse<Product>> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size)
        .set('sort', sort);

    return this.http.get<PageResponse<Product>>(
      API_ENDPOINTS.products,
      { params }
    );
  }

  getById(
    id: string
  ): Observable<Product> {

    return this.http.get<Product>(
      `${API_ENDPOINTS.products}/${id}`
    );
  }

  create(
    request: ProductCreateRequest
  ): Observable<Product> {

    return this.http.post<Product>(
      API_ENDPOINTS.products,
      request
    );
  }

  update(
    id: string,
    request: ProductUpdateRequest
  ): Observable<Product> {

    return this.http.put<Product>(
      `${API_ENDPOINTS.products}/${id}`,
      request
    );
  }

  activate(
    id: string
  ): Observable<Product> {

    return this.http.patch<Product>(
      `${API_ENDPOINTS.products}/${id}/activate`,
      {}
    );
  }

  deactivate(
    id: string
  ): Observable<Product> {

    return this.http.patch<Product>(
      `${API_ENDPOINTS.products}/${id}/deactivate`,
      {}
    );
  }

  searchBySku(
    sku: string,
    page = 0,
    size = 10,
    sort = 'name,asc'
  ): Observable<PageResponse<Product>> {

    const params =
      new HttpParams()
        .set('sku', sku)
        .set('page', page)
        .set('size', size)
        .set('sort', sort);

    return this.http.get<PageResponse<Product>>(
      `${API_ENDPOINTS.products}/search/sku`,
      { params }
    );
  }

  searchByName(
    name: string,
    page = 0,
    size = 10,
    sort = 'name,asc'
  ): Observable<PageResponse<Product>> {

    const params =
      new HttpParams()
        .set('name', name)
        .set('page', page)
        .set('size', size)
        .set('sort', sort);

    return this.http.get<PageResponse<Product>>(
      `${API_ENDPOINTS.products}/search/name`,
      { params }
    );
  }

  getByCategory(
    categoryId: string,
    page = 0,
    size = 10,
    sort = 'name,asc'
  ): Observable<PageResponse<Product>> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size)
        .set('sort', sort);

    return this.http.get<PageResponse<Product>>(
      `${API_ENDPOINTS.products}/category/${categoryId}`,
      { params }
    );
  }
}
