import { Routes } from '@angular/router';

import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  // PUBLIC ROUTES
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login')
        .then(m => m.Login)
  },

  // PRIVATE ROUTES

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


      {
        path: 'transports/new',

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
            './features/transports/pages/transport-form/transport-form'
            ).then(m => m.TransportForm)
      },

      {
        path: 'transports/:id/edit',

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
            './features/transports/pages/transport-form/transport-form'
            ).then(m => m.TransportForm)
      },

      {
        path: 'transports',

        loadComponent: () =>
          import(
            './features/transports/pages/transport-list/transport-list'
            ).then(m => m.TransportList)
      },


      {
        path: 'business-partners/customers/new',

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
            './features/business-partners/customers/pages/customer-form/customer-form'
            ).then(m => m.CustomerForm)
      },

      {
        path: 'business-partners/customers/:id/edit',

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
            './features/business-partners/customers/pages/customer-form/customer-form'
            ).then(m => m.CustomerForm)
      },

      {
        path: 'business-partners/customers',

        loadComponent: () =>
          import(
            './features/business-partners/customers/pages/customer-list/customer-list'
            ).then(m => m.CustomerList)
      },

      {
        path: 'business-partners/suppliers/new',

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
            './features/business-partners/suppliers/pages/supplier-form/supplier-form'
            ).then(m => m.SupplierForm)
      },

      {
        path: 'business-partners/suppliers/:id/edit',

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
            './features/business-partners/suppliers/pages/supplier-form/supplier-form'
            ).then(m => m.SupplierForm)
      },

      {
        path: 'business-partners/suppliers',

        loadComponent: () =>
          import(
            './features/business-partners/suppliers/pages/supplier-list/supplier-list'
            ).then(m => m.SupplierList)
      },

      {
        path: 'business-partners/carriers/new',

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
            './features/business-partners/carriers/pages/carrier-form/carrier-form'
            ).then(m => m.CarrierForm)
      },

      {
        path: 'business-partners/carriers/:id/edit',

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
            './features/business-partners/carriers/pages/carrier-form/carrier-form'
            ).then(m => m.CarrierForm)
      },

      {
        path: 'business-partners/carriers',

        loadComponent: () =>
          import(
            './features/business-partners/carriers/pages/carrier-list/carrier-list'
            ).then(m => m.CarrierList)
      },


      {
        path: 'product-inventory/product-categories/new',

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
            './features/product-inventory/product-categories/pages/product-category-form/product-category-form'
            ).then(
            m => m.ProductCategoryForm
          )
      },

      {
        path: 'product-inventory/product-categories/:id/edit',

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
            './features/product-inventory/product-categories/pages/product-category-form/product-category-form'
            ).then(
            m => m.ProductCategoryForm
          )
      },

      {
        path: 'product-inventory/product-categories',

        loadComponent: () =>
          import(
            './features/product-inventory/product-categories/pages/product-category-list/product-category-list'
            ).then(
            m => m.ProductCategoryList
          )
      },












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
