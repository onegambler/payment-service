package roberto.magale.api.exceptions;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import roberto.magale.api.model.ErrorMessage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class AccountNotFoundException extends WebApplicationException {

    public AccountNotFoundException(Long accountId) {
        super(Response.status(NOT_FOUND).
                entity(new ErrorMessage(String.format("Account %s not found", accountId)))
                .type(APPLICATION_JSON).build());
    }
}
