package com.lukegjpotter.tools.cyclingrouteutils.service;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteAndDateTimeRecord;
import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteUrlsRecord;
import com.lukegjpotter.tools.cyclingrouteutils.service.component.CyclingRouteConverterComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CyclingRouteConverterService {

    private final Logger logger = LoggerFactory.getLogger(CyclingRouteConverterService.class);
    private final CyclingRouteConverterComponent cyclingRouteConverter;

    public CyclingRouteConverterService(CyclingRouteConverterComponent cyclingRouteConverter) {
        this.cyclingRouteConverter = cyclingRouteConverter;
    }

    public RouteUrlsRecord convertRoute(RouteAndDateTimeRecord routeAndDateTime) throws IOException {
        logger.trace("Convert Route");

        return cyclingRouteConverter.convertRoute(routeAndDateTime.url(), routeAndDateTime.dateTime());
    }
}
