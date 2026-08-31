import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GoodsReceptionList } from './goods-reception-list';

describe('GoodsReceptionList', () => {
  let component: GoodsReceptionList;
  let fixture: ComponentFixture<GoodsReceptionList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GoodsReceptionList],
    }).compileComponents();

    fixture = TestBed.createComponent(GoodsReceptionList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
