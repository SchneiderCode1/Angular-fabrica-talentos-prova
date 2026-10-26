import { SubscriptionModal } from './components/subscription-modal/subscription-modal';
import { SubsDatabase } from './components/subs-database/subs-database';
import { Component, signal } from '@angular/core';
import { Footer } from './components/footer/footer';
import { Header } from './components/header/header';

@Component({
  selector: 'app-landing-page',
  imports: [Footer, Header, SubsDatabase, SubscriptionModal],
  templateUrl: './landing-page.html',
  styleUrl: './landing-page.css',
})
export class LandingPage {
  list = Array.from({ length: 5 }, (_, i) => i);

  protected readonly modalOpened = signal(false);

  openModal() {
    this.modalOpened.set(true)
  };

  closeModal() {
    this.modalOpened.set(false)
  };

  

}
