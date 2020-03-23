package org.otaibe.nginx.with.eureka.demo.producer.service;

import io.vertx.mutiny.core.Vertx;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.otaibe.commons.quarkus.eureka.client.service.EurekaClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@ApplicationScoped
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@Slf4j
public class NginxService {

    public static final String DEMO_MICROSERVICE_SERVERS_PLACEHOLDER = "__DEMO_MICROSERVICE_SERVERS__";
    public static final String SERVER_PORT_PLACEHOLDER = "__SERVER_HTTP_PORT__";
    public static final String SERVER_BAD_GATEWAY_PORT_PLACEHOLDER = "__SERVER_BAD_GATEWAY_PORT__";

    private String nginxConfigTemplate;
    private String lastConfig;
    private String badGatewayServer;

    @ConfigProperty(name = "cloud-gateway.http.port")
    Integer cloudGatewayPort;
    @ConfigProperty(name = "cloud-gateway.bad-gateway.port")
    Integer cloudGatewayBadGatewayPort;
    @ConfigProperty(name = "nginx.config.file")
    String nginxConfigTemplatePath;
    @ConfigProperty(name = "eureka.demo-microservice.name")
    String demoMicroserviceName;

    @Inject
    Vertx vertx;
    @Inject
    EurekaClient eurekaClient;

    @PostConstruct
    public void init() {
        log.info("init start");
        badGatewayServer = MessageFormat.format("server localhost:{0,number,#};\n", getCloudGatewayBadGatewayPort());
        nginxConfigTemplate = getVertx().fileSystem().readFileBlocking(getNginxConfigTemplatePath()).toString();
        log.info("init end");
    }

    public String getNginxConfig() {
        String config = getNginxConfigTemplate();
        String config1 = addDemoMicroserviceConfig(config);
        String config2 = StringUtils.replace(config1, SERVER_BAD_GATEWAY_PORT_PLACEHOLDER, getCloudGatewayBadGatewayPort().toString());
        String config3 = StringUtils.replace(config2, SERVER_PORT_PLACEHOLDER, getCloudGatewayBadGatewayPort().toString());
        return config3;
    }

    private String addDemoMicroserviceConfig(String config) {
        return buildUpstreamServers(getServers(getDemoMicroserviceName()))
                .map(s -> StringUtils.replace(config, DEMO_MICROSERVICE_SERVERS_PLACEHOLDER, s))
                .orElse(StringUtils.replace(config, DEMO_MICROSERVICE_SERVERS_PLACEHOLDER, badGatewayServer));
    }

    Optional<String> buildUpstreamServers(List<String> servers) {
        if (CollectionUtils.isEmpty(servers)) {
            return Optional.of(badGatewayServer);
        }
        return servers.stream()
                .map(s -> MessageFormat.format("server {0};", s))
                .reduce((s, s1) -> StringUtils.join(s, s1, "\n"));
    }

    List<String> getServers(String name) {
        Collection<String> servers = new ConcurrentLinkedQueue<>();
        AtomicBoolean found = new AtomicBoolean(false);

        return Mono.defer(() -> getEurekaClient().getNextServer(name))
                .map(s -> {
                    found.set(found.get() || servers.contains(s));
                    boolean isFound = found.get();
                    if (!isFound) {
                        servers.add(s);
                        return s;
                    }
                    return StringUtils.EMPTY;
                })
                .repeat(() -> !found.get())
                .filter(s -> StringUtils.isNotBlank(s))
                .map(s -> {
                    String s1 = StringUtils.replace(s, "http://", StringUtils.EMPTY);
                    String s2 = StringUtils.replace(s1, "https://", StringUtils.EMPTY);
                    while (StringUtils.isNotBlank(s2) && StringUtils.endsWith(s2, "/")) {
                        s2 = StringUtils.substring(s2, 0, s2.length() - 1);
                    }
                    log.debug("service={}, server={}", name, s2);
                    return s2;
                })
                .collectList()
                .block();
    }
}
