---
name: write-dynamodb-it
description: Testar persistência e concorrência contra DynamoDB do LocalStack.
---
Use tabela LocalStack real e UUIDs únicos; cubra leitura consistente, escrita condicional, falhas e concorrência. Não substitua o adaptador por mapa em memória. Execute os IT juntamente com testes unitários e gate JaCoCo. Nunca use credenciais reais nos testes.
