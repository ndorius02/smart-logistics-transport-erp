import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InventoryCreateForm } from './inventory-create-form';

describe('InventoryCreateForm', () => {
  let component: InventoryCreateForm;
  let fixture: ComponentFixture<InventoryCreateForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InventoryCreateForm],
    }).compileComponents();

    fixture = TestBed.createComponent(InventoryCreateForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
