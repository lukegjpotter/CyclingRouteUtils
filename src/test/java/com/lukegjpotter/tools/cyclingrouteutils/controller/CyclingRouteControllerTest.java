package com.lukegjpotter.tools.cyclingrouteutils.controller;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteAndDateTimeRecord;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

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
    @Order(2)
    public void testTest() {
        when()
                .get("/test")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(
                        "sourceRoute", containsStringIgnoringCase("strava"),
                        "veloViewerRoute", containsStringIgnoringCase("veloviewer"),
                        "myWindSockRoute", containsStringIgnoringCase("mywindsock"),
                        "errorMessage", emptyString());
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
                        "errorMessage", emptyString());
    }

    @Test
    public void testConvertRoute_Strava_DateTime() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("https://www.strava.com/routes/3203050355643790746", "06/09/2023 16:45 Europe/Dublin"))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("sourceRoute", equalTo("https://www.strava.com/routes/3203050355643790746"),
                        "veloViewerRoute", equalTo("https://www.veloviewer.com/routes/3203050355643790746"),
                        "myWindSockRoute", equalTo("https://mywindsock.com/route/3203050355643790746/#forecast=1694015100"),
                        "errorMessage", emptyString());
    }

    @Test
    public void testConvertRoute_Strava_DateTime_NoHttpsInDomain() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("www.strava.com/routes/3200099234954750760", "06/09/2023 16:45 Europe/Dublin"))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("sourceRoute", equalTo("https://www.strava.com/routes/3200099234954750760"),
                        "veloViewerRoute", equalTo("https://www.veloviewer.com/routes/3200099234954750760"),
                        "myWindSockRoute", equalTo("https://mywindsock.com/route/3200099234954750760/#forecast=1694015100"),
                        "errorMessage", emptyString());
    }

    @Test
    public void testConvertRoute_Strava_DateTime_NoHttpsOrWwwInDomain() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("strava.com/routes/3126093884440601656", "06/09/2023 16:45 Europe/Dublin"))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("sourceRoute", equalTo("https://www.strava.com/routes/3126093884440601656"),
                        "veloViewerRoute", equalTo("https://www.veloviewer.com/routes/3126093884440601656"),
                        "myWindSockRoute", equalTo("https://mywindsock.com/route/3126093884440601656/#forecast=1694015100"),
                        "errorMessage", emptyString());
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
                        "errorMessage", emptyString());
    }

    @Test
    public void testConvertRoute_RideWithGPS_NoDateTime() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("https://www.ridewithgps.com/routes/32964326", ""))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("sourceRoute", equalTo("https://www.ridewithgps.com/routes/32964326"),
                        "veloViewerRoute", emptyOrNullString(),
                        "myWindSockRoute", equalTo("https://mywindsock.com/rwgps/route/32964326"),
                        "errorMessage", emptyString());
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
                        "errorMessage", emptyString());
    }

    @Test
    public void testConvertRoute_RideWithGPS_DateTime_NoHttpsOrWwwInDomain() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("ridewithgps.com/routes/32964327", "06/09/2023 16:45 Europe/Dublin"))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("sourceRoute", equalTo("https://www.ridewithgps.com/routes/32964327"),
                        "veloViewerRoute", emptyOrNullString(),
                        "myWindSockRoute", equalTo("https://mywindsock.com/rwgps/route/32964327/#forecast=1694015100"),
                        "errorMessage", emptyString());
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
                        "errorMessage", emptyString());
    }

    @Test
    public void testConvertRoute_EdgeCase_NonCyclingWebsite_MalformedUrlException() {
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
                        "errorMessage", equalTo("URL is not Strava or RideWithGPS."));
    }

    @Test
    public void testConvertRoute_EdgeCase_NonCyclingWebsite_DateFormatParseException() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("https://www.facebook.com", "32/13/2069 16:45 Europe/Dublin"))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body("sourceRoute", equalTo("https://www.facebook.com"),
                        "veloViewerRoute", emptyOrNullString(),
                        "myWindSockRoute", emptyOrNullString(),
                        "errorMessage", equalTo("URL is not Strava or RideWithGPS."));
    }

    @Test
    public void testConvertRoute_EdgeCase_DateFormatException() {
        given()
                .contentType(ContentType.JSON)
                .body(new RouteAndDateTimeRecord("https://www.strava.com/routes/123", "Ligma"))
                .when()
                .post("/route")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body("sourceRoute", equalTo("https://www.strava.com/routes/123"),
                        "veloViewerRoute", equalTo("https://www.veloviewer.com/routes/123"),
                        "myWindSockRoute", equalTo("https://mywindsock.com/route/123"),
                        "errorMessage", equalTo("ZonedDateTime format is incorrect. Please use 'dd/MM/yyyy HH:mm zzz', for example '31/12/2024 23:59 Europe/Dublin'. You supplied 'Ligma'."));
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
                        "errorMessage", equalTo("URL is not Strava or RideWithGPS."));
    }
}
