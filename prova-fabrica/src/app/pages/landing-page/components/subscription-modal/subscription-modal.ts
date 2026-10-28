import { Subscriptions } from './../../../../services/subscriptions';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { criarFormularioInscricao, mensagemDoCampo } from '../../../../formularios/inscricao.formulario';
import { normalizarNome } from '../../../../dominio/inscricao.validacao';
import { ProfileModel } from './models/subscription-model';
import {
  DATA_PROVA,
  ErroRegulamento,
  avaliarInscricao,
  normalizarEmail,
} from '../../../../dominio/inscricao.regras';

const MENSAGEM_REGULAMENTO: Record<ErroRegulamento, string> = {
  IDADE_MINIMA: 'Inscrição recusada: é preciso ter pelo menos 14 anos na data da prova (14/03/2027).',
  DISTANCIA_NAO_PERMITIDA:
    'Inscrição recusada: o percurso de 10 km é só para quem tem 18 anos ou mais na data da prova. Escolha 5 km.',
};

@Component({
  selector: 'app-subscription-modal',
  imports: [ReactiveFormsModule],
  templateUrl: './subscription-modal.html',
  styleUrl: './subscription-modal.css',
})
export class SubscriptionModal {
  private readonly localStorageService = inject(Subscriptions);
  private readonly fb = inject(FormBuilder);

  protected readonly form = criarFormularioInscricao(this.fb);

  protected erro(campo: keyof typeof this.form.controls): string | null {
    return mensagemDoCampo(this.form.controls[campo]);
  }

  protected readonly recusa = signal<string | null>(null);

  injectInfo() {
    this.recusa.set(null);

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const valores = this.form.getRawValue();

    const profile: ProfileModel = {
      username: normalizarNome(valores.username),
      email: normalizarEmail(valores.email),
      birthdate: valores.birthdate,
      race_length: valores.race_length as ProfileModel['race_length'],
    };

    const avaliacao = avaliarInscricao(profile.birthdate, profile.race_length, DATA_PROVA);
    if (!avaliacao.aceita && avaliacao.erro) {
      this.recusa.set(MENSAGEM_REGULAMENTO[avaliacao.erro]);
      return;
    }

    if (this.localStorageService.add(profile)) {
      this.form.reset();
    } else {
      this.recusa.set(this.localStorageService.error());

    }
  }
}
