package com.lukegjpotter.tools.cyclingrouteutils.controller;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteAndDateTimeRecord;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CyclingRouteControllerTest {

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
                .statusCode(HttpStatus.SC_OK)
                .body(equalTo("OK"));
    }

    @Test
    public void testConvertRoute_Strava_NoDateTime() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("https://www.strava.com/routes/123", ""))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("sourceRoute", equalTo("https://www.strava.com/routes/123"),
                        "veloViewerRoute", equalTo("https://www.veloviewer.com/routes/123"),
                        "myWindSockRoute", equalTo("https://mywindsock.com/route/123"),
                        "error", emptyOrNullString());
    }

    @Test
    public void testConvertRoute_Strava_DateTime() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("https://www.strava.com/routes/123", "06/09/2023 16:45 Europe/Dublin"))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("sourceRoute", equalTo("https://www.strava.com/routes/123"),
                        "veloViewerRoute", equalTo("https://www.veloviewer.com/routes/123"),
                        "myWindSockRoute", equalTo("https://mywindsock.com/route/123/#forecast=1694015100"),
                        "error", emptyOrNullString());
    }

    @Test
    public void testConvertRoute_Strava_DateTime_NoHttpsInDomain() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("www.strava.com/routes/123", "06/09/2023 16:45 Europe/Dublin"))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("sourceRoute", equalTo("https://www.strava.com/routes/123"),
                        "veloViewerRoute", equalTo("https://www.veloviewer.com/routes/123"),
                        "myWindSockRoute", equalTo("https://mywindsock.com/route/123/#forecast=1694015100"),
                        "error", emptyOrNullString());
    }

    @Test
    public void testConvertRoute_Strava_DateTime_NoHttpsOrWwwInDomain() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("strava.com/routes/123", "06/09/2023 16:45 Europe/Dublin"))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("sourceRoute", equalTo("https://www.strava.com/routes/123"),
                        "veloViewerRoute", equalTo("https://www.veloviewer.com/routes/123"),
                        "myWindSockRoute", equalTo("https://mywindsock.com/route/123/#forecast=1694015100"),
                        "error", emptyOrNullString());
    }

    @Test
    public void testConvertRoute_RideWithGPS_DateTime_NoHttpsInDomain() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("www.ridewithgps.com/routes/123", "06/09/2023 16:45 IST"))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("sourceRoute", equalTo("https://www.ridewithgps.com/routes/123"),
                        "veloViewerRoute", emptyOrNullString(),
                        "myWindSockRoute", equalTo("https://mywindsock.com/rwgps/route/123/#forecast=1694015100"),
                        "error", emptyOrNullString());
    }

    @Test
    public void testConvertRoute_RideWithGPS_NoDateTime() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("https://www.ridewithgps.com/routes/123", ""))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("sourceRoute", equalTo("https://www.ridewithgps.com/routes/123"),
                        "veloViewerRoute", emptyOrNullString(),
                        "myWindSockRoute", equalTo("https://mywindsock.com/rwgps/route/123"),
                        "error", emptyOrNullString());
    }

    @Test
    public void testConvertRoute_RideWithGPS_DateTime() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("https://www.ridewithgps.com/routes/123", "06/09/2023 16:45 Europe/Dublin"))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("sourceRoute", equalTo("https://www.ridewithgps.com/routes/123"),
                        "veloViewerRoute", emptyOrNullString(),
                        "myWindSockRoute", equalTo("https://mywindsock.com/rwgps/route/123/#forecast=1694015100"),
                        "error", emptyOrNullString());
    }

    @Test
    public void testConvertRoute_RideWithGPS_DateTime_NoHttpsOrWwwInDomain() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("ridewithgps.com/routes/123", "06/09/2023 16:45 Europe/Dublin"))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("sourceRoute", equalTo("https://www.ridewithgps.com/routes/123"),
                        "veloViewerRoute", emptyOrNullString(),
                        "myWindSockRoute", equalTo("https://mywindsock.com/rwgps/route/123/#forecast=1694015100"),
                        "error", emptyOrNullString());
    }

    @Test
    public void testConvertRoute_StravaAppLink_DateTime() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("https://strava.app.link/7VkQ8ZLsKKb", "26/05/2024 12:00 Europe/Dublin"))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(
                        "sourceRoute", equalTo("https://www.strava.com/routes/3203050355643790746"),
                        "veloViewerRoute", equalTo("https://www.veloviewer.com/routes/3203050355643790746"),
                        "myWindSockRoute", equalTo("https://mywindsock.com/route/3203050355643790746/#forecast=1716721200"),
                        "error", emptyOrNullString());
    }

    @Test
    public void testConvertRoute_EdgeCase_NonCyclingWebsite() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("facebook.com", "06/09/2023 16:45 Europe/Dublin"))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body("sourceRoute", equalTo("facebook.com"),
                        "veloViewerRoute", emptyOrNullString(),
                        "myWindSockRoute", emptyOrNullString(),
                        "error", equalTo("NonCyclingWebsite"));
    }

    @Test
    public void testConvertRoute_EdgeCase_DateFormatException() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("https://www.strava.com/routes/123", "Ligma"))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("sourceRoute", equalTo("https://www.strava.com/routes/123"),
                        "veloViewerRoute", equalTo("https://www.veloviewer.com/routes/123"),
                        "myWindSockRoute", equalTo("https://mywindsock.com/route/123"),
                        "error", equalTo("DateFormatException"));
    }

    @Test
    public void testConvertRoute_EdgeCase_MalformedUrlException() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("horse", ""))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body("sourceRoute", equalTo("horse"),
                        "veloViewerRoute", emptyOrNullString(),
                        "myWindSockRoute", emptyOrNullString(),
                        "error", equalTo("MalformedUrlException"));
    }
}
