package org.otaibe.nginx.with.eureka.demo.producer.config;

import io.quarkus.runtime.StartupEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.otaibe.commons.quarkus.eureka.client.service.EurekaClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * Class is used to eagerly load all required beans and will try to preserve the bean load logic
 * All beans that require such load should be added here as dependencies
 */
@ApplicationScoped
@Getter
@Setter
@Slf4j
public class EagerBeansLoader {
    @Inject
    EurekaClient eurekaClient;

    public void init(@Observes StartupEvent event) throws Exception {
        log.info("init start");
        getEurekaClient().getAllApps(); //not enough just to inject bean ...
        log.info("init end");
    }
}
