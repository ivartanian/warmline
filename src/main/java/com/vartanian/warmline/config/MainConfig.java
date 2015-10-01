package com.vartanian.warmline.config;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.ApplicationPath;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by super on 10/1/15.
 */
@ApplicationPath(value = "/")
public class MainConfig extends ResourceConfig {

    public MainConfig() {

        packages("com.vartanian.warmline");

        // Enable Tracing support.
        property(ServerProperties.TRACING, "ALL");

    }
}
