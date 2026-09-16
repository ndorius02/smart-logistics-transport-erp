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

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

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

  getAuthorities(): string[] {
    return this.getPayload()?.authorities ?? [];
  }

  hasRole(role: string): boolean {
    return this.getAuthorities().includes(role);
  }

  hasAnyRole(roles: string[]): boolean {
    return roles.some(role => this.hasRole(role));
  }

  private setToken(token: string): void {
    localStorage.setItem(
      this.TOKEN_KEY,
      token
    );
  }

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
