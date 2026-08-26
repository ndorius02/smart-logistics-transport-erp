import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StockMovementCreateForm } from './stock-movement-create-form';

describe('StockMovementCreateForm', () => {
  let component: StockMovementCreateForm;
  let fixture: ComponentFixture<StockMovementCreateForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StockMovementCreateForm],
    }).compileComponents();

    fixture = TestBed.createComponent(StockMovementCreateForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
