package com.lukegjpotter.tools.cyclingrouteutils.controller;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteAndDateTimeRecord;
import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteUrlsRecord;
import com.lukegjpotter.tools.cyclingrouteutils.service.CyclingRouteConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController("/")
public class CyclingRouteController {

    private final Logger logger = LoggerFactory.getLogger(CyclingRouteController.class);
    private final CyclingRouteConverterService converterService;

    @Autowired
    public CyclingRouteController(CyclingRouteConverterService converterService) {
        this.converterService = converterService;
    }

    @PostMapping("route")
    public ResponseEntity<RouteUrlsRecord> convertRoute(@RequestBody RouteAndDateTimeRecord routeAndDateTime) {
        logger.info("Endpoint Convert Route called with {}", routeAndDateTime);

        try {
            return ResponseEntity.ok(converterService.convertRoute(routeAndDateTime));
        } catch (IOException e) {
            logger.error("Error converting route: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(new RouteUrlsRecord(
                    routeAndDateTime.url(),
                    "",
                    "",
                    "The URL is not Correct."));
            // FixMe: If the date format is not correct, the URL message is given back, when it should be the date message.
        }
    }

    @GetMapping("test")
    public ResponseEntity<RouteUrlsRecord> testJsonOutput() {
        logger.trace("Endpoint Test called");

        RouteUrlsRecord testRecord = new RouteUrlsRecord(
                "https://www.strava.com/routes/123",
                "https://www.veloviewer.com/routes/123",
                "https://mywindsock.com/route/123",
                null);

        return ResponseEntity.ok(testRecord);
    }

    @GetMapping("/health")
    public ResponseEntity<String> getHealth() {
        logger.trace("Endpoint Health called");
        return ResponseEntity.ok("OK");
    }
}
