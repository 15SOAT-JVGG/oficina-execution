package br.com.fiap.execution.adapter.out;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.regions.Region;
import java.net.URI;
@ApplicationScoped public class DynamoConfig {
 @Produces @ApplicationScoped DynamoDbClient client(@ConfigProperty(name="execution.dynamo.region") String region, @ConfigProperty(name="execution.dynamo.endpoint") String endpoint) {
  var builder=DynamoDbClient.builder().region(Region.of(region));
  if(!endpoint.isBlank()) builder.endpointOverride(URI.create(endpoint));
  return builder.build();
 }
}
