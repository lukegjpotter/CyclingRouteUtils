package com.lukegjpotter.tools.cyclingrouteutils.dto;

import java.time.LocalDateTime;

public record RouteAndDateTimeRecord(
        String url,
        LocalDateTime dateTime) {
}
