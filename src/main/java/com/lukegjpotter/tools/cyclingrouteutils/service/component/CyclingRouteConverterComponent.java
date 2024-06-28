package com.lukegjpotter.tools.cyclingrouteutils.service.component;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteUrlsRecord;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class CyclingRouteConverterComponent {

    private final Logger logger = LoggerFactory.getLogger(CyclingRouteConverterComponent.class);

    public RouteUrlsRecord convertRoute(String routeUrlString, String dateTimeString) throws IOException {
        logger.trace("Convert Route");

        // Standardise URL.
        if (routeUrlString == null || routeUrlString.isEmpty())
            return new RouteUrlsRecord(routeUrlString, "", "", "Route URL is empty.");
        if (routeUrlString.startsWith("www.")) routeUrlString = "https://" + routeUrlString;
        if (routeUrlString.startsWith("strava.com") || routeUrlString.startsWith("ridewithgps.com"))
            routeUrlString = "https://www." + routeUrlString;

        routeUrlString = routeUrlString.trim();

        // Check if Standardised URL is valid.
        try {
            new URL(routeUrlString);
        } catch (MalformedURLException e) {
            throw new MalformedURLException(e.toString());
        }

        // Resolve Strava.App.Link URLs.
        // It's done this way, as there are no 3xx Redirects and HttpConnection doesn't get the location.
        if (routeUrlString.startsWith("https://strava.app.link/") || routeUrlString.startsWith("strava.app.link/")) {
            URL location = new URL(Jsoup.connect(routeUrlString).get().location());
            routeUrlString = location.getProtocol() + "://" + location.getHost() + location.getPath();
        }

        // Apply the MyWindSock Date and Time Query String.
        String errorMessage = "";
        String forecastPostfix = "";
        if (dateTimeString == null) dateTimeString = "";

        dateTimeString = dateTimeString.trim();
        if (!dateTimeString.isEmpty()) {
            try {
                forecastPostfix = convertDateTimeToMyWindSockForecastPostfix(dateTimeString);
            } catch (DateTimeParseException dtpe) {
                forecastPostfix = "";
                errorMessage = "ZonedDateTime format is incorrect. Please use 'dd/MM/yyyy HH:mm z', for example '31/12/2024 23:59 IST'. You supplied '" + dateTimeString + "'.";
            }
        }

        int substringBeginIndex = determineSubstringBeginIndex(routeUrlString);

        // Fix Me: Use URL object here.
        String veloViewerURL = "", myWindSockURL = "";
        String hostname = routeUrlString.substring(substringBeginIndex);

        // Populate VeloViewer and MyWindSock URLs.
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

        return new RouteUrlsRecord(routeUrlString, veloViewerURL, myWindSockURL, errorMessage);
    }

    private String convertDateTimeToMyWindSockForecastPostfix(String dateTimeString) {

        try {
            ZonedDateTime zonedDateTime = ZonedDateTime
                    .from(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm z")
                            .parse(dateTimeString));
            return "/#forecast=" + zonedDateTime.toEpochSecond();
        } catch (DateTimeParseException dtpe) {
            logger.warn("Time is wrong. {}", dtpe.getMessage());
            throw dtpe;
        }
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
