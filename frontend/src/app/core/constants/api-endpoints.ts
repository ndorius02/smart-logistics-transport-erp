import { environment } from '../../../environments/environment.development';

export const API_ENDPOINTS = {
  auth: {
    login: `${environment.apiUrl}/auth/login`
  },

  users: `${environment.apiUrl}/users`,
  roles: `${environment.apiUrl}/roles`,
  warehouses: `${environment.apiUrl}/warehouses`,
  vehicles: `${environment.apiUrl}/vehicles`,
  drivers: `${environment.apiUrl}/drivers`,
  transports: `${environment.apiUrl}/transports`,
  customers: `${environment.apiUrl}/customers`,
  suppliers: `${environment.apiUrl}/suppliers`,
  carriers: `${environment.apiUrl}/carriers`,
  productCategories: `${environment.apiUrl}/product-categories`,
} as const;
