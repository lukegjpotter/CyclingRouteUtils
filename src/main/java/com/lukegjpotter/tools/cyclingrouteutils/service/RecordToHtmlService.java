package com.lukegjpotter.tools.cyclingrouteutils.service;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteUrlsRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RecordToHtmlService {

    private final Logger logger = LoggerFactory.getLogger(RecordToHtmlService.class);

    public String convertRoute(RouteUrlsRecord routeUrlsRecord) {

        return String.format("""
                <html>
                    <head>
                        <style>body {font-family: Arial, Helvetica, sans-serif;}</style>
                        <title>Route URLs</title>
                    </head>
                    <body>
                        <a href="%s">Strava</a><br />
                        <a href="%s">VeloViewer</a><br />
                        <a href="%s">MyWindSock</a>
                    </body>
                </html>""", routeUrlsRecord.sourceRoute(), routeUrlsRecord.veloViewerRoute(), routeUrlsRecord.myWindSockRoute());
    }
}
