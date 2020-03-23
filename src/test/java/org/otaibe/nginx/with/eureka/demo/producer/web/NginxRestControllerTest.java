package org.otaibe.nginx.with.eureka.demo.producer.web;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.otaibe.nginx.with.eureka.demo.producer.service.NginxService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

@QuarkusTest
public class NginxRestControllerTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get(NginxRestController.ROOT_PATH + NginxRestController.CONFIG)
                .then()
                .statusCode(200)
                .body(not(containsString(NginxService.DEMO_MICROSERVICE_SERVERS_PLACEHOLDER)))
                .body(not(containsString(NginxService.SERVER_PORT_PLACEHOLDER)))
                .body(not(containsString(NginxService.SERVER_BAD_GATEWAY_PORT_PLACEHOLDER)))
        ;
    }

}