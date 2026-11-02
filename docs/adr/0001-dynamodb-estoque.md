# ADR 0001 — DynamoDB e atualização condicional

Aceito. O Execution mantém um item por peça, com saldo numérico inteiro e partId UUID como chave. `UpdateItem` condicional faz incremento ou decremento atômico, impedindo saldo negativo e sobrescrita concorrente. Leitura consistente apresenta o saldo persistido. Desconhecidos podem receber primeira entrada; saída desconhecida é recusada. Eventos e reservas são decisões de issues posteriores.
