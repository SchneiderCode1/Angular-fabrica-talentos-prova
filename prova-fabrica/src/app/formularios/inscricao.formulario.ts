import { AbstractControl, FormBuilder, ValidatorFn } from '@angular/forms';
import {
  validarEmail,
  validarNascimento,
  validarNome,
  validarPercurso,
} from '../dominio/inscricao.validacao';

function validador(regra: (valor: string) => string | null): ValidatorFn {
  return (controle: AbstractControl) => {
    const mensagem = regra(controle.value ?? '');
    return mensagem ? { mensagem } : null;
  };
}

export function criarFormularioInscricao(fb: FormBuilder) {
  return fb.nonNullable.group({
    username: ['', validador(validarNome)],
    email: ['', validador(validarEmail)],
    birthdate: ['', validador((valor) => validarNascimento(valor))],
    race_length: ['', validador(validarPercurso)],
  });
}

export function mensagemDoCampo(controle: AbstractControl): string | null {
  return controle.touched ? (controle.errors?.['mensagem'] ?? null) : null;
}
