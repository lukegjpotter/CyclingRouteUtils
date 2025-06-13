# syntax=docker/dockerfile:1
#
# Build stage
#
# Use Gradle runtime as a parent image. Gradle Zip is already on this Image, but the Deamon still takes time to start.
# The version of Gradle should match the version listed in the distributionUrl in the gradle/wrapper/gradle-wrapper.properties.
FROM gradle:8.14-jdk17-alpine AS buildstage
ENV APP_HOME=/app
# Set the working directory to /app.
WORKDIR $APP_HOME
# Copy the Source code files to the Build Stage Container.
COPY . $APP_HOME/
# Build the project with the Image's Gradle.
RUN gradle clean build

#
# Run stage
#
# Use a JDK runtime as a parent image.
FROM eclipse-temurin:17-jdk-alpine AS runstage
ENV APP_HOME=/app
LABEL author="lukegjpotter"
# Copy the Build Stage JAR file to the Run Stage Container.
COPY --from=buildstage $APP_HOME/build/libs/cycling-route-utils-0.0.1-SNAPSHOT.jar $APP_HOME/cycling-route-utils.jar
# Set the working directory to /app, so we don't need to prefix the CMD Layer with /app.
WORKDIR $APP_HOME
# Expose port 8080
EXPOSE 8080
# Start the Spring Boot app
CMD ["java", "-jar", "cycling-route-utils.jar"]