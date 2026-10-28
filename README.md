# Corrida Solidária da Vila Esperança — Inscrições

Aplicação Angular para cadastrar, listar, consultar, editar e cancelar inscrições da 5ª Corrida Solidária, com resumo do total de inscritos e da quantidade por percurso.

## Como rodar

**Pré-requisitos:** Node 22 e npm (ambiente de referência do enunciado).

```bash
cd prova-fabrica
npm ci          # instala as dependências exatas do package-lock.json
npm start       # sobe o servidor de desenvolvimento (ng serve)
```

Abra `http://localhost:4200`. O endereço `/` redireciona para `/inscricoes`.

Outros comandos:

```bash
npm test        # testes (Vitest)
npm run build   # build de produção em dist/
```

Os dados ficam no `localStorage` do navegador, na chave `corrida.inscricoes.v1`. A aplicação começa com a lista vazia. Para zerar os dados: DevTools (F12) → Application → Local Storage → apagar a chave.

## Entendimento do problema

A associação organiza a corrida e precisa substituir o caderno e a planilha por um sistema simples de inscrições.
Cada inscrição tem nome, e-mail, data de nascimento e percurso (5 km ou 10 km); o e-mail identifica a pessoa e só pode ser usado uma vez.
O sistema calcula a idade na data da prova e a categoria, recusando quem não cumpre o regulamento (mínimo de 14 anos; 10 km só a partir de 18).
A organização precisa ver o total de inscritos e por percurso, para encomendar medalhas e água.
Também precisa corrigir nome, nascimento e percurso sem cancelar a inscrição, e abrir uma inscrição específica por link.

## Regras e suas fontes

O regulamento oficial (Peça 2 do pacote do cliente) prevalece sobre o áudio e as mensagens.

| Regra | Fonte |
|---|---|
| Inscrição individual com nome completo, e-mail, data de nascimento e percurso | Regulamento, Art. 4º, I |
| O e-mail identifica a inscrição; não há duas inscrições com o mesmo e-mail | Regulamento, Art. 4º, II |
| Inscrição gratuita, mediante doação de 1 kg de alimento na retirada do kit | Regulamento, Art. 4º, III |
| Prova em 14/03/2027 | Regulamento, Art. 5º, I |
| Percursos de 5 km e 10 km | Regulamento, Art. 5º, II |
| Vale a idade completa na data da prova (aniversário no próprio dia já conta) | Regulamento, Art. 6º, I; enunciado |
| Idade mínima de 14 anos | Regulamento, Art. 6º, II |
| 10 km só para 18 anos ou mais | Regulamento, Art. 6º, III |
| Idade mínima é verificada antes da restrição dos 10 km | Enunciado |
| Categorias: Juvenil 14–17, Adulto 18–39, Master 40–59, Sênior 60+ | Regulamento, Art. 6º, IV |
| Cancelamento pelo e-mail, a qualquer momento antes da prova; o e-mail fica livre para nova inscrição | Regulamento, Art. 7º |
| Edição de nome, nascimento e percurso; e-mail imutável (para trocar, cancela e inscreve de novo) | Atualização do pedido (19/10/2026) |
| Nome: 3 a 100 caracteres após remover espaços nas pontas e reduzir espaços internos repetidos; aceita acentos, apóstrofos e hífens | Enunciado |
| E-mail: espaços das pontas removidos e convertido para minúsculas antes de validar e comparar | Enunciado |
| Nascimento: data válida anterior a hoje; gravada como aaaa-mm-dd e exibida como dd/mm/aaaa | Enunciado |
| Idade e categoria são sempre calculadas, nunca digitadas nem gravadas | Enunciado |
| Resumo (total, 5 km, 10 km) sempre derivado da lista, inclusive quando zerado | Enunciado |

## Conflitos e decisões

**1. Data da prova: dia 13 ou 14 de março?**
O áudio do Seu Jorge diz "domingo, dia 13 de março". O regulamento (Art. 5º, I) diz "domingo, 14 de março de 2027".
**Decisão:** 14/03/2027. O regulamento prevalece, e 13/03/2027 cai num sábado, enquanto 14/03/2027 é domingo, o que confirma o regulamento. A data está definida num único lugar (`DATA_PROVA` em `src/app/dominio/inscricao.regras.ts`).

**2. Categoria pelo ano de nascimento (sugestão do Valdir)**
O Valdir sugere que "nasceu em 87, em 2027 é master".
**Decisão:** a categoria segue a idade completa na data da prova (Art. 6º, I). Exemplo: quem nasceu em 15/03/1987 tem 39 anos em 14/03/2027 e é Adulto, não Master.

