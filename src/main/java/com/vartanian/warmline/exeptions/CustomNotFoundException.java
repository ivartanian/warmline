package com.vartanian.warmline.exeptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Created by super on 10/2/15.
 */
public class CustomNotFoundException extends WebApplicationException {

    /**
     * Create a HTTP 404 (Not Found) exception.
     */
    public CustomNotFoundException() {
        super(Response.status(404).build());
    }

    /**
     * Create a HTTP 404 (Not Found) exception.
     *
     * @param message the String that is the entity of the 404 response.
     */
    public CustomNotFoundException(String message) {
        super(Response.status(Response.Status.NOT_FOUND).
                entity(message).type("text/plain").build());
    }
}