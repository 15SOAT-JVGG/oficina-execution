# Contexto

Execution é dono do saldo de estoque por identificador UUID de peça. Billing é dono do catálogo e preços; OS emite o JWT da equipe. Não consultamos o banco de outro serviço. A primeira entrada aceita um partId desconhecido, pois eventos do catálogo ainda não existem. O estoque nunca pode ficar negativo. Reserva, fila e saga são futuras.
