package com.lukegjpotter.tools.cyclingrouteutils.controller;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteAndDateTimeRecord;
import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteUrlsRecord;
import com.lukegjpotter.tools.cyclingrouteutils.service.CyclingRouteConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class CyclingRouteController {

    @Autowired
    private CyclingRouteConverterService converterService;

    private final Logger logger = LoggerFactory.getLogger(CyclingRouteController.class);

    @PostMapping("route")
    public RouteUrlsRecord convertRoute(@RequestBody RouteAndDateTimeRecord routeAndDateTime) {

        logger.info("Endpoint Convert Route called");

        return converterService.convertRoute(routeAndDateTime);
    }

    @GetMapping("test")
    public RouteUrlsRecord testJsonOutput() {
        logger.info("Endpoint Test called");

        return new RouteUrlsRecord(
                "https://www.strava.com/routes/123",
                "https://www.veloviewer.com/routes/123",
                "https://mywindsock.com/route/123");
    }

    @GetMapping("health")
    public String getHealth() {
        logger.trace("Endpoint health called");
        return "OK";
    }
}
