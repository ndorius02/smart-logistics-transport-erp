import { Routes } from '@angular/router';

import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [

  // =========================
  // PUBLIC ROUTES
  // =========================

  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login')
        .then(m => m.Login)
  },

  // =========================
  // PRIVATE ROUTES
  // =========================

  {
    path: '',
    canActivate: [authGuard],

    loadComponent: () =>
      import('./layout/main-layout/main-layout')
        .then(m => m.MainLayout),

    children: [

      {
        path: 'dashboard',
        loadComponent: () =>
          import(
            './features/dashboard/dashboard/dashboard'
            ).then(m => m.Dashboard)
      },


      {
        path: 'warehouses/new',

        canActivate: [
          roleGuard
        ],

        data: {
          roles: [
            'ROLE_ADMIN',
            'ROLE_MANAGER'
          ]
        },

        loadComponent: () =>
          import(
            './features/warehouses/pages/warehouse-form/warehouse-form'
            ).then(m => m.WarehouseForm)
      },

      {
        path: 'warehouses/:id/edit',

        canActivate: [
          roleGuard
        ],

        data: {
          roles: [
            'ROLE_ADMIN',
            'ROLE_MANAGER'
          ]
        },

        loadComponent: () =>
          import(
            './features/warehouses/pages/warehouse-form/warehouse-form'
            ).then(m => m.WarehouseForm)
      },

      {
        path: 'warehouses/new',
        loadComponent: () =>
          import(
            './features/warehouses/pages/warehouse-form/warehouse-form'
            ).then(m => m.WarehouseForm)
      },

      {
        path: 'warehouses/:id/edit',
        loadComponent: () =>
          import(
            './features/warehouses/pages/warehouse-form/warehouse-form'
            ).then(m => m.WarehouseForm)
      },

      {
        path: 'warehouses',
        loadComponent: () =>
          import(
            './features/warehouses/pages/warehouse-list/warehouse-list'
            ).then(m => m.WarehouseList)
      },

      /*

      {
        path: 'vehicles',
        loadComponent: () =>
          import(
            './features/vehicles/vehicles'
          ).then(m => m.Vehicles)
      },

      {
        path: 'drivers',
        loadComponent: () =>
          import(
            './features/drivers/drivers'
          ).then(m => m.Drivers)
      },

      {
        path: 'transports',
        loadComponent: () =>
          import(
            './features/transports/transports'
          ).then(m => m.Transports)
      },
      */

      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  },

  // =========================
  // FALLBACK
  // =========================

  {
    path: '**',
    redirectTo: 'login'
  }
];
