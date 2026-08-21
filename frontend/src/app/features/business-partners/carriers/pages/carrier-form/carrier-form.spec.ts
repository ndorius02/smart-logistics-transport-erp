import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarrierForm } from './carrier-form';

describe('CarrierForm', () => {
  let component: CarrierForm;
  let fixture: ComponentFixture<CarrierForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarrierForm],
    }).compileComponents();

    fixture = TestBed.createComponent(CarrierForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
