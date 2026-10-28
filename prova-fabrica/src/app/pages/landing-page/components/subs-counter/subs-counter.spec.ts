import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubsCounter } from './subs-counter';

describe('SubsCounter', () => {
  let component: SubsCounter;
  let fixture: ComponentFixture<SubsCounter>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubsCounter],
    }).compileComponents();

    fixture = TestBed.createComponent(SubsCounter);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
