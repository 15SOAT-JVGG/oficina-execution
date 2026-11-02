# Engenharia

Não confiar no consumidor para impor saldo não negativo: a condição pertence à escrita DynamoDB. Não simular integração com armazenamento em memória. Testar concorrência, falhas e persistência no LocalStack; isolar dados por UUID. Cobertura mínima 80% de linhas incluindo IT, com SonarCloud bloqueante. Validar issuer e assinatura RS256 de tokens; chave pública é fornecida em execução. Jamais versionar chave privada. Prefira testes de comportamento e contratos arquiteturais executáveis.
