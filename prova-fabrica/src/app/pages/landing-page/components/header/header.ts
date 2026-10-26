import { Component } from '@angular/core';
import { SubsCounter } from '../subs-counter/subs-counter';

@Component({
  selector: 'app-header',
  imports: [SubsCounter],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {}
