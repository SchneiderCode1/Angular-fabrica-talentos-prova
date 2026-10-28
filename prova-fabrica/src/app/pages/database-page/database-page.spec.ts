import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DatabasePage } from './database-page';

describe('DatabasePage', () => {
  let component: DatabasePage;
  let fixture: ComponentFixture<DatabasePage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DatabasePage],
    }).compileComponents();

    fixture = TestBed.createComponent(DatabasePage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
