# Instruções para agentes

Leia CONTEXT.md, docs/architecture.md e docs/engineering.md e docs/agents.md antes de alterar o serviço. Domínio e aplicação não dependem de frameworks ou adaptadores; I/O passa por portas. Preserve o contrato HTTP documentado, a validação RS256 e a atomicidade do estoque. Não coloque chaves privadas ou credenciais no repositório. Execute testes unitários, integração LocalStack e `mvn verify`; não contorne gates. Documente decisões e limitações. Não implemente reservas nem mensageria sem issue específica.
