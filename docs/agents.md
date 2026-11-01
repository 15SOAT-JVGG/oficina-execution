# Fluxo de trabalho dos agentes

1. Leia a issue, o contexto e os contratos existentes.
2. Descreva invariantes e dependências externas antes de alterar código.
3. Implemente uma fatia de cada vez preservando limites hexagonais.
4. Teste comportamento e erros com LocalStack, autenticação e concorrência.
5. Execute `mvn verify`, revise segurança, cobertura e diff; não desative gates.
6. Informe comandos, resultados, limitações e decisões para revisão.
