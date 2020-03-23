package org.otaibe.nginx.with.eureka.demo.microservice.web;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class NginxRestControllerTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/nginx/conf")
          .then()
             .statusCode(200)
             .body(is("hello"));
    }

}