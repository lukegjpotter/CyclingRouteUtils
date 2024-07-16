package com.lukegjpotter.tools.cyclingrouteutils.controller;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteAndDateTimeRecord;
import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteUrlsRecord;
import com.lukegjpotter.tools.cyclingrouteutils.service.CyclingRouteConverterService;
import com.lukegjpotter.tools.cyclingrouteutils.service.JsonToHtmlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CyclingRouteController {

    private final Logger logger = LoggerFactory.getLogger(CyclingRouteController.class);
    private final CyclingRouteConverterService converterService;
    private final JsonToHtmlService jsonToHtmlService;

    public CyclingRouteController(CyclingRouteConverterService converterService, JsonToHtmlService jsonToHtmlService) {
        this.converterService = converterService;
        this.jsonToHtmlService = jsonToHtmlService;
    }

    @PostMapping("/route")
    public ResponseEntity<RouteUrlsRecord> convertRoute(@RequestBody RouteAndDateTimeRecord routeAndDateTime) {
        logger.info("Endpoint Convert Route called with {}", routeAndDateTime);

        String errorMessage;
        try {
            RouteUrlsRecord routeUrlsRecord = converterService.convertRoute(routeAndDateTime);
            if (routeUrlsRecord.errorMessage().isEmpty()) return ResponseEntity.ok(routeUrlsRecord);
            else return ResponseEntity.internalServerError().body(routeUrlsRecord);
        } catch (IOException ioe) {
            logger.error("Error converting route: {}", ioe.getMessage());
            return ResponseEntity.internalServerError().body(new RouteUrlsRecord(
                    routeAndDateTime.url(),
                    "",
                    "",
                    "URL is not Strava or RideWithGPS."));
        }
    }

    @PostMapping("/route/html")
    public ResponseEntity<String> convertRouteToHtml(@RequestBody RouteAndDateTimeRecord routeAndDateTime) {
        RouteUrlsRecord routeUrlsRecord;
        try {
            routeUrlsRecord = converterService.convertRoute(routeAndDateTime);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("<html><body><p>Error</p></body></html>");
        }
        if (!routeUrlsRecord.errorMessage().isEmpty()) {
            return ResponseEntity.internalServerError().body("<html><body><p>Error</p></body></html>");
        }

        String html = jsonToHtmlService.convertRoute(routeUrlsRecord);

        return ResponseEntity.ok(html);
    }

    @GetMapping("/test")
    public ResponseEntity<RouteUrlsRecord> testJsonOutput() {
        logger.trace("Endpoint Test called");

        RouteUrlsRecord testRecord = new RouteUrlsRecord(
                "https://www.strava.com/routes/123",
                "https://www.veloviewer.com/routes/123",
                "https://mywindsock.com/route/123",
                "");

        return ResponseEntity.ok(testRecord);
    }

    @GetMapping("/health")
    public ResponseEntity<String> getHealth() {
        logger.trace("Endpoint Health called");
        return ResponseEntity.ok("OK");
    }
}
