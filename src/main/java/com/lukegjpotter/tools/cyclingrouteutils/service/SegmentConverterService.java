package com.lukegjpotter.tools.cyclingrouteutils.service;

import com.lukegjpotter.tools.cyclingrouteutils.dto.SegmentRecord;
import com.lukegjpotter.tools.cyclingrouteutils.dto.SegmentUrlsRecord;
import com.lukegjpotter.tools.cyclingrouteutils.exceptions.NotSupportedSiteUrlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class SegmentConverterService {

    private final Logger logger = LoggerFactory.getLogger(SegmentConverterService.class);
    private final UrlNormalisationService urlNormalisationService;

    public SegmentConverterService(UrlNormalisationService urlNormalisationService) {
        this.urlNormalisationService = urlNormalisationService;
    }

    public SegmentUrlsRecord convertSegment(SegmentRecord segmentRecord) throws IOException, NotSupportedSiteUrlException {

        // Ensure it is a correct URL.
        String httpProtocolString = "http://";
        if (segmentRecord.stravaSegment().startsWith(httpProtocolString))
            segmentRecord = new SegmentRecord("https://" + segmentRecord.stravaSegment().substring(httpProtocolString.length()));

        String httpsPrefixedSegmentUrlString = (segmentRecord.stravaSegment().startsWith("https")) ? segmentRecord.stravaSegment() : "https://" + segmentRecord.stravaSegment();

        URL segmentUrl = urlNormalisationService.validUrlCheck(httpsPrefixedSegmentUrlString);

        // Ensure that it is from Strava.
        if (!segmentUrl.getHost().contains("strava.com") && !segmentUrl.getHost().contains("strava.app.link"))
            throw new NotSupportedSiteUrlException("URL is not from Strava.");

        // Resolve Strava.App.Link URLs.
        segmentUrl = urlNormalisationService.resolveStravaAppLink(segmentUrl);

        // Distinguish between the Strava Segment and Strava Route URLs.
        if (!segmentUrl.getPath().startsWith("/segments"))
            throw new NotSupportedSiteUrlException("This Endpoint is for Strava Segments.");

        // Normalise the URL.
        String www = (segmentUrl.getHost().startsWith("www")) ? "" : "www.";
        logger.trace("Setting URL to: {}", segmentUrl.getProtocol() + "://" + www + segmentUrl.getHost() + segmentUrl.getPath());

        segmentUrl = new URL(segmentUrl.getProtocol() + "://" + www + segmentUrl.getHost() + segmentUrl.getPath());

        // Form the VeloViewer URL.
        URL veloViewerUrl = new URL(segmentUrl.getProtocol() + "://www.veloviewer.com" + segmentUrl.getPath());

        return new SegmentUrlsRecord(segmentUrl.toString(), veloViewerUrl.toString(), "");
    }
}
