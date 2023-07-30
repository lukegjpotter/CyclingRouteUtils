package com.lukegjpotter.tools.cyclingrouteutils.dto;

import java.util.Objects;

public record RouteUrlsRecord(
        String sourceRoute,
        String veloViewerRoute,
        String myWindSockRoute
) {
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof RouteUrlsRecord routeUrls))
            return false;

        return Objects.equals(this.sourceRoute, routeUrls.sourceRoute)
                && Objects.equals(this.veloViewerRoute, routeUrls.veloViewerRoute)
                && Objects.equals(this.myWindSockRoute, routeUrls.myWindSockRoute);
    }
}
