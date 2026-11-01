package br.com.fiap.execution;

import static org.junit.jupiter.api.Assertions.*;

import br.com.fiap.execution.application.StockPort;
import br.com.fiap.execution.application.StockService;
import br.com.fiap.execution.domain.Stock;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class StockServiceTest {
  @Test
  void validatesAdjustmentBeforeCallingPersistence() {
    StockPort port =
        new StockPort() {
          public Optional<Stock> get(UUID id) {
            return Optional.empty();
          }

          public Stock adjust(UUID id, long amount) {
            return new Stock(id, amount);
          }
        };
    var service = new StockService(port);
    var id = UUID.randomUUID();
    assertThrows(IllegalArgumentException.class, () -> service.adjust(id, 0));
    assertThrows(IllegalArgumentException.class, () -> service.adjust(id, Long.MIN_VALUE));
    assertEquals(new Stock(id, 4), service.adjust(id, 4));
    assertTrue(service.get(id).isEmpty());
  }
}
