package roberto.magale.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.google.inject.Inject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import roberto.magale.api.exceptions.AccountNotFoundException;
import roberto.magale.api.model.Account;
import roberto.magale.service.AccountService;

import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/account")
@Api(value = "/account", description = "CRUD operations on accounts")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class AccountResource {

    private final AccountService accountService;

    @Inject
    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @POST
    @ApiOperation(value = "Add a new account", response = Account.class, httpMethod = "POST")
    public Response addAccount(@NotNull @Valid Account account) {
        Account createdAccount = accountService.addAccount(account);
        return Response.status(201).entity(createdAccount).build();
    }

    @PUT
    @Path("/{accountId}")
    @ApiOperation(value = "Updates an existing account", response = Account.class, httpMethod = "PUT")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 400, message = "Invalid account supplied"),
                    @ApiResponse(code = 404, message = "Account not found")
            })
    public Account updateAccount(@NotNull @Valid Account account, @Min(0) @PathParam("accountId") Long accountId) {
        Optional<Account> updateAccount = accountService.updateAccount(account, accountId);
        return updateAccount.orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    @GET
    @Path("/{accountId}")
    @ApiOperation(value = "Get an existing account", response = Account.class, httpMethod = "GET")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 400, message = "Invalid account id supplied"),
                    @ApiResponse(code = 404, message = "Account not found")
            })
    public Account getAccount(@Min(0) @PathParam("accountId") Long accountId) {
        Optional<Account> account = accountService.getAccount(accountId);
        return account.orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    @DELETE
    @Path("/{accountId}")
    @ApiOperation(value = "Delete an existing account", httpMethod = "DELETE")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 400, message = "Invalid account supplied"),
                    @ApiResponse(code = 404, message = "Account not found")
            })
    public Response deleteAccount(@Min(0) @PathParam("accountId") Long accountId) {
        boolean result = accountService.removeAccount(accountId);
        if (!result) {
            throw new AccountNotFoundException(accountId);
        }
        return Response.noContent().build();
    }
}
