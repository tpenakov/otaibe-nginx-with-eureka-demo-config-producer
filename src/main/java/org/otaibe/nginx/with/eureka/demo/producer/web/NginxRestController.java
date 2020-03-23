package org.otaibe.nginx.with.eureka.demo.producer.web;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.otaibe.nginx.with.eureka.demo.producer.service.NginxService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(NginxRestController.ROOT_PATH)
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@Slf4j
public class NginxRestController {

    public static final String ROOT_PATH = "/nginx";
    public static final String CONFIG = "/config";
    @Inject
    NginxService service;

    @GET
    @Path(CONFIG)
    @Produces(MediaType.TEXT_PLAIN)
    public String getConfig() {
        return getService().getNginxConfig();
    }
}