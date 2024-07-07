package com.lukegjpotter.tools.cyclingrouteutils.controller;

import com.lukegjpotter.tools.cyclingrouteutils.dto.SegmentRecord;
import com.lukegjpotter.tools.cyclingrouteutils.dto.SegmentUrlsRecord;
import com.lukegjpotter.tools.cyclingrouteutils.exceptions.NotSupportedSiteUrlException;
import com.lukegjpotter.tools.cyclingrouteutils.service.SegmentConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CyclingSegmentController {

    private final Logger logger = LoggerFactory.getLogger(CyclingSegmentController.class);
    private final SegmentConverterService segmentConverterService;

    public CyclingSegmentController(SegmentConverterService segmentConverterService) {
        this.segmentConverterService = segmentConverterService;
    }

    @PostMapping("/segment")
    public ResponseEntity<SegmentUrlsRecord> convertSegment(@RequestBody SegmentRecord segmentRecord) {
        logger.info("Endpoint Convert Segment called with {}", segmentRecord);

        try {
            return ResponseEntity.ok(segmentConverterService.convertSegment(segmentRecord));
        } catch (IOException | NotSupportedSiteUrlException e) {
            logger.error("Error converting segment: {}", e.getMessage());

            return ResponseEntity.badRequest().body(new SegmentUrlsRecord(
                    segmentRecord.stravaSegment(),
                    "",
                    e.getMessage()));
        }
    }
}
