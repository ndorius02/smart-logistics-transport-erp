import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductCategoryList } from './product-category-list';

describe('ProductCategoryList', () => {
  let component: ProductCategoryList;
  let fixture: ComponentFixture<ProductCategoryList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductCategoryList],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductCategoryList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
