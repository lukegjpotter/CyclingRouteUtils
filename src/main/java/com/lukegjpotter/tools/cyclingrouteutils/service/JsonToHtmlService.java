package com.lukegjpotter.tools.cyclingrouteutils.service;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteUrlsRecord;
import org.springframework.stereotype.Service;

@Service
public class JsonToHtmlService {

    public String convertRoute(RouteUrlsRecord routeUrlsRecord) {

        return String.format("""
                <html>
                    <head><title>Route URLs</title></head>
                    <body>
                        <a href="%s">Strava</a><br />
                        <a href="%s">VeloViewer</a><br />
                        <a href="%s">MyWindSock</a>
                    </body>
                </html>""", routeUrlsRecord.sourceRoute(), routeUrlsRecord.veloViewerRoute(), routeUrlsRecord.myWindSockRoute());
    }
}
