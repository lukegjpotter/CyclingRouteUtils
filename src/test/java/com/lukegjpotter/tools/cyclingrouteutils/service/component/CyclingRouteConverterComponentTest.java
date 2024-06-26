package com.lukegjpotter.tools.cyclingrouteutils.service.component;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteUrlsRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CyclingRouteConverterComponentTest {

    @Autowired
    CyclingRouteConverterComponent cyclingRouteConverter;

    @Test
    public void testConvertRoute_Strava_EmptyDateTime() throws MalformedURLException {
        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "https://www.strava.com/routes/123",
                "https://www.veloviewer.com/routes/123",
                "https://mywindsock.com/route/123",
                "");
        RouteUrlsRecord actualRouteUrls = cyclingRouteConverter.convertRoute("https://www.strava.com/routes/123", "");

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }

    @Test
    public void testConvertRoute_Strava_DateTime() throws MalformedURLException {
        String september6th16h45m = "06/09/2023 16:45 IST";

        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "https://www.strava.com/routes/456",
                "https://www.veloviewer.com/routes/456",
                "https://mywindsock.com/route/456/#forecast=1694015100",
                "");

        RouteUrlsRecord actualRouteUrls = cyclingRouteConverter.convertRoute("https://www.strava.com/routes/456", september6th16h45m);

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }

    @Test
    public void testConvertRoute_RideWithGPS_NullDateTime() throws MalformedURLException {
        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "https://ridewithgps.com/routes/123",
                "",
                "https://mywindsock.com/rwgps/route/123",
                "");
        RouteUrlsRecord actualRouteUrls = cyclingRouteConverter.convertRoute("https://ridewithgps.com/routes/123", null);

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }

    @Test
    public void testConvertRoute_RideWithGPS_DateTime() throws MalformedURLException {
        String september6th16h45m = "06/09/2023 16:45 IST";

        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "https://ridewithgps.com/routes/456",
                "",
                "https://mywindsock.com/rwgps/route/456/#forecast=1694015100",
                "");

        RouteUrlsRecord actualRouteUrls = cyclingRouteConverter.convertRoute("https://ridewithgps.com/routes/456", september6th16h45m);

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }

    @Test
    public void testConvertRoute_EdgeCase_EmptyRoute() throws MalformedURLException {
        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "",
                "",
                "",
                "Route URL is empty.");
        RouteUrlsRecord actualRouteUrls = cyclingRouteConverter.convertRoute("", null);

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }

    @Test
    public void testConvertRoute_EdgeCase_MalformedURL() {
        MalformedURLException exception = assertThrows(
                MalformedURLException.class,
                () -> cyclingRouteConverter.convertRoute("123", null));

        assertEquals("java.net.MalformedURLException: no protocol: 123", exception.getMessage());
    }

    @Test
    public void testConvertRoute_EdgeCase_RouteURLStartsWithWww() throws MalformedURLException {
        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "https://www.strava.com/routes/123",
                "https://www.veloviewer.com/routes/123",
                "https://mywindsock.com/route/123",
                "");
        RouteUrlsRecord actualRouteUrls = cyclingRouteConverter.convertRoute("www.strava.com/routes/123", "");

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }

    @Test
    public void testConvertRoute_EdgeCase_NotStravaOrRideWithGps() throws MalformedURLException {
        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "https://www.facebook.com/",
                "",
                "",
                "URL is not Strava or RideWithGPS.");
        RouteUrlsRecord actualRouteUrls = cyclingRouteConverter.convertRoute("www.facebook.com/", "");

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }
}