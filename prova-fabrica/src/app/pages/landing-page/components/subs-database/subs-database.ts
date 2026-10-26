import { Subscriptions } from './../../../../services/subscriptions';
import { Component, inject } from '@angular/core';

@Component({
  selector: 'app-subs-database',
  imports: [],
  templateUrl: './subs-database.html',
  styleUrl: './subs-database.css',
})
export class SubsDatabase {

  protected readonly service = inject(Subscriptions)
  name = "Nome";
  email = "exemplo@gmail.com";
  birthdate = "01/11/2004";
  race_length = 10;

}
