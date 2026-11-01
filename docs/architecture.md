# Arquitetura e contratos

A API é adaptador de entrada; serviço de aplicação usa porta de estoque; DynamoDB é adaptador de saída. O domínio não depende de Quarkus ou AWS. Um item por UUID tem `partId` (chave string) e `balance` (inteiro). Atualizações condicionais atômicas impedem saldo negativo e perda de atualizações concorrentes. Consultas são consistentes.

`PATCH /admin/parts/{id}/stock?adjustment=N`: ADMIN; N inteiro não nulo, positivo entrada, negativo saída; 200 `{ "partId": "UUID", "balance": 1 }`, 400 ajuste inválido ou saldo insuficiente. Primeiro ajuste positivo cria saldo; saída de peça desconhecida falha. A resposta difere da peça completa do monólito. `GET /admin/parts/{id}/stock`: ADMIN ou MECHANIC, 200 com mesmo corpo ou 404 se ainda não houve entrada. JWT inválido: 401; papel proibido: 403. O roteamento público e clientes serão adaptados em outra issue.
