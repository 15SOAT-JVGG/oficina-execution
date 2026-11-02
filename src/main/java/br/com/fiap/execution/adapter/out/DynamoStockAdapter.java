package br.com.fiap.execution.adapter.out;

import br.com.fiap.execution.application.StockPort;
import br.com.fiap.execution.domain.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@ApplicationScoped
public class DynamoStockAdapter implements StockPort {
  private final DynamoDbClient db;
  private final String table;

  @Inject
  public DynamoStockAdapter(
      DynamoDbClient db, @ConfigProperty(name = "execution.dynamo.table") String table) {
    this.db = db;
    this.table = table;
  }

  private Map<String, AttributeValue> key(UUID id) {
    return Map.of("partId", AttributeValue.fromS(id.toString()));
  }

  public Optional<Stock> get(UUID id) {
    var item = db.getItem(r -> r.tableName(table).key(key(id)).consistentRead(true)).item();
    return item == null || item.isEmpty()
        ? Optional.empty()
        : Optional.of(new Stock(id, Long.parseLong(item.get("balance").n())));
  }

  public Stock adjust(UUID id, long amount) {
    var values = new HashMap<String, AttributeValue>();
    values.put(":delta", AttributeValue.fromN(Long.toString(amount)));
    values.put(":zero", AttributeValue.fromN("0"));
    values.put(":max", AttributeValue.fromN(Long.toString(Long.MAX_VALUE - Math.max(amount, 0))));
    String condition =
        amount < 0
            ? "attribute_exists(partId) AND balance >= :minimum"
            : "attribute_not_exists(balance) OR balance <= :max";
    if (amount < 0) {
      values.put(":minimum", AttributeValue.fromN(Long.toString(-amount)));
      values.remove(":max");
    }
    try {
      var result =
          db.updateItem(
              r ->
                  r.tableName(table)
                      .key(key(id))
                      .updateExpression("SET balance = if_not_exists(balance, :zero) + :delta")
                      .conditionExpression(condition)
                      .expressionAttributeValues(values)
                      .returnValues(ReturnValue.ALL_NEW));
      return new Stock(id, Long.parseLong(result.attributes().get("balance").n()));
    } catch (ConditionalCheckFailedException e) {
      throw new InsufficientStock();
    }
  }
}
