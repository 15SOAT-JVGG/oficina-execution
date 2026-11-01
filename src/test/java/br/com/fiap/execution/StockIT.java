package br.com.fiap.execution;

import br.com.fiap.execution.adapter.out.DynamoStockAdapter;
import br.com.fiap.execution.domain.InsufficientStock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.concurrent.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest @QuarkusTestResource(TestResources.class)
class StockIT {
 @Inject DynamoStockAdapter adapter;
 private String token(String role) { return Jwt.issuer("oficina-api").upn("user").groups(Set.of(role)).sign(TestResources.privateKey); }
 @Test void httpSecurityAndStock() {
  String id=UUID.randomUUID().toString(), admin=token("ADMIN"), mechanic=token("MECHANIC");
  given().get("/admin/parts/"+id+"/stock").then().statusCode(401);
  given().auth().oauth2(mechanic).get("/admin/parts/"+id+"/stock").then().statusCode(404);
  given().auth().oauth2(mechanic).queryParam("adjustment",1).patch("/admin/parts/"+id+"/stock").then().statusCode(403);
  given().auth().oauth2(admin).patch("/admin/parts/"+id+"/stock").then().statusCode(400);
  given().auth().oauth2(admin).queryParam("adjustment",0).patch("/admin/parts/"+id+"/stock").then().statusCode(400);
  given().auth().oauth2(admin).queryParam("adjustment",-1).patch("/admin/parts/"+id+"/stock").then().statusCode(400);
  given().auth().oauth2(admin).queryParam("adjustment",10).patch("/admin/parts/"+id+"/stock").then().statusCode(200).body("balance",equalTo(10));
  given().auth().oauth2(admin).queryParam("adjustment",-3).patch("/admin/parts/"+id+"/stock").then().statusCode(200).body("balance",equalTo(7));
  given().auth().oauth2(mechanic).get("/admin/parts/"+id+"/stock").then().statusCode(200).body("balance",equalTo(7));
  given().auth().oauth2("invalid").get("/admin/parts/"+id+"/stock").then().statusCode(401);
 }
 @Test void concurrentUpdatesNeverGoNegative() throws Exception {
  UUID id=UUID.randomUUID(); adapter.adjust(id,1);
  try(var pool=Executors.newFixedThreadPool(2)) {
   var tasks=List.<Callable<Boolean>>of(()->take(id),()->take(id));
   var results=pool.invokeAll(tasks); assertNotEquals(results.get(0).get(),results.get(1).get());
  }
  assertEquals(0,adapter.get(id).orElseThrow().balance());
 }
 boolean take(UUID id) { try {adapter.adjust(id,-1); return true;} catch(InsufficientStock e){return false;} }
}
