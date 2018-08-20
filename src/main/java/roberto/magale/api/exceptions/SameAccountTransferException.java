package roberto.magale.api.exceptions;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import roberto.magale.api.model.ErrorMessage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class SameAccountTransferException extends WebApplicationException {

    public SameAccountTransferException() {
        super(Response.status(BAD_REQUEST).
                entity(new ErrorMessage("An account can't be source and destination. Request Rejected"))
                .type(APPLICATION_JSON).build());
    }
}
