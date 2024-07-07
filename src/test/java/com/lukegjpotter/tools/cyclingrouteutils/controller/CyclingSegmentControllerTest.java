package com.lukegjpotter.tools.cyclingrouteutils.controller;

import com.lukegjpotter.tools.cyclingrouteutils.dto.SegmentRecord;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CyclingSegmentControllerTest {

    @BeforeAll
    static void beforeAll() {
        baseURI = "http://localhost:8080/";
    }

    @Test
    void convertSegment_StravaSegment() {
        given()
                .contentType(ContentType.JSON)
                .body(new SegmentRecord("https://www.strava.com/segments/22191150"))
                .when()
                .post("/segment")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("stravaSegment", equalTo("https://www.strava.com/segments/22191150"),
                        "veloViewerSegment", equalTo("https://www.veloviewer.com/segments/22191150"),
                        "errorMessage", emptyString());
    }

    @Test
    void convertSegment_StravaAppLinkSegment() {
        given()
                .contentType(ContentType.JSON)
                .body(new SegmentRecord("https://strava.app.link/od3X1LTM2Kb"))
                .when()
                .post("/segment")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("stravaSegment", equalTo("https://www.strava.com/segments/22191150"),
                        "veloViewerSegment", equalTo("https://www.veloviewer.com/segments/22191150"),
                        "errorMessage", emptyString());
    }

    @Test
    void convertSegment_StravaSegment_WithoutWww() {
        given()
                .contentType(ContentType.JSON)
                .body(new SegmentRecord("https://strava.com/segments/22191155"))
                .when()
                .post("/segment")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("stravaSegment", equalTo("https://www.strava.com/segments/22191155"),
                        "veloViewerSegment", equalTo("https://www.veloviewer.com/segments/22191155"),
                        "errorMessage", emptyString());
    }

    @Test
    void convertSegment_StravaSegment_WithoutHttps() {
        given()
                .contentType(ContentType.JSON)
                .body(new SegmentRecord("strava.com/segments/22191169"))
                .when()
                .post("/segment")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("stravaSegment", equalTo("https://www.strava.com/segments/22191169"),
                        "veloViewerSegment", equalTo("https://www.veloviewer.com/segments/22191169"),
                        "errorMessage", emptyString());
    }

    @Test
    void convertSegment_StravaSegment_OnlyHttp() {
        given()
                .contentType(ContentType.JSON)
                .body(new SegmentRecord("http://www.strava.com/segments/22191150"))
                .when()
                .post("/segment")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("stravaSegment", equalTo("https://www.strava.com/segments/22191150"),
                        "veloViewerSegment", equalTo("https://www.veloviewer.com/segments/22191150"),
                        "errorMessage", emptyString());
    }

    @Test
    void convertSegment_EdgeCase_MalformedUrlException() {
        given()
                .contentType(ContentType.JSON)
                .body(new SegmentRecord("ligma"))
                .when()
                .post("/segment")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("stravaSegment", equalTo("ligma"),
                        "veloViewerSegment", equalTo(""),
                        "errorMessage", equalTo("URL is not from Strava."));
    }

    @Test
    void convertSegment_EdgeCase_NotStravaUrl() {
        given()
                .contentType(ContentType.JSON)
                .body(new SegmentRecord("https://www.facebook.com"))
                .when()
                .post("/segment")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("stravaSegment", equalTo("https://www.facebook.com"),
                        "veloViewerSegment", equalTo(""),
                        "errorMessage", equalTo("URL is not from Strava."));
    }

    @Test
    void convertSegment_EdgeCase_StravaRouteUrl() {
        given()
                .contentType(ContentType.JSON)
                .body(new SegmentRecord("https://www.strava.com/route/22191150"))
                .when()
                .post("/segment")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("stravaSegment", equalTo("https://www.strava.com/route/22191150"),
                        "veloViewerSegment", equalTo(""),
                        "errorMessage", equalTo("This Endpoint is for Strava Segments."));
    }
}