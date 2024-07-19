package com.lukegjpotter.tools.cyclingrouteutils.service.component;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteUrlsRecord;
import com.lukegjpotter.tools.cyclingrouteutils.service.UrlNormalisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class CyclingRouteConverterComponent {

    private final Logger logger = LoggerFactory.getLogger(CyclingRouteConverterComponent.class);
    private final UrlNormalisationService urlNormalisationService;

    public CyclingRouteConverterComponent(UrlNormalisationService urlNormalisationService) {
        this.urlNormalisationService = urlNormalisationService;
    }

    public RouteUrlsRecord convertRoute(String routeUrlString, String dateTimeString) throws IOException {
        logger.trace("Convert Route");

        // Standardise URL.
        if (routeUrlString == null || routeUrlString.isEmpty())
            return new RouteUrlsRecord(routeUrlString, "", "", "Route URL is empty.");
        if (routeUrlString.startsWith("www.")) routeUrlString = "https://" + routeUrlString;
        if (routeUrlString.startsWith("strava.com") || routeUrlString.startsWith("ridewithgps.com"))
            routeUrlString = "https://www." + routeUrlString;

        // Check if Standardised URL is valid.
        URL routeUrl = urlNormalisationService.validUrlCheck(routeUrlString);

        // Resolve Strava.App.Link URLs.
        routeUrl = urlNormalisationService.resolveStravaAppLink(routeUrl);

        if (!routeUrl.getHost().contains("strava.com") && !routeUrl.getHost().contains("ridewithgps.com")) {
            return new RouteUrlsRecord(routeUrl.toString(), "", "", "URL is not Strava or RideWithGPS.");
        }

        // Apply the MyWindSock Date and Time Query String.
        String errorMessage = "";
        String forecastPostfix = "";
        if (dateTimeString == null) dateTimeString = "";

        if (!dateTimeString.isEmpty() && (routeUrl.getHost().contains("strava.com") || routeUrl.getHost().contains("ridewithgps.com"))) {
            try {
                forecastPostfix = "/#forecast=" + ZonedDateTime.from(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm zzz")
                        .parse(dateTimeString.trim())).toEpochSecond();
            } catch (DateTimeParseException dtpe) {
                // Don't throw the DTPE, as we can still give the user the route URLs, along with the message.
                errorMessage = "ZonedDateTime format is incorrect. Please use 'dd/MM/yyyy HH:mm zzz', for example '31/12/2024 23:59 Europe/Dublin'. You supplied '" + dateTimeString + "'.";
            }
        }

        // Populate VeloViewer and MyWindSock URLs.
        String veloViewerUrlString = "", myWindSockUrlString = "";
        if (routeUrl.getHost().contains("strava.com")) {
            String routeCode = routeUrl.getPath().substring("/routes/".length());
            veloViewerUrlString = "https://www.veloviewer.com/routes/" + routeCode;
            myWindSockUrlString = "https://mywindsock.com/route/" + routeCode + forecastPostfix;
        } else if (routeUrl.getHost().contains("ridewithgps.com")) {
            String routeCode = routeUrl.getPath().substring("/routes/".length());
            myWindSockUrlString = "https://mywindsock.com/rwgps/route/" + routeCode + forecastPostfix;
        } else {
            errorMessage = "URL is not Strava or RideWithGPS.";
        }

        return new RouteUrlsRecord(routeUrl.toString(), veloViewerUrlString, myWindSockUrlString, errorMessage);
    }
}
