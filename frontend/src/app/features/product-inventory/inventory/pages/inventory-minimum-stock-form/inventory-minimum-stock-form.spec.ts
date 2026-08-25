import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InventoryMinimumStockForm } from './inventory-minimum-stock-form';

describe('InventoryMinimumStockForm', () => {
  let component: InventoryMinimumStockForm;
  let fixture: ComponentFixture<InventoryMinimumStockForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InventoryMinimumStockForm],
    }).compileComponents();

    fixture = TestBed.createComponent(InventoryMinimumStockForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
