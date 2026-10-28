import { Routes } from '@angular/router';
import { LandingPage } from './pages/landing-page/landing-page';
import { DetailsPage } from './pages/details-page/details-page';

export const routes: Routes = [
  { path: '', redirectTo: 'inscricoes', pathMatch: 'full' },
  { path: 'inscricoes', component: LandingPage },
  { path: 'inscricoes/:email', component: DetailsPage },
];
