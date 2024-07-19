package com.lukegjpotter.tools.cyclingrouteutils.controller;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteAndDateTimeRecord;
import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteUrlsRecord;
import com.lukegjpotter.tools.cyclingrouteutils.service.CyclingRouteConverterService;
import com.lukegjpotter.tools.cyclingrouteutils.service.RecordToHtmlService;
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
    private final RecordToHtmlService recordToHtmlService;

    public CyclingRouteController(CyclingRouteConverterService converterService, RecordToHtmlService recordToHtmlService) {
        this.converterService = converterService;
        this.recordToHtmlService = recordToHtmlService;
    }

    @PostMapping("/route")
    public ResponseEntity<RouteUrlsRecord> convertRoute(@RequestBody RouteAndDateTimeRecord routeAndDateTime) {
        logger.info("Endpoint Convert Route called with {}", routeAndDateTime);

        try {
            RouteUrlsRecord routeUrlsRecord = converterService.convertRoute(routeAndDateTime);

            if (routeUrlsRecord.errorMessage().isEmpty()) return ResponseEntity.ok(routeUrlsRecord);
            else return ResponseEntity.badRequest().body(routeUrlsRecord);
        } catch (IOException ioe) {
            logger.error("Error converting route: {}", ioe.getMessage());
            return ResponseEntity.badRequest().body(new RouteUrlsRecord(
                    routeAndDateTime.url(),
                    "",
                    "",
                    "URL is not Strava or RideWithGPS."));
        }
    }

    @PostMapping("/route/html")
    public ResponseEntity<String> convertRouteToHtml(@RequestBody RouteAndDateTimeRecord routeAndDateTime) {
        logger.info("Endpoint Convert Route to HTML called with {}", routeAndDateTime);

        RouteUrlsRecord routeUrlsRecord = convertRoute(routeAndDateTime).getBody();

        if (!routeUrlsRecord.errorMessage().isEmpty()) {
            return ResponseEntity.badRequest().body(String.format("""
                    <html>
                        <head>
                            <style>body {font-family: Arial, Helvetica, sans-serif;}</style>
                            <title>Error</title>
                        </head>
                        <body>
                            <p>Error: %s</p>
                            <p>%s</p>
                        </body>
                    </html>
                    """, routeUrlsRecord.errorMessage(), routeUrlsRecord));
        }

        return ResponseEntity.ok(recordToHtmlService.convertRoute(routeUrlsRecord));
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
