import { Component, inject } from '@angular/core';
import { Subscriptions } from './../../../../services/subscriptions';

@Component({
  selector: 'app-subs-counter',
  imports: [],
  templateUrl: './subs-counter.html',
  styleUrl: './subs-counter.css',
})
export class SubsCounter {
  protected readonly localStorageService = inject(Subscriptions);
}
