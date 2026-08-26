import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StockMovementList } from './stock-movement-list';

describe('StockMovementList', () => {
  let component: StockMovementList;
  let fixture: ComponentFixture<StockMovementList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StockMovementList],
    }).compileComponents();

    fixture = TestBed.createComponent(StockMovementList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
