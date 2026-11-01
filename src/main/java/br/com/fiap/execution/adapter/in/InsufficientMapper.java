package br.com.fiap.execution.adapter.in;
import br.com.fiap.execution.domain.InsufficientStock;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;
@Provider public class InsufficientMapper implements ExceptionMapper<InsufficientStock> { public Response toResponse(InsufficientStock e) { return Response.status(400).entity(new StockResource.ErrorResponse("INSUFFICIENT_STOCK",e.getMessage())).build(); } }
