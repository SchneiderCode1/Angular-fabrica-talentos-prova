import { normalizarEmail } from './inscricao.regras';

const NOME_MINIMO = 3;
const NOME_MAXIMO = 100;
const FORMATO_EMAIL = /^[a-zA-Z0-9._%+-]{2,}@[a-zA-Z0-9-]{2,}(?:\.[a-zA-Z]{2,})+$/;
const FORMATO_DATA = /^\d{4}-\d{2}-\d{2}$/;

export function normalizarNome(nome: string): string {
  return nome.trim().replace(/\s+/g, ' ');
}

export function validarNome(nome: string): string | null {
  const tamanho = normalizarNome(nome).length;
  if (tamanho === 0) return 'Informe o nome completo.';
  if (tamanho < NOME_MINIMO) return `O nome deve ter pelo menos ${NOME_MINIMO} caracteres.`;
  if (tamanho > NOME_MAXIMO) return `O nome deve ter no máximo ${NOME_MAXIMO} caracteres.`;
  return null;
}

export function validarEmail(email: string): string | null {
  const normalizado = normalizarEmail(email);
  if (!normalizado) return 'Informe o e-mail.';
  if (!FORMATO_EMAIL.test(normalizado)) return 'Informe um e-mail válido, como nome@exemplo.com.';
  return null;
}

export function validarNascimento(nascimento: string, hoje: string = hojeIso()): string | null {
  if (!nascimento) return 'Informe a data de nascimento.';
  if (!FORMATO_DATA.test(nascimento)) return 'Data inválida.';

  const [ano, mes, dia] = nascimento.split('-').map(Number);
  const data = new Date(Date.UTC(ano, mes - 1, dia));
  const existe =
    data.getUTCFullYear() === ano && data.getUTCMonth() === mes - 1 && data.getUTCDate() === dia;
  if (!existe) return 'Data inválida.';

  if (nascimento >= hoje) return 'A data de nascimento deve ser anterior a hoje.';
  return null;
}

export function validarPercurso(percurso: string): string | null {
  return percurso === 'KM_5' || percurso === 'KM_10' ? null : 'Selecione o percurso.';
}

export function hojeIso(): string {
  const agora = new Date();
  const mes = String(agora.getMonth() + 1).padStart(2, '0');
  const dia = String(agora.getDate()).padStart(2, '0');
  return `${agora.getFullYear()}-${mes}-${dia}`;
}