**3. Dona Marta inscrevendo três pessoas num só e-mail**
Ela quer inscrever a si mesma e os filhos Pedro (15) e Júlia (12) com `marta.lima@email.com`.
**Decisão:** não é permitido. A inscrição é individual e cada e-mail identifica uma única inscrição (Art. 4º, I e II). Além disso, a Júlia não atinge a idade mínima de 14 anos (Art. 6º, II) e seria recusada de qualquer forma. O Pedro pode se inscrever com um e-mail próprio, como Juvenil e apenas nos 5 km.

**4. `joao.silva@gmail.com` e `joaosilva@gmail.com` são a mesma pessoa?**
O Gmail ignora pontos no nome do usuário, então os dois endereços chegam à mesma caixa.
**Decisão:** são tratados como e-mails diferentes. A normalização exigida é só remover espaços das pontas e converter para minúsculas. Remover pontos é uma regra específica do Gmail; aplicá-la a todos os provedores juntaria endereços que, em outros provedores, pertencem a pessoas diferentes.

**5. Pix da inscrição**
O áudio pede para "gerar o Pix da inscrição", mas o regulamento diz que a inscrição é gratuita, mediante doação de alimento (Art. 4º, III).
**Decisão:** não há cobrança no sistema. Ver "Fora de escopo".

## Perguntas ao cliente

1. A data da prova é mesmo 14/03/2027 (domingo), como no regulamento? O áudio menciona dia 13.
2. Como inscrever menores que ainda não têm e-mail próprio, como os filhos da Dona Marta?
3. Inscrições de menores precisam de autorização dos responsáveis? Se sim, como ela deve ser registrada?
4. Podemos considerar `joao.silva@gmail.com` e `joaosilva@gmail.com` como inscrições diferentes?
5. Existe limite de vagas por percurso?
6. A doação de 1 kg de alimento precisa ser registrada no sistema, ou é controlada só na retirada do kit?
7. O sistema deve impedir inscrições ou cancelamentos depois de alguma data (por exemplo, a partir do dia da prova)?

## Fora de escopo

| Pedido | Motivo |
|---|---|
| Gerar Pix da inscrição | A inscrição é gratuita (Art. 4º, III); pagamento está fora do escopo do teste |
| E-mail de confirmação | Fora do escopo definido no pacote do cliente |
| Login | Fora do escopo definido no pacote do cliente |
| Autorização de responsáveis para menores | Fora do escopo definido no pacote do cliente |
| Bloqueio temporal de inscrições e cancelamentos | Fora do escopo definido no pacote do cliente |
| Backend e banco de dados | O front-end é independente do teste de backend; os dados ficam no `localStorage` |
| Sincronização entre abas do navegador | Não é obrigatória, conforme o enunciado |
| Mais de uma inscrição por e-mail | Contraria o Art. 4º, II |

## Verificações realizadas

- **Regras de idade nas bordas** (`avaliarInscricao`, prova em 14/03/2027):

  | Nascimento | Percurso | Resultado |
  |---|---|---|
  | 14/03/2013 | 5 km | 14 anos, Juvenil, aceita |
  | 15/03/2013 | 5 km | 13 anos, recusada (`IDADE_MINIMA`) |
  | 01/01/2015 | 10 km | 12 anos, recusada por `IDADE_MINIMA` (não pela distância) |
  | 14/03/2009 | 10 km | 18 anos no dia, Adulto, aceita |
  | 15/03/2009 | 10 km | 17 anos, recusada (`DISTANCIA_NAO_PERMITIDA`) |
  | 15/03/2009 | 5 km | 17 anos, Juvenil, aceita |
  | 15/03/1987 / 14/03/1987 | 5 km | 39 → Adulto / 40 → Master |
  | 15/03/1967 / 14/03/1967 | 10 km | 59 → Master / 60 → Sênior |

- **Validações de formato:** nome com 2, 3, 100 e 101 caracteres; nome com espaços repetidos; nome com acento, apóstrofo e hífen; e-mail com espaços e maiúsculas; e-mail sem `@` e sem domínio; data inexistente (30/02); data de hoje e de amanhã; 29/02 em ano bissexto; percurso vazio.
- **E-mail na URL:** `ana.souza+corrida@email.com` gera `/inscricoes/ana.souza%2Bcorrida@email.com` e é lido de volta sem perder o `+` nem o `@`, inclusive quando colado na barra com o `+` sem codificação.
- **Compilação:** sem erros de TypeScript nem de template (modo `strictTemplates`).
- **Layout:** sem rolagem horizontal em 375 px (celular), 768 px (tablet) e 1366 px (desktop), com e sem o formulário aberto.
