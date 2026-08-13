import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import { API_ENDPOINTS } from '../constants/api-endpoints';
import { LoginRequest } from '../models/login-request.model';
import { LoginResponse } from '../models/login-response.model';
import type { JwtPayload } from '../models/jwt-payload.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly http = inject(HttpClient);

  private readonly TOKEN_KEY = 'access_token';

  /**
   * Authenticate the user with the backend.
   * The JWT returned by Spring Boot is stored in localStorage.
   */
  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(
        API_ENDPOINTS.auth.login,
        request
      )
      .pipe(
        tap(response => {
          this.setToken(response.token);
        })
      );
  }

  /**
   * Remove the authentication token.
   */
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  /**
   * Return the JWT stored in localStorage.
   */
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  /**
   * Check whether the user currently has
   * a valid and non-expired JWT.
   */
  isAuthenticated(): boolean {
    const token = this.getToken();

    if (!token) {
      return false;
    }

    if (this.isTokenExpired()) {
      this.logout();
      return false;
    }

    return true;
  }

  /**
   * Return the authorities contained in the JWT.
   *
   * Example:
   * ["ROLE_ADMIN"]
   */
  getAuthorities(): string[] {
    return this.getPayload()?.authorities ?? [];
  }

  /**
   * Check whether the authenticated user
   * has a specific role.
   */
  hasRole(role: string): boolean {
    return this.getAuthorities().includes(role);
  }

  /**
   * Check whether the authenticated user
   * has at least one of the provided roles.
   */
  hasAnyRole(roles: string[]): boolean {
    return roles.some(role => this.hasRole(role));
  }

  /**
   * Store the JWT in localStorage.
   */
  private setToken(token: string): void {
    localStorage.setItem(
      this.TOKEN_KEY,
      token
    );
  }

  /**
   * Decode the JWT payload.
   *
   * A JWT has the following structure:
   *
   * header.payload.signature
   */
  private getPayload(): JwtPayload | null {
    const token = this.getToken();

    if (!token) {
      return null;
    }

    try {
      const payloadPart = token.split('.')[1];

      if (!payloadPart) {
        return null;
      }

      const base64 = payloadPart
        .replace(/-/g, '+')
        .replace(/_/g, '/');

      const decodedPayload = atob(base64);

      const payload: JwtPayload =
        JSON.parse(decodedPayload);

      return payload;

    } catch {
      return null;
    }
  }

  /**
   * Check the JWT expiration date.
   *
   * JWT "exp" is expressed in seconds.
   * Date.now() is expressed in milliseconds,
   * therefore we divide it by 1000.
   */
  private isTokenExpired(): boolean {
    const payload = this.getPayload();

    if (!payload) {
      return true;
    }

    const currentTime =
      Math.floor(Date.now() / 1000);

    return payload.exp <= currentTime;
  }
}
