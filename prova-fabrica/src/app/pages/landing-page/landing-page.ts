import { SubscriptionModal } from './components/subscription-modal/subscription-modal';
import { Component, signal, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { formatarDataBr } from '../../dominio/datas';
import { Footer } from './components/footer/footer';
import { Header } from './components/header/header';
import { Subscriptions } from './../../services/subscriptions';
import { ProfileModel } from './components/subscription-modal/models/subscription-model';
import { AvaliacaoInscricao, DATA_PROVA, ROTULO_CATEGORIA, avaliarInscricao } from '../../dominio/inscricao.regras';

@Component({
  selector: 'app-landing-page',
  imports: [Footer, Header, SubscriptionModal, RouterLink],
  templateUrl: './landing-page.html',
  styleUrl: './landing-page.css',
})
export class LandingPage {
  protected readonly localStorageService = inject(Subscriptions);

  protected readonly modalOpened = signal(false);

  protected readonly rotuloCategoria = ROTULO_CATEGORIA;
  protected readonly formatarDataBr = formatarDataBr;

  protected avaliar(inscricao: ProfileModel): AvaliacaoInscricao {
    return avaliarInscricao(inscricao.birthdate, inscricao.race_length, DATA_PROVA);
  }

  
  openModal(): void {
    this.modalOpened.set(true)
  };

  closeModal(): void {
    this.modalOpened.set(false)
  };

}
