package com.lukegjpotter.tools.cyclingrouteutils.service.component;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteUrlsRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CyclingRouteConverterComponent {

    private final Logger logger = LoggerFactory.getLogger(CyclingRouteConverterComponent.class);

    public RouteUrlsRecord convertRoute(String routeUrl, String dateTimeString) throws MalformedURLException {
        logger.trace("Convert Route");

        if (routeUrl.isEmpty()) return new RouteUrlsRecord(routeUrl, "", "", "Route URL is empty.");
        if (routeUrl.startsWith("www.")) routeUrl = "https://" + routeUrl;
        if (routeUrl.startsWith("strava.com") || routeUrl.startsWith("ridewithgps.com"))
            routeUrl = "https://www." + routeUrl;

        try {
            new URL(routeUrl);
        } catch (MalformedURLException e) {
            throw new MalformedURLException(e.toString());
        }

        String forecastPostfix = "";
        if (dateTimeString == null) dateTimeString = "";
        if (!dateTimeString.isEmpty()) forecastPostfix = convertDateTimeToMyWindSockForecastPostfix(dateTimeString);

        int substringBeginIndex = determineSubstringBeginIndex(routeUrl);

        String veloViewerURL = "", myWindSockURL = "", errorMessage = "";
        String hostname = routeUrl.substring(substringBeginIndex);

        if (hostname.startsWith("strava.com")) {
            String urlPath = hostname.substring("strava.com/routes/".length());
            veloViewerURL = "https://www.veloviewer.com/routes/" + urlPath;
            myWindSockURL = "https://mywindsock.com/route/" + urlPath + forecastPostfix;
        } else if (hostname.startsWith("ridewithgps.com")) {
            String urlPath = hostname.substring("ridewithgps.com/routes/".length());
            myWindSockURL = "https://mywindsock.com/rwgps/route/" + urlPath + forecastPostfix;
        } else {
            errorMessage = "URL is not Strava or RideWithGPS.";
        }

        return new RouteUrlsRecord(routeUrl, veloViewerURL, myWindSockURL, errorMessage);
    }

    private String convertDateTimeToMyWindSockForecastPostfix(String dateTimeString) {

        ZonedDateTime zonedDateTime = ZonedDateTime.from(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm z").parse(dateTimeString));
        return "/#forecast=" + zonedDateTime.toEpochSecond();
    }

    private int determineSubstringBeginIndex(String routeUrl) {
        int substringBeginIndex = 0;

        if (routeUrl.startsWith("https://www.")) {
            substringBeginIndex = "https://www.".length();
        } else if (routeUrl.startsWith("http://www.")) {
            substringBeginIndex = "http://www.".length();
        } else if (routeUrl.startsWith("www.")) {
            substringBeginIndex = "www.".length();
        } else if (routeUrl.startsWith("https://")) {
            substringBeginIndex = "https://".length();
        } else if (routeUrl.startsWith("http://")) {
            substringBeginIndex = "http://".length();
        }
        return substringBeginIndex;
    }
}
