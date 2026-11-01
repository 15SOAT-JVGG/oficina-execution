package br.com.fiap.execution;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.nio.file.*;
import java.security.*;
import java.util.*;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import java.net.URI;
public class TestResources implements QuarkusTestResourceLifecycleManager {
 static PrivateKey privateKey;
 public Map<String,String> start() {
  try {
   var generator=KeyPairGenerator.getInstance("RSA"); generator.initialize(2048); var pair=generator.generateKeyPair(); privateKey=pair.getPrivate();
   Path publicKey=Files.createTempFile("execution-public-", ".pem"); Files.writeString(publicKey,"-----BEGIN PUBLIC KEY-----\n"+Base64.getMimeEncoder(64,new byte[]{'\n'}).encodeToString(pair.getPublic().getEncoded())+"\n-----END PUBLIC KEY-----\n"); publicKey.toFile().deleteOnExit();
   String endpoint=System.getenv().getOrDefault("DYNAMODB_ENDPOINT","http://localhost:4566");
   try(var db=DynamoDbClient.builder().endpointOverride(URI.create(endpoint)).region(Region.US_EAST_1).credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("local","local"))).build()) {
    try { db.createTable(r->r.tableName("execution-test").attributeDefinitions(a->a.attributeName("partId").attributeType(ScalarAttributeType.S)).keySchema(k->k.attributeName("partId").keyType(KeyType.HASH)).billingMode(BillingMode.PAY_PER_REQUEST)); } catch(ResourceInUseException ignored) {}
    db.waiter().waitUntilTableExists(r->r.tableName("execution-test"));
   }
   return Map.of("mp.jwt.verify.publickey.location",publicKey.toUri().toString(),"execution.dynamo.endpoint",endpoint,"execution.dynamo.table","execution-test");
  } catch(Exception e) { throw new RuntimeException(e); }
 }
 public void stop() {}
}
