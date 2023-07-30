package com.lukegjpotter.tools.cyclingrouteutils;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Cycling Route Utils", version = "0.0.1",
		description = "A RESTful Service that takes a Strava Route URL, or a RideWithGPS Route URL, and a DateTime, " +
				"and returns the VeloViewer URL (Strava Route only) and MyWindSock URL with the timecode of the " +
				"DateTime supplied."))
public class CyclingRouteUtilsApplication {

	private static final Logger logger = LoggerFactory.getLogger(CyclingRouteUtilsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CyclingRouteUtilsApplication.class, args);

		logger.info("Application Started");
	}

}
