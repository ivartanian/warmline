package com.vartanian.warmline.example;


import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.net.URI;

/**
 * Created by super on 10/1/15.
 */
// The Java class will be hosted at the URI path "/test"
@Path("/test")
public class HelloWorld {
    // The Java method will process HTTP GET requests
    @GET
    @Produces("text/plain")
    public String getClichedMessage() {

        // Return some cliched textual content
        return "Hello World";
    }

    public static void main(String[] args) throws Exception {


    }

}
