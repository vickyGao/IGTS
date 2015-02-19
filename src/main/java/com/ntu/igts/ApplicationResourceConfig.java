package com.ntu.igts;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@ApplicationPath("/*")
public class ApplicationResourceConfig extends ResourceConfig {

    public ApplicationResourceConfig() {
        packages("com.ntu.igts");
        register(JacksonJsonProvider.class);
    }
}
