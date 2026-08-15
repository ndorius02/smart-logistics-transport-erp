import {
  Component,
  inject,
  OnInit,
  signal
} from '@angular/core';

import { DatePipe } from '@angular/common';

import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';

import {
  DashboardService,
  DashboardStats
} from '../services/dashboard.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    DatePipe,
    MatCardModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatTableModule
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {

  private readonly dashboardService =
    inject(DashboardService);

  readonly loading =
    signal(false);

  readonly stats =
    signal<DashboardStats>({
      totalWarehouses: 0,
      availableVehicles: 0,
      availableDrivers: 0,
      plannedTransports: 0,
      inProgressTransports: 0,
      completedTransports: 0,
      recentTransports: []
    });

  readonly displayedColumns: string[] = [
    'code',
    'origin',
    'destination',
    'vehicle',
    'driver',
    'departure',
    'status'
  ];

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {

    this.loading.set(true);

    this.dashboardService
      .getStats()
      .subscribe({

        next: stats => {

          this.stats.set(stats);

          this.loading.set(false);
        },

        error: error => {

          console.error(
            'Unable to load dashboard',
            error
          );

          this.loading.set(false);
        }
      });
  }
}
