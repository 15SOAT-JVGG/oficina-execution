# Oficina Execution

Serviço Quarkus para consulta e ajuste de estoque. Consulte [contratos](docs/architecture.md), [engenharia](docs/engineering.md), [harness](AGENTS.md) e [contexto](CONTEXT.md).

## Executar localmente

Instale Docker e Compose. Copie a chave pública RS256 do OS para `.local-jwt/publicKey.pem` (nunca a chave privada), então execute `docker compose up --build`. LocalStack inicializa a tabela e persiste dados em volume. API em `localhost:8080`, Swagger em `/q/swagger-ui`, OpenAPI em `/q/openapi` e health em `/q/health`. O issuer padrão é `oficina-api`.

Para testes: inicie `docker compose up -d localstack`, configure `AWS_ACCESS_KEY_ID=local AWS_SECRET_ACCESS_KEY=local AWS_REGION=us-east-1 DYNAMODB_ENDPOINT=http://localhost:4566` e execute `mvn verify`. Os testes geram chaves temporárias. A aplicação na AWS usa cadeia padrão de credenciais AWS e requer `DYNAMODB_TABLE`, `AWS_REGION`, `JWT_PUBLIC_KEY_LOCATION` e `JWT_ISSUER`; `DYNAMODB_ENDPOINT` fica vazio. Infraestrutura da tabela: `oficina-infra-dynamo`.

SonarCloud exige projeto, variável `SONAR_PROJECT_KEY` e secret `SONAR_TOKEN` no GitHub; configuração de branch protection exige permissão administrativa. Sem essas configurações o check não é considerado aprovado.
