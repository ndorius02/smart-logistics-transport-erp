import {
  Component,
  inject,
  OnInit,
  signal
} from '@angular/core';

import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';

import { MatTableModule } from '@angular/material/table';
import {
  MatPaginatorModule,
  PageEvent
} from '@angular/material/paginator';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

import {
  MatSnackBar,
  MatSnackBarModule
} from '@angular/material/snack-bar';

import { Transport } from '../../models/transport.model';
import { TransportStatus } from '../../models/transport-status.model';
import { TransportService } from '../../services/transport.service';

import { AuthService } from '../../../../core/auth/auth';

@Component({
  selector: 'app-transport-list',
  standalone: true,
  imports: [
    DatePipe,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatSnackBarModule
  ],
  templateUrl: './transport-list.html',
  styleUrl: './transport-list.scss'
})
export class TransportList implements OnInit {

  private readonly transportService =
    inject(TransportService);

  private readonly router =
    inject(Router);

  private readonly authService =
    inject(AuthService);

  private readonly snackBar =
    inject(MatSnackBar);

  readonly transports =
    signal<Transport[]>([]);

  readonly loading =
    signal(false);

  readonly totalElements =
    signal(0);

  readonly pageIndex =
    signal(0);

  readonly pageSize =
    signal(10);

  readonly searchTerm =
    signal('');

  readonly selectedStatus =
    signal<TransportStatus | ''>('');

  readonly transportStatuses =
    Object.values(TransportStatus);

  readonly displayedColumns: string[] = [
    'code',
    'origin',
    'destination',
    'vehicle',
    'driver',
    'plannedDeparture',
    'plannedArrival',
    'status',
    'actions'
  ];

  ngOnInit(): void {
    this.loadTransports();
  }

  loadTransports(): void {

    this.loading.set(true);

    const code =
      this.searchTerm().trim();

    const status =
      this.selectedStatus();

    let request$;

    if (status) {

      request$ =
        this.transportService.getByStatus(
          status,
          this.pageIndex(),
          this.pageSize()
        );

    } else if (code) {

      request$ =
        this.transportService.searchByCode(
          code,
          this.pageIndex(),
          this.pageSize()
        );

    } else {

      request$ =
        this.transportService.getAll(
          this.pageIndex(),
          this.pageSize()
        );
    }

    request$.subscribe({

      next: response => {

        this.transports.set(
          response.content
        );

        this.totalElements.set(
          response.totalElements
        );

        this.loading.set(false);
      },

      error: error => {

        console.error(
          'Unable to load transports',
          error
        );

        this.loading.set(false);

        this.snackBar.open(
          'Unable to load transports',
          'Close',
          {
            duration: 4000
          }
        );
      }
    });
  }

  search(): void {
    this.pageIndex.set(0);
    this.loadTransports();
  }

  clearFilters(): void {

    this.searchTerm.set('');
    this.selectedStatus.set('');

    this.pageIndex.set(0);

    this.loadTransports();
  }

  onPageChange(
    event: PageEvent
  ): void {

    this.pageIndex.set(
      event.pageIndex
    );

    this.pageSize.set(
      event.pageSize
    );

    this.loadTransports();
  }

  createTransport(): void {

    this.router.navigate([
      '/transports/new'
    ]);
  }

  editTransport(
    transport: Transport
  ): void {

    this.router.navigate([
      '/transports',
      transport.id,
      'edit'
    ]);
  }

  startTransport(
    transport: Transport
  ): void {

    this.transportService
      .start(transport.id)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Transport started successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.loadTransports();
        },

        error: error => {

          this.showBackendError(
            error,
            'Unable to start transport'
          );
        }
      });
  }

  completeTransport(
    transport: Transport
  ): void {

    this.transportService
      .complete(transport.id)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Transport completed successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.loadTransports();
        },

        error: error => {

          this.showBackendError(
            error,
            'Unable to complete transport'
          );
        }
      });
  }

  cancelTransport(
    transport: Transport
  ): void {

    this.transportService
      .cancel(transport.id)
      .subscribe({

        next: () => {

          this.snackBar.open(
            'Transport cancelled successfully',
            'Close',
            {
              duration: 3000
            }
          );

          this.loadTransports();
        },

        error: error => {

          this.showBackendError(
            error,
            'Unable to cancel transport'
          );
        }
      });
  }

  canManageTransport(): boolean {

    return this.authService.hasAnyRole([
      'ROLE_ADMIN',
      'ROLE_TRANSPORT_COORDINATOR'
    ]);
  }

  canEditTransport(
    transport: Transport
  ): boolean {

    return (
      this.canManageTransport() &&
      transport.status === TransportStatus.PLANNED
    );
  }

  canStartTransport(
    transport: Transport
  ): boolean {

    return (
      this.canManageTransport() &&
      transport.status === TransportStatus.PLANNED
    );
  }

  canCancelTransport(
    transport: Transport
  ): boolean {

    return (
      this.canManageTransport() &&
      transport.status === TransportStatus.PLANNED
    );
  }

  canCompleteTransport(
    transport: Transport
  ): boolean {

    return (
      this.canManageTransport() &&
      transport.status === TransportStatus.IN_PROGRESS
    );
  }

  private showBackendError(
    error: any,
    fallbackMessage: string
  ): void {

    const backendMessage =
      error?.error?.message;

    this.snackBar.open(
      backendMessage ?? fallbackMessage,
      'Close',
      {
        duration: 5000
      }
    );
  }
}
