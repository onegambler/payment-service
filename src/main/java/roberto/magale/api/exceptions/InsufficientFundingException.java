package roberto.magale.api.exceptions;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import roberto.magale.api.model.ErrorMessage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class InsufficientFundingException extends WebApplicationException {

    public InsufficientFundingException(Long accountId) {
        super(Response.status(BAD_REQUEST).
                entity(new ErrorMessage(String.format("Account %s has insufficient funding. Request Rejected",
                        accountId)))
                .type(APPLICATION_JSON).build());
    }
}