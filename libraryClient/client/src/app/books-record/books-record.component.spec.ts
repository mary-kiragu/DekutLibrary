import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BooksRecordComponent } from './books-record.component';

describe('BooksRecordComponent', () => {
  let component: BooksRecordComponent;
  let fixture: ComponentFixture<BooksRecordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BooksRecordComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BooksRecordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
