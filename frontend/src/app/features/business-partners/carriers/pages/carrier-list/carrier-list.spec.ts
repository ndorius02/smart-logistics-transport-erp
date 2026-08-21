import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarrierList } from './carrier-list';

describe('CarrierList', () => {
  let component: CarrierList;
  let fixture: ComponentFixture<CarrierList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarrierList],
    }).compileComponents();

    fixture = TestBed.createComponent(CarrierList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
