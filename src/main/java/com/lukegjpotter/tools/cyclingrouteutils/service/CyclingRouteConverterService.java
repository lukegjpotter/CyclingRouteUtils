package com.lukegjpotter.tools.cyclingrouteutils.service;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteAndDateTimeRecord;
import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteUrlsRecord;
import com.lukegjpotter.tools.cyclingrouteutils.service.component.CyclingRouteConverterComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CyclingRouteConverterService {

    private final Logger logger = LoggerFactory.getLogger(CyclingRouteConverterService.class);
    @Autowired
    CyclingRouteConverterComponent cyclingRouteConverter;

    public RouteUrlsRecord convertRoute(RouteAndDateTimeRecord routeAndDateTime) {
        logger.trace("Convert Route");
        return cyclingRouteConverter.convertRoute(routeAndDateTime.url(), routeAndDateTime.dateTime());
    }
}
