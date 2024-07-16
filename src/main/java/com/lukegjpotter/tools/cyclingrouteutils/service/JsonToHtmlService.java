package com.lukegjpotter.tools.cyclingrouteutils.service;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteUrlsRecord;
import org.springframework.stereotype.Service;

@Service
public class JsonToHtmlService {

    public String convertRoute(RouteUrlsRecord routeUrlsRecord) {

        return """
                <html>
                    <head><title>Route URLs</title></head>
                    <body>
                        <a href="https://www.strava.com/routes/123">Strava</a><br />
                        <a href="https://www.veloviewer.com/routes/12">VeloViewer</a><br />
                        <a href="https://mywindsock.com/route/123/#forecast=1694015100">MyWindSock</a>
                    </body>
                </html>""";
    }
}
