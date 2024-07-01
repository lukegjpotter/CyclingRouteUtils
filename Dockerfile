# syntax=docker/dockerfile:1
#
# Build stage
#
# Use JDK runtime as a parent image. Gradle Zip and Deamon still takes time to download and start.
FROM eclipse-temurin:17-jdk-alpine AS buildstage
ENV APP_HOME=/app
# Create a Volume to persist the JAR file.
VOLUME $APP_HOME
# Set the working directory to /app.
WORKDIR $APP_HOME
# Copy the Gradle build and Source code files to the Build Stage Container.
COPY . $APP_HOME/
# Build the project with the Repository's Gradle.
CMD ["./gradlew", "clean", "build"]
CMD ["ls -l", "$APP_HOME"]

#
# Run stage
#
# Use a JDK runtime as a parent image.
FROM eclipse-temurin:17-jdk-alpine AS runstage
ENV APP_HOME=/app
LABEL author="lukegjpotter"
CMD ["ls -l", "$APP_HOME"]
# Create a Volume to persist the JAR file.
VOLUME $APP_HOME
CMD ["ls -l", "$APP_HOME"]
# Copy the Build Stage JAR file to the Run Stage Container Volume.
COPY --from=buildstage $APP_HOME/build/libs/cycling-route-utils-0.0.1-SNAPSHOT.jar $APP_HOME/cycling-route-utils.jar
# Set the working directory to /app, so we don't need to prefix the CMD Layer with /app.
WORKDIR $APP_HOME
# Expose port 8080
EXPOSE 8080
# Start the Spring Boot app
CMD ["java", "-jar", "cycling-route-utils.jar"]