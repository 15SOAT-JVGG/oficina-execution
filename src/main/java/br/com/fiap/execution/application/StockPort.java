package br.com.fiap.execution.application;
import br.com.fiap.execution.domain.Stock;
import java.util.Optional;
import java.util.UUID;
public interface StockPort { Optional<Stock> get(UUID id); Stock adjust(UUID id, long amount); }
