package roberto.magale.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.google.inject.Inject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import roberto.magale.api.exceptions.TransactionNotFoundException;
import roberto.magale.api.model.Transaction;
import roberto.magale.service.TransactionService;

import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/transaction")
@Api(value = "/transaction", description = "Operations on transaction")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class TransactionResource {

    private final TransactionService transactionService;

    @Inject
    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @POST
    @ApiOperation(
            value = "Creates a new account",
            response = Transaction.class, httpMethod = "POST",
            notes = "This will affect also relevant accounts as the transaction amount will be moved from the source account to the destination account")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 400, message = "Invalid transaction supplied, (e.g. insufficient funding for transfer)"),
                    @ApiResponse(code = 422, message = "Invalid transaction supplied, (e.g. same account id)"),
                    @ApiResponse(code = 404, message = "Account not found"),
            })
    public Response createTransaction(@NotNull @Valid Transaction transaction) {
        Transaction createdTransaction = transactionService.transferMoney(transaction);
        return Response.status(201).entity(createdTransaction).build();
    }

    @GET
    @Path("/{transactionId}")
    @ApiOperation(value = "Get an existing transaction", response = Transaction.class, httpMethod = "GET")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 400, message = "Invalid transaction id supplied"),
                    @ApiResponse(code = 404, message = "Transaction not found")
            })
    public Transaction getTransaction(@Min(0) @PathParam("transactionId") Long transactionId) {
        Optional<Transaction> transaction = transactionService.getTransaction(transactionId);
        return transaction.orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }
}
