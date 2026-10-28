import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubscriptionModal } from './subscription-modal';

describe('SubscriptionModal', () => {
  let component: SubscriptionModal;
  let fixture: ComponentFixture<SubscriptionModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubscriptionModal],
    }).compileComponents();

    fixture = TestBed.createComponent(SubscriptionModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
