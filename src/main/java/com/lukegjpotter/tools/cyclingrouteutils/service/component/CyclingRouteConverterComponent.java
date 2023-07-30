package com.lukegjpotter.tools.cyclingrouteutils.service.component;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteUrlsRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class CyclingRouteConverterComponent {

    public RouteUrlsRecord convertRoute(String routeUrl, LocalDateTime localDateTime) {

        String veloViewerURL = "", myWindSockURL = "", forecastPostfix = "";
        int substringBeginIndex = 0;

        if (localDateTime != null) forecastPostfix = convertDateTimeToMyWindSockForecastPostfix(localDateTime);

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

        String hostname = routeUrl.substring(substringBeginIndex);
        if (hostname.startsWith("strava.com")) {
            String urlPath = hostname.substring("strava.com/routes/".length());

            // https://www.strava.com/routes/123 -> https://www.veloviewer.com/routes/123
            veloViewerURL = "https://www.veloviewer.com/routes/" + urlPath;

            // https://www.strava.com/routes/123 -> https://mywindsock.com/route/123
            myWindSockURL = "https://mywindsock.com/route/" + urlPath + forecastPostfix;
        } else if (hostname.startsWith("ridewithgps.com")) {
            String urlPath = hostname.substring("ridewithgps.com/routes/".length());
            // VeloViewer is not set for RideWithGPS.
            // https://ridewithgps.com/routes/123 -> https://mywindsock.com/rwgps/route/123
            myWindSockURL = "https://mywindsock.com/rwgps/route/" + urlPath + forecastPostfix;
        }

        return new RouteUrlsRecord(routeUrl, veloViewerURL, myWindSockURL);
    }

    private String convertDateTimeToMyWindSockForecastPostfix(LocalDateTime localDateTime) {
        return "/#forecast=" + localDateTime.toEpochSecond(ZoneOffset.UTC);
    }
}
