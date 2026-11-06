# Corrida Solidária — API de Inscrições

API REST para cadastrar, listar, consultar, editar e cancelar inscrições da **5ª Corrida Solidária da Vila Esperança**, com o resumo do total de inscritos e da quantidade por percurso. Idade na prova e categoria são sempre calculadas pelo sistema, conforme o regulamento oficial.

## Requisitos

- **JDK 21**
- Não é necessário instalar o Maven: o projeto usa o Maven Wrapper (`mvnw`).

## Como executar

Na pasta `backend`:

```bash
# Linux, macOS ou Git Bash
./mvnw spring-boot:run

# Windows (PowerShell)
.\mvnw spring-boot:run
```

A API sobe em **http://localhost:8080**. Os dados ficam em memória: a aplicação começa com a lista vazia, e as inscrições são perdidas ao reiniciar.

## Como rodar os testes

```bash
./mvnw test
```

### Observação sobre os testes:
Os testes automatizados não foram realizados, foram realizados **apenas** testes manualmente pela ferramenta Postman.

## Contrato da API

Prefixo `/api`. Corpos e respostas em JSON.

| Operação  | Rota                              | Corpo                                          | Sucesso                 | Recusas  |
|-----------|-----------------------------------|------------------------------------------------|-------------------------|----------|
| Cadastrar | `POST /api/inscricoes`            | `nome`, `email`, `dataNascimento`, `percurso`  | `201` + inscrição       | 400, 409 |
| Listar    | `GET /api/inscricoes`             | —                                              | `200`, ordem de cadastro| —        |
| Consultar | `GET /api/inscricoes/{email}`     | —                                              | `200` + inscrição       | 404      |
| Editar    | `PUT /api/inscricoes/{email}`     | `nome`, `dataNascimento`, `percurso`           | `200` + inscrição       | 400, 404 |
| Cancelar  | `DELETE /api/inscricoes/{email}`  | —                                              | `204`, sem corpo        | 404      |
| Resumo    | `GET /api/inscricoes/resumo`      | —                                              | `200`                   | —        |

**Inscrição**

```json
{
  "nome": "Ana Souza",
  "email": "ana.souza@email.com",
  "dataNascimento": "1990-05-20",
  "percurso": "KM_10",
  "idadeNaProva": 36,
  "categoria": "ADULTO"
}
```

**Resumo**

```json
{ "total": 3, "km5": 2, "km10": 1 }
```

**Recusa** (toda resposta de erro)

```json
{ "mensagem": "A idade mínima para participar é: 14 anos." }
```

| Status | Quando |
|--------|--------|
| 400 | Campo ausente; nome ou e-mail fora do formato; data inexistente, fora do formato `aaaa-mm-dd` ou não anterior a hoje; percurso diferente de `KM_5`/`KM_10`; JSON malformado; menos de 14 anos na data da prova; 10 km com menos de 18 anos |
| 404 | Não existe inscrição ativa para o e-mail informado |
| 409 | O e-mail já tem inscrição ativa (somente no cadastro) |

### Exemplo rápido

```bash
curl -X POST http://localhost:8080/api/inscricoes \
  -H "Content-Type: application/json" \
  -d '{"nome":"Ana Souza","email":"ana.souza@email.com","dataNascimento":"1990-05-20","percurso":"KM_10"}'

curl http://localhost:8080/api/inscricoes
curl http://localhost:8080/api/inscricoes/resumo
```

No PowerShell, use `curl.exe` em vez de `curl`.

## Regras implementadas e fontes

| Regra | Fonte |
|-------|-------|
| Data da prova: **14/03/2027**, definida uma única vez em `SubscriptionRules.RACE_DATE` | Regulamento, Art. 5º I |
| Idade **completa na data da prova** (`Period.between`), não a idade de hoje nem a diferença entre os anos. Aniversário no dia da prova já conta | Regulamento, Art. 6º I; enunciado |
| Idade mínima de 14 anos, verificada **antes** da restrição de percurso | Regulamento, Art. 6º II; enunciado |
| 10 km somente a partir de 18 anos | Regulamento, Art. 6º III |
| Categorias: Juvenil 14–17, Adulto 18–39, Master 40–59, Sênior 60+ | Regulamento, Art. 6º IV |
| Um e-mail, uma inscrição ativa; o e-mail identifica a inscrição | Regulamento, Art. 4º II |
| E-mail normalizado (espaços nas pontas removidos e minúsculas): `" Ana.Souza@Email.COM "` e `"ana.souza@email.com"` são a mesma inscrição | Enunciado |
| Nome com 3 a 100 caracteres após remover espaços nas pontas e reduzir espaços repetidos; aceita acentos, apóstrofos e hífens | Enunciado |
| Edição altera nome, nascimento e percurso; o e-mail é imutável | Pacote do cliente (atualização de 19/10); enunciado |
| Cancelamento libera o e-mail para nova inscrição | Regulamento, Art. 7º II |

