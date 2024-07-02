package com.lukegjpotter.tools.cyclingrouteutils.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CyclingRouteControllerTest {

    //ToDo: Use RestAssured in this test class.
    @Autowired
    CyclingRouteController cyclingRouteController;

    @BeforeAll
    static void setUp() {
        baseURI = "http://localhost:8080/";
    }

    @Test
    @Order(1)
    public void healthCheck() {
        when()
                .get("/health")
                .then()
                .statusCode(200)
                .body(equalTo("OK"));
    }

    //@Test
    public void testConvertRoute_Strava_NoDateTime() {
    }

    @Test
    public void testConvertRoute_Strava_DateTime() {
        given()
                .header("Content-Type", "application/json")
                .body("{\"url\": \"https://www.strava.com/routes/123\","
                        + " \"dateTime\": \"06/09/2023 16:45 Europe/Dublin\"}")
                .when()
                .post("/route")
                .then()
                .statusCode(200)
                .body("sourceRoute", equalTo("https://www.strava.com/routes/123"))
                .body("veloViewerRoute", equalTo("https://www.veloviewer.com/routes/123"))
                .body("myWindSockRoute", equalTo("https://mywindsock.com/route/123/#forecast=1694015100"))
                .body("error", emptyOrNullString());
    }

    //@Test
    public void testConvertRoute_RideWithGPS_NoDateTime() {
    }

    //@Test
    public void testConvertRoute_RideWithGPS_DateTime() {
    }
}
