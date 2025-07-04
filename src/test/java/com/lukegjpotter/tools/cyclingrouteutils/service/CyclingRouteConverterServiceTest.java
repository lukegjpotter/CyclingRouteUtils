package com.lukegjpotter.tools.cyclingrouteutils.service;

import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteAndDateTimeRecord;
import com.lukegjpotter.tools.cyclingrouteutils.dto.RouteUrlsRecord;
import com.lukegjpotter.tools.cyclingrouteutils.service.component.CyclingRouteConverterComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CyclingRouteConverterServiceTest {

    @InjectMocks
    CyclingRouteConverterService converterService;
    @Mock
    CyclingRouteConverterComponent cyclingRouteConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        converterService = new CyclingRouteConverterService(cyclingRouteConverter);
    }

    @Test
    public void testConvertRoute_Strava_NoDateTime() throws IOException {
        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "https://www.strava.com/routes/123",
                "https://www.veloviewer.com/routes/123",
                "https://mywindsock.com/route/123",
                "");

        Mockito.when(cyclingRouteConverter.convertRoute("https://www.strava.com/routes/123", null)).thenReturn(expectedRouteUrls);

        RouteUrlsRecord actualRouteUrls = converterService.convertRoute(new RouteAndDateTimeRecord("https://www.strava.com/routes/123", null));

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }

    @Test
    public void testConvertRoute_Strava_DateTime() throws IOException {
        String september6th16h45m = "06/09/2023 16:45 Europe/Dublin";

        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "https://www.strava.com/routes/456",
                "https://www.veloviewer.com/routes/456",
                "https://mywindsock.com/route/456/#forecast=1694015100",
                "");

        Mockito.when(cyclingRouteConverter.convertRoute("https://www.strava.com/routes/456", september6th16h45m)).thenReturn(expectedRouteUrls);

        RouteUrlsRecord actualRouteUrls = converterService.convertRoute(new RouteAndDateTimeRecord("https://www.strava.com/routes/456", september6th16h45m));

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }

    @Test
    public void testConvertRoute_RideWithGPS_NoDateTime() throws IOException {
        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "https://ridewithgps.com/routes/123",
                "",
                "https://mywindsock.com/rwgps/route/123",
                "");

        Mockito.when(cyclingRouteConverter.convertRoute("https://ridewithgps.com/routes/123", null)).thenReturn(expectedRouteUrls);

        RouteUrlsRecord actualRouteUrls = converterService.convertRoute(new RouteAndDateTimeRecord("https://ridewithgps.com/routes/123", null));

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }

    @Test
    public void testConvertRoute_RideWithGPS_DateTime() throws IOException {
        String september6th16h45m = "06/09/2023 16:45 Europe/Dublin";

        RouteUrlsRecord expectedRouteUrls = new RouteUrlsRecord(
                "https://ridewithgps.com/routes/456",
                "",
                "https://mywindsock.com/rwgps/route/456/#forecast=1694015100",
                "");

        Mockito.when(cyclingRouteConverter.convertRoute("https://ridewithgps.com/routes/456", september6th16h45m)).thenReturn(expectedRouteUrls);

        RouteUrlsRecord actualRouteUrls = converterService.convertRoute(new RouteAndDateTimeRecord("https://ridewithgps.com/routes/456", september6th16h45m));

        assertEquals(expectedRouteUrls, actualRouteUrls);
    }
}