## Conflitos e decisões

| Situação | Decisão | Motivo |
|----------|---------|--------|
| O áudio diz "domingo, dia 13 de março"; o regulamento diz 14/03/2027 | 14/03/2027 | O regulamento prevalece sobre áudio e mensagens |
| O tesoureiro sugere categoria pelo **ano** de nascimento | Não adotado: vale a idade completa na data da prova | Art. 6º I; o enunciado proíbe usar só a diferença entre os anos |
| Dona Marta quer inscrever a si e aos dois filhos no mesmo e-mail | A segunda inscrição com o mesmo e-mail é recusada (409); a filha de 12 anos é recusada pela idade (400) | Art. 4º I (inscrição individual), Art. 4º II e Art. 6º II |
| `joao.silva@gmail.com` × `joaosilva@gmail.com` | São inscrições diferentes | A normalização definida no enunciado é apenas espaços e caixa; regras específicas de provedores não fazem parte do escopo |
| Trocar o e-mail de uma inscrição | Não é permitido na edição: cancelar e inscrever de novo | Pedido do cliente (atualização de 19/10) |
| Ordem das validações no cadastro | Dados e regulamento primeiro, duplicidade depois | Um pedido inválido recebe 400; o 409 indica um pedido válido que conflita com uma inscrição existente |
| `PUT` para e-mail inexistente com corpo inválido | Responde 400 | A validação do corpo (`@Valid`) ocorre antes da busca pela inscrição |
| Nome com números | Recusado | O enunciado lista acentos, apóstrofos e hífens como caracteres especiais aceitos |
| Formato do e-mail | Exige pelo menos 2 caracteres antes e depois do `@` e terminação com letras (ex.: `.com`, `.com.br`) | Barra erros de digitação comuns, como `ana@email` |
| Armazenamento | Em memória (`LinkedHashMap`, com o e-mail normalizado como chave) | Permitido pelo enunciado; mantém a ordem de cadastro, a posição na edição e a unicidade do e-mail |
| Resumo | Calculado a partir da lista a cada consulta, sem contadores separados | Mantém os totais sempre coerentes com a lista, inclusive após edições e quando zerados |
| Versão do framework | Spring Boot 4 | O enunciado indica Spring Boot 3 como preferência, não como obrigação |
| Idioma do código | Classes e campos em inglês; nomes do JSON em português via `@JsonProperty`; valores do contrato (`KM_5`, `JUVENIL`...) preservados | Consistência no código e fidelidade ao contrato |

## Fora do escopo

Não implementados por não fazerem parte do enunciado:

- Pagamento e geração de Pix (a inscrição é gratuita, mediante doação de 1 kg de alimento — Art. 4º III)
- Envio de e-mail de confirmação
- Login e autorização
- Autorização de responsáveis para menores
- Bloqueio de inscrições ou cancelamentos por data
- Persistência após reiniciar a aplicação
- Integração com o front-end

## Estrutura do projeto

```
src/main/java/com/example/backend/
├── controller/   SubscriptionsController — rotas HTTP
├── service/      SubscriptionsService — fluxo de cada operação e validações
├── domain/       SubscriptionRules — regulamento e normalização (Java puro, sem Spring)
│                 Subscriptions, Length, Categories, SubscriptionEval, RegulamentException
├── repository/   SubscriptionsRepository — armazenamento em memória
├── dto/          formatos de entrada e saída do JSON
└── exception/    exceções de negócio e GlobalExceptionHandler (@RestControllerAdvice)
```

## Tecnologias

- Java 21
- Spring Boot 4 (Web MVC e Validation)
- Maven (via Maven Wrapper)

### Observação sobre tecnologias:
Foi mencionado o uso **preferencial** do Spring Boot 3, após uma pesquisa a conclusão chegada foi que o Spring Boot 3 e 4 possuem poucas diferenças e que não haveria impacto direto no projeto.
