package org.bai.security.library.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.bai.security.library.domain.Message;

@Path("/hello-world")
public class HelloResource {

    @GET
    @Produces({"application/json"})
    public Message hello() {
        return new Message("Hello, World!");
    }

//    @GET()
//    @Produces({"application/json"})
//    public String hello() {
//        return "Hello, World!";
//    }
}