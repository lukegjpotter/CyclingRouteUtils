package com.lukegjpotter.tools.cyclingrouteutils.service.component;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteUrlsRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CyclingRouteConverterComponentTest {

    @Autowired
    CyclingRouteConverterComponent cyclingRouteConverter;

    @Test
    public void testConvertRoute_Strava_EmptyDateTime() {
        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "https://www.strava.com/routes/123",
                "https://www.veloviewer.com/routes/123",
                "https://mywindsock.com/route/123");
        RouteUrlsRecord actualRouteUrls = cyclingRouteConverter.convertRoute("https://www.strava.com/routes/123", "");

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }

    @Test
    public void testConvertRoute_Strava_DateTime() {
        String september6th16h45m = "06/09/2023 16:45 IST";

        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "https://www.strava.com/routes/456",
                "https://www.veloviewer.com/routes/456",
                "https://mywindsock.com/route/456/#forecast=1694015100");

        RouteUrlsRecord actualRouteUrls = cyclingRouteConverter.convertRoute("https://www.strava.com/routes/456", september6th16h45m);

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }

    @Test
    public void testConvertRoute_RideWithGPS_NullDateTime() {
        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "https://ridewithgps.com/routes/123",
                "",
                "https://mywindsock.com/rwgps/route/123");
        RouteUrlsRecord actualRouteUrls = cyclingRouteConverter.convertRoute("https://ridewithgps.com/routes/123", null);

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }

    @Test
    public void testConvertRoute_RideWithGPS_DateTime() {
        String september6th16h45m = "06/09/2023 16:45 IST";

        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "https://ridewithgps.com/routes/456",
                "",
                "https://mywindsock.com/rwgps/route/456/#forecast=1694015100");

        RouteUrlsRecord actualRouteUrls = cyclingRouteConverter.convertRoute("https://ridewithgps.com/routes/456", september6th16h45m);

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }

    @Test
    public void testConvertRoute_EdgeCase_EmptyRoute() {
        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "",
                "",
                "");
        RouteUrlsRecord actualRouteUrls = cyclingRouteConverter.convertRoute("", null);

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }

    @Test
    public void testConvertRoute_EdgeCase_MalformedURL() {
        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "123",
                "",
                "");
        RouteUrlsRecord actualRouteUrls = cyclingRouteConverter.convertRoute("123", null);

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }

    @Test
    public void testConvertRoute_EdgeCase_RouteURLStartsWithWww() {
        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "https://www.strava.com/routes/123",
                "https://www.veloviewer.com/routes/123",
                "https://mywindsock.com/route/123");
        RouteUrlsRecord actualRouteUrls = cyclingRouteConverter.convertRoute("www.strava.com/routes/123", "");

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }
}