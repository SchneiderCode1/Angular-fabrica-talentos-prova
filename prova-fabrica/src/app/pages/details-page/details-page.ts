import { Component, computed, inject, input } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { Subscriptions } from '../../services/subscriptions';
import { Header } from '../landing-page/components/header/header';
import { Footer } from '../landing-page/components/footer/footer';
import { DATA_PROVA, ROTULO_CATEGORIA, avaliarInscricao, normalizarEmail } from '../../dominio/inscricao.regras';
import { formatarDataBr } from '../../dominio/datas';

@Component({
  selector: 'app-details-page',
  imports: [RouterLink, Header, Footer],
  templateUrl: './details-page.html',
  styleUrl: './details-page.css',
})
export class DetailsPage {
  protected readonly localStorageService = inject(Subscriptions);
  private readonly router = inject(Router);

  readonly email = input.required<string>();

  protected readonly inscricao = computed(() =>
    this.localStorageService.subscriptions().find(p => p.email === normalizarEmail(this.email())),
  );

  protected readonly avaliacao = computed(() => {
    const inscricao = this.inscricao();
    return inscricao ? avaliarInscricao(inscricao.birthdate, inscricao.race_length, DATA_PROVA) : null;
  });

  protected readonly rotuloCategoria = ROTULO_CATEGORIA;
  protected readonly formatarDataBr = formatarDataBr;

  cancelar(email: string): void {
    if (this.localStorageService.remove(email)) {
      this.router.navigate(['/inscricoes']);
    }
  }
}
