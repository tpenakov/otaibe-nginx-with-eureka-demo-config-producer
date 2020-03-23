package org.otaibe.nginx.with.eureka.demo.producer.service;

import io.vertx.mutiny.core.Vertx;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.otaibe.commons.quarkus.eureka.client.service.EurekaClient;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@Slf4j
public class NginxService extends org.otaibe.commons.quarkus.nginx.eureka.NginxService {

    public static final String DEMO_MICROSERVICE_SERVERS_PLACEHOLDER = "__DEMO_MICROSERVICE_SERVERS__";

    @ConfigProperty(name = "eureka.demo-microservice.name")
    String demoMicroserviceName;

    @Inject
    Vertx vertx;
    @Inject
    EurekaClient eurekaClient;

    @PostConstruct
    public void init() {
        log.info("init start");
        super.init();
        log.info("init end");
    }

    public String getNginxConfig() {
        String config = getNginxConfigTemplate();
        String config1 = addDemoMicroserviceConfig(config);
        return config1;
    }

    private String addDemoMicroserviceConfig(String config) {
        return buildUpstreamServers(getServers(getDemoMicroserviceName()))
                .map(s -> StringUtils.replace(config, DEMO_MICROSERVICE_SERVERS_PLACEHOLDER, s))
                .orElse(StringUtils.replace(config, DEMO_MICROSERVICE_SERVERS_PLACEHOLDER, getBadGatewayServer()));
    }
}
