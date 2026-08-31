import { Component,DestroyRef, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { finalize } from 'rxjs/operators';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { GoodsReceptionService } from '../../services/goods-reception.service';
import { GoodsReceptionResponse } from '../../models/goods-reception-response';

@Component({
  selector: 'app-goods-reception-list',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatTooltipModule
  ],

  templateUrl: './goods-reception-list.html',
  styleUrl: './goods-reception-list.scss'
})
export class GoodsReceptionList implements OnInit {

  private readonly goodsReceptionService = inject(GoodsReceptionService);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  readonly goodsReceptions = signal<GoodsReceptionResponse[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly errorMessage = signal<string | null>(null);

  readonly displayedColumns: string[] = [
    'reference',
    'purchaseOrder',
    'product',
    'warehouse',
    'quantity',
    'receptionDate',
    'createdBy'
  ];

  pageIndex = 0;
  pageSize = 10;

  ngOnInit(): void {this.loadGoodsReceptions();}

  loadGoodsReceptions(): void {
    this.loading.set(true);
    this.errorMessage.set(null);
    this.goodsReceptionService
      .getAll(
        this.pageIndex,
        this.pageSize
      )
      .pipe(finalize(() => {this.loading.set(false);}),
        takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: page => {
          this.goodsReceptions.set(page?.content ?? []);
          this.totalElements.set(page?.totalElements ?? 0);
        },
        error: error => {
          console.error(
            'Failed to load goods receptions',
            error
          );
          this.goodsReceptions.set([]);
          this.totalElements.set(0);
          this.errorMessage.set(
            error?.error?.message ??
            'Unable to load goods receptions.'
          );
        }
      });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadGoodsReceptions();
  }

  createGoodsReception(): void {
    this.router.navigate([
      '/procurement/goods-receptions/new'
    ]);
  }

  openPurchaseOrder(purchaseOrderId: string): void {
    this.router.navigate([
      '/procurement/purchase-orders',
      purchaseOrderId
    ]);
  }
}
