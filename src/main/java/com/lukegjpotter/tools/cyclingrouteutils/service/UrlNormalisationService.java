package com.lukegjpotter.tools.cyclingrouteutils.service;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class UrlNormalisationService {

    private final Logger logger = LoggerFactory.getLogger(UrlNormalisationService.class);

    public URL normaliseUrlString(final String urlString) {
        // ToDo: Do URL sanitizing here. Refactor common code with SegmentConverterService and CyclingRouteConverterComponent.
        return null;
    }

    public URL validUrlCheck(String urlString) throws MalformedURLException {
        try {
            return new URL(urlString);
        } catch (MalformedURLException mue) {
            logger.error("Error Converting Route. {}", mue.getMessage());
            throw new MalformedURLException("The URL is not correctly formed");
        }
    }

    public URL resolveStravaAppLink(URL routeUrl) throws IOException {
        // It's done this way, as there are no 3xx Redirects and HttpConnection doesn't get the location.
        if (routeUrl.getHost().contains("strava.app.link")) {
            URL locationUrl = new URL(Jsoup.connect(routeUrl.toString()).get().location());
            return new URL(locationUrl.getProtocol() + "://" + locationUrl.getHost() + locationUrl.getPath());
        }

        return routeUrl;
    }
}
