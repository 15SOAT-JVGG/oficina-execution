package br.com.fiap.execution.adapter.in;

import br.com.fiap.execution.application.StockService;
import br.com.fiap.execution.domain.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.UUID;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/admin/parts/{id}/stock")
@Produces(MediaType.APPLICATION_JSON)
public class StockResource {
  @Inject StockService service;

  @GET
  @RolesAllowed({"ADMIN", "MECHANIC"})
  @Operation(summary = "Consultar saldo da peça")
  public Response get(@PathParam("id") UUID id) {
    return service
        .get(id)
        .map(s -> Response.ok(s).build())
        .orElseGet(
            () ->
                Response.status(404)
                    .entity(new ErrorResponse("STOCK_NOT_FOUND", "Stock not found"))
                    .build());
  }

  @PATCH
  @RolesAllowed("ADMIN")
  @Operation(summary = "Ajustar saldo: positivo para entrada, negativo para saída")
  public Stock adjust(@PathParam("id") UUID id, @QueryParam("adjustment") String adjustment) {
    try {
      if (adjustment == null) throw new IllegalArgumentException();
      return service.adjust(id, Long.parseLong(adjustment));
    } catch (NumberFormatException e) {
      throw new BadRequestException("Invalid adjustment");
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Adjustment must be nonzero and within range");
    }
  }

  public record ErrorResponse(String code, String message) {}
}
