import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_ENDPOINTS } from '../../../../core/constants/api-endpoints';
import { PageResponse } from '../../../../core/models/page-response.model';

import { ProductCategory } from '../models/product-category.model';
import { ProductCategoryCreateRequest } from '../models/product-category-create-request.model';
import { ProductCategoryUpdateRequest } from '../models/product-category-update-request.model';

@Injectable({
  providedIn: 'root'
})
export class ProductCategoryService {

  private readonly http =
    inject(HttpClient);

  getAll(
    page = 0,
    size = 10,
    sort = 'name,asc'
  ): Observable<PageResponse<ProductCategory>> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size)
        .set('sort', sort);

    return this.http.get<PageResponse<ProductCategory>>(
      API_ENDPOINTS.productCategories,
      { params }
    );
  }

  getById(
    id: string
  ): Observable<ProductCategory> {

    return this.http.get<ProductCategory>(
      `${API_ENDPOINTS.productCategories}/${id}`
    );
  }

  create(
    request: ProductCategoryCreateRequest
  ): Observable<ProductCategory> {

    return this.http.post<ProductCategory>(
      API_ENDPOINTS.productCategories,
      request
    );
  }

  update(
    id: string,
    request: ProductCategoryUpdateRequest
  ): Observable<ProductCategory> {

    return this.http.put<ProductCategory>(
      `${API_ENDPOINTS.productCategories}/${id}`,
      request
    );
  }

  activate(
    id: string
  ): Observable<ProductCategory> {

    return this.http.patch<ProductCategory>(
      `${API_ENDPOINTS.productCategories}/${id}/activate`,
      {}
    );
  }

  deactivate(
    id: string
  ): Observable<ProductCategory> {

    return this.http.patch<ProductCategory>(
      `${API_ENDPOINTS.productCategories}/${id}/deactivate`,
      {}
    );
  }

  searchByCode(
    code: string,
    page = 0,
    size = 10,
    sort = 'name,asc'
  ): Observable<PageResponse<ProductCategory>> {

    const params =
      new HttpParams()
        .set('code', code)
        .set('page', page)
        .set('size', size)
        .set('sort', sort);

    return this.http.get<PageResponse<ProductCategory>>(
      `${API_ENDPOINTS.productCategories}/search/code`,
      { params }
    );
  }

  searchByName(
    name: string,
    page = 0,
    size = 10,
    sort = 'name,asc'
  ): Observable<PageResponse<ProductCategory>> {

    const params =
      new HttpParams()
        .set('name', name)
        .set('page', page)
        .set('size', size)
        .set('sort', sort);

    return this.http.get<PageResponse<ProductCategory>>(
      `${API_ENDPOINTS.productCategories}/search/name`,
      { params }
    );
  }
}
