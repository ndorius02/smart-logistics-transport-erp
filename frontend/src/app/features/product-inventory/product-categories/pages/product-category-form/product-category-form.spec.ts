import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductCategoryForm } from './product-category-form';

describe('ProductCategoryForm', () => {
  let component: ProductCategoryForm;
  let fixture: ComponentFixture<ProductCategoryForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductCategoryForm],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductCategoryForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
