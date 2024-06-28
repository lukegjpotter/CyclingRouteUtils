# syntax=docker/dockerfile:1
LABEL authors="lukegjpotter"
#
# Build stage
#
# Use JDK runtime as a parent image. Gradle Deamon still takes time to start.
FROM gradle:8.2.1-jdk17-alpine AS BuildStage
ENV APP_HOME=/app
# Set the working directory to /app.
WORKDIR $APP_HOME
# Copy the Gradle build and Source code files to the Build Stage Container.
COPY . $APP_HOME/
# Build the project with the Image's Gradle, so we don't have to download the Gradle.bin.zip.
RUN gradle clean build

#
# Run stage
#
# Use a JDK runtime as a parent image.
FROM eclipse-temurin:17-jdk-alpine
ENV APP_HOME=/app
# Create a Volume to persist the JAR file.
#VOLUME $APP_HOME
# Copy the Build Stage JAR file to the Run Stage Container Volume.
COPY --from=BuildStage $APP_HOME/build/libs/cycling-route-utils-0.0.1-SNAPSHOT.jar $APP_HOME/cycling-route-utils.jar
# Set the working directory to /app, so we don't need to prefix the CMD Layer with /app.
WORKDIR $APP_HOME
# Expose port 8080
EXPOSE 8080
# Start the Spring Boot app
CMD ["java", "-jar", "cycling-route-utils.jar"]