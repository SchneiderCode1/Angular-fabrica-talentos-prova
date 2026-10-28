export type Distancia = 'KM_5' | 'KM_10';
export type Categoria = 'JUVENIL' | 'ADULTO' | 'MASTER' | 'SENIOR';
export type ErroRegulamento = 'IDADE_MINIMA' | 'DISTANCIA_NAO_PERMITIDA';

export interface AvaliacaoInscricao {
  aceita: boolean;
  idadeNaProva: number;
  categoria: Categoria | null;
  erro: ErroRegulamento | null;
}

export const DATA_PROVA = '2027-03-14';

const IDADE_MINIMA = 14;
const IDADE_MINIMA_10KM = 18;

export const ROTULO_CATEGORIA: Record<Categoria, string> = {
  JUVENIL: 'Juvenil',
  ADULTO: 'Adulto',
  MASTER: 'Master',
  SENIOR: 'Sênior',
};

export function normalizarEmail(email: string): string {
  return email.trim().toLowerCase();
}

export function calcularIdade(nascimento: string, referencia: string): number {
  const [anoNasc, mesNasc, diaNasc] = nascimento.split('-').map(Number);
  const [anoRef, mesRef, diaRef] = referencia.split('-').map(Number);

  let idade = anoRef - anoNasc;
  const aniversarioAindaNaoChegou = mesRef < mesNasc || (mesRef === mesNasc && diaRef < diaNasc);
  if (aniversarioAindaNaoChegou) idade--;
  return idade;
}

export function verifyName(name: string):boolean{
    if (name.length >= 3 && name.length <= 100) {
        return true
    } 
    return false
    
}

function categoriaPorIdade(idade: number): Categoria {
  if (idade >= 60) return 'SENIOR';
  if (idade >= 40) return 'MASTER';
  if (idade >= 18) return 'ADULTO';
  return 'JUVENIL';
}

export function avaliarInscricao(
  nascimento: string,
  distancia: Distancia,
  dataProva: string,
): AvaliacaoInscricao {
  const idadeNaProva = calcularIdade(nascimento, dataProva);

  if (idadeNaProva < IDADE_MINIMA) {
    return { aceita: false, idadeNaProva, categoria: null, erro: 'IDADE_MINIMA' };
  }
  if (distancia === 'KM_10' && idadeNaProva < IDADE_MINIMA_10KM) {
    return { aceita: false, idadeNaProva, categoria: null, erro: 'DISTANCIA_NAO_PERMITIDA' };
  }
  return { aceita: true, idadeNaProva, categoria: categoriaPorIdade(idadeNaProva), erro: null };
}
