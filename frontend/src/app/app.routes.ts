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


      {
        path: 'vehicles/new',

        canActivate: [
          roleGuard
        ],

        data: {
          roles: [
            'ROLE_ADMIN',
            'ROLE_TRANSPORT_COORDINATOR'
          ]
        },

        loadComponent: () =>
          import(
            './features/vehicles/pages/vehicle-form/vehicle-form'
            ).then(m => m.VehicleForm)
      },

      {
        path: 'vehicles/:id/edit',

        canActivate: [
          roleGuard
        ],

        data: {
          roles: [
            'ROLE_ADMIN',
            'ROLE_TRANSPORT_COORDINATOR'
          ]
        },

        loadComponent: () =>
          import(
            './features/vehicles/pages/vehicle-form/vehicle-form'
            ).then(m => m.VehicleForm)
      },

      {
        path: 'vehicles',

        loadComponent: () =>
          import(
            './features/vehicles/pages/vehicle-list/vehicle-list'
            ).then(m => m.VehicleList)
      },


      {
        path: 'drivers/new',

        canActivate: [
          roleGuard
        ],

        data: {
          roles: [
            'ROLE_ADMIN',
            'ROLE_TRANSPORT_COORDINATOR'
          ]
        },

        loadComponent: () =>
          import(
            './features/drivers/pages/driver-form/driver-form'
            ).then(m => m.DriverForm)
      },

      {
        path: 'drivers/:id/edit',

        canActivate: [
          roleGuard
        ],

        data: {
          roles: [
            'ROLE_ADMIN',
            'ROLE_TRANSPORT_COORDINATOR'
          ]
        },

        loadComponent: () =>
          import(
            './features/drivers/pages/driver-form/driver-form'
            ).then(m => m.DriverForm)
      },

      {
        path: 'drivers',

        loadComponent: () =>
          import(
            './features/drivers/pages/driver-list/driver-list'
            ).then(m => m.DriverList)
      },






      /*

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
