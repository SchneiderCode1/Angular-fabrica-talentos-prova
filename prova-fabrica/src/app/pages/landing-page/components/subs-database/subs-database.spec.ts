import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubsDatabase } from './subs-database';

describe('SubsDatabase', () => {
  let component: SubsDatabase;
  let fixture: ComponentFixture<SubsDatabase>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubsDatabase],
    }).compileComponents();

    fixture = TestBed.createComponent(SubsDatabase);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
