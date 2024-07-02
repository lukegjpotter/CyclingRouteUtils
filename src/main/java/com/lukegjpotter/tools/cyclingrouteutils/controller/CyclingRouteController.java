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
import java.net.MalformedURLException;
import java.time.format.DateTimeParseException;

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

        String errorMessage;
        try {
            RouteUrlsRecord routeUrlsRecord = converterService.convertRoute(routeAndDateTime);
            if (routeUrlsRecord.errorMessage().isEmpty()) return ResponseEntity.ok(routeUrlsRecord);
            else return ResponseEntity.internalServerError().body(routeUrlsRecord);
        } catch (MalformedURLException mue) {
            errorMessage = "URL is not Strava or RideWithGPS.";
        } catch (DateTimeParseException dtpe) {
            errorMessage = "The URL is not Correct.";
        } catch (IOException e) {
            logger.error("Error converting route: {}", e.getMessage());
            errorMessage = "Error converting route.";
        }
        return ResponseEntity.internalServerError().body(new RouteUrlsRecord(
                routeAndDateTime.url(),
                "",
                "",
                errorMessage));
    }

    @GetMapping("test")
    public ResponseEntity<RouteUrlsRecord> testJsonOutput() {
        logger.trace("Endpoint Test called");

        RouteUrlsRecord testRecord = new RouteUrlsRecord(
                "https://www.strava.com/routes/123",
                "https://www.veloviewer.com/routes/123",
                "https://mywindsock.com/route/123",
                "");

        return ResponseEntity.ok(testRecord);
    }

    @GetMapping("health")
    public ResponseEntity<String> getHealth() {
        logger.trace("Endpoint Health called");
        return ResponseEntity.ok("OK");
    }
}
