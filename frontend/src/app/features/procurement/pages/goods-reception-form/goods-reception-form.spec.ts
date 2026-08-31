import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GoodsReceptionForm } from './goods-reception-form';

describe('GoodsReceptionForm', () => {
  let component: GoodsReceptionForm;
  let fixture: ComponentFixture<GoodsReceptionForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GoodsReceptionForm],
    }).compileComponents();

    fixture = TestBed.createComponent(GoodsReceptionForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
