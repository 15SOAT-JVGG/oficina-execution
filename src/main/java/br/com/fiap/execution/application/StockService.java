package br.com.fiap.execution.application;
import br.com.fiap.execution.domain.Stock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;
import java.util.UUID;
@ApplicationScoped public class StockService {
 private final StockPort port;
 @Inject public StockService(StockPort port) { this.port=port; }
 public Optional<Stock> get(UUID id) { return port.get(id); }
 public Stock adjust(UUID id, long amount) { if(amount==0 || amount==Long.MIN_VALUE) throw new IllegalArgumentException("Adjustment must be nonzero and within range"); return port.adjust(id,amount); }
}
