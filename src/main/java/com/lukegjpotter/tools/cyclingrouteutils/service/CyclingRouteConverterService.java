package com.lukegjpotter.tools.cyclingrouteutils.service;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteAndDateTimeRecord;
import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteUrlsRecord;
import com.lukegjpotter.tools.cyclingrouteutils.service.component.CyclingRouteConverterComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CyclingRouteConverterService {

    @Autowired
    CyclingRouteConverterComponent cyclingRouteConverter;

    public RouteUrlsRecord convertRoute(RouteAndDateTimeRecord routeAndDateTime) {

        return cyclingRouteConverter.convertRoute(routeAndDateTime.url(), routeAndDateTime.dateTime());
    }
}
