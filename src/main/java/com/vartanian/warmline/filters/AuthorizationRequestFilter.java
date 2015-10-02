package com.vartanian.warmline.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;

/**
 * Created by super on 10/2/15.
 */
public class AuthorizationRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {

//        final SecurityContext securityContext = requestContext.getSecurityContext();
//        if (securityContext == null || !securityContext.isUserInRole("privileged")) {
//
//            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
//                    .entity("User cannot access the resource.")
//                    .build());
//        }
    }
}