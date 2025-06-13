# Cycling Route Utils

[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy?repo=https://github.com/lukegjpotter/CyclingRouteUtils)

A RESTful Service that takes a Strava Segment URL, Strava Route URL, or a RideWithGPS Route URL, and a DateTime, and
returns the VeloViewer URL (Strava only) and MyWindSock URL with the timecode of the DateTime supplied. It even works
with Strava.App.Link links.

### Upcoming Features

Adding Cloud Function/Lambda support.  
Adding Heroku support.  
Date and time for MyWindSock Segments.  
Coming Soon: Heroku, Railways, Amazon Lambda Deloyment Buttons.

### How to Use the Endpoints

Optional: Install JSON to format/pretty print the Response on the CLI.

    sudo npm i -g json

##### Route Endpoint

Curl instructions

    curl -X POST localhost:8080/route \
         -H 'Content-type:application/json' \
         -d '{
                "url": "https://www.strava.com/routes/123",
                "dateTime": "06/09/2025 16:45 Europe/Dublin"
            }' | json

Then it will return

    {
        "sourceRoute": "https://www.strava.com/routes/123",
        "veloViewerRoute": "https://www.veloviewer.com/routes/123",
        "myWindSockRoute": "https://mywindsock.com/route/123/#forecast=1757173500",
        "errorMessage": ""
    }

##### Segment Endpoint

Curl instructions

    curl -X POST localhost:8080/segment \
         -H 'Content-type:application/json' \
         -d '{"stravaSegment": "https://www.strava.com/segments/22191150"}' | json

Then it will return

    {
      "stravaSegment": "https://www.strava.com/segments/22191150",
      "veloViewerSegment": "https://www.veloviewer.com/segments/22191150",
      "errorMessage": ""
    }

##### Route HTML Endpoint

The `/route/html` Endpoint. This will return HTML instead of JSON. The benefit is that, if you execute this Endpoint in
Postman, and choose the "Preview" option for the Response, you can copy and paste the contents straight into a Spread
Sheet for route planning or stage race logistics. In the PostMan Collection below, the "Strava Route HTML" and
"RideWithGPS Route HTML" requests are ready to call the `/route/html` Endpoint and provide some easily
copy-and-paste-able response in Postman's Preview Response pane.

Curl Instructions

    curl -X POST http://localhost:8080/route/html \
         -H 'accept: */*' \
         -H 'Content-Type: application/json' \
         -d '{
                "url": "https://www.strava.com/routes/123",
                "dateTime": "14/07/2025 10:50 Europe/Dublin"
            }'

Then it will return

    <html>
        <head>
            <style>body {font-family: Arial, Helvetica, sans-serif;}</style>
            <title>Route URLs</title>
        </head>
        <body>
            <a href="https://www.strava.com/routes/123">Strava</a><br />
            <a href="https://www.veloviewer.com/routes/123">VeloViewer</a><br />
            <a href="https://mywindsock.com/route/123/#forecast=1752486600">MyWindSock</a>
        </body>
    </html>

#### Postman REST Client

PostMan Instructions

You can use this Collection to run the REST Requests. You can change the hostname and the body contents, to suit your
Cloud instance or Route URL and Time.

Postman
Collection: [Cycling Route Utils](https://www.postman.com/bold-moon-552911/workspace/cyclingrouteutils/collection/3947605-dfff5988-bae7-479c-9a3d-9045ce20eae1?action=share&creator=3947605).

In this Collection, you'll need to set the Environment (top-right where it says "No Environment", click that). The
Collection already has Environments for localhost and the service's live deployment on Render (give it about 50 seconds
to start-up for your first request, so maybe send a request to the `/health` endpoint first, to wake up the
instance, and it'll be rapid response after that). The Collection also has prefilled JSON bodies, to ensure that you
get the correct `ZonedDateTime` format and the Route URL.

#### Swagger UI (SpringDoc-OpenApi)

Swagger Instructions

When the Service is running, you can add `/swagger-ui/index.html` to your host URL and execute requests from there.

Swagger UI: [on Localhost](http://localhost:8080/swagger-ui/index.html)
or [on Render](https://cyclingrouteutils.onrender.com/swagger-ui/index.html)
(give it about 50 seconds to start up).

## How to Build and Run

### Gradle Wrapper CLI

You can build and run this application from the Command Line with the Gradle Wrapper with:

    ./gradlew clean build bootRun

For subsequent runs, where you haven't made Source Code changes, you don't need the `clean` and `build` tasks,
so just `./gradlew bootRun` will do the job.

### Docker

Build the Docker Image with the following Command:

    docker build --pull -t cycling-route-utils:latest .

This command presumes that you are in the directory with this application's Dockerfile.  
You can see the built Image with `docker image ls`.  
Later you can remove the Image with `docker image rm <image id sha>`.

Run the Image as a Docker Container with the following Command:

    docker run --name cycling-route-utils \
      -p 8080:8080 \
      -d --rm cycling-route-utils:latest

You can see the Container running with `docker ps` or `docker container ls`,  
Although, the `run` command has the `--rm` flag set, so the container will remove itself after you stop it, so it will
not be shown in `conatiner ls` after you stop the Container.  
Stop it with `docker stop cycling-route-utils`.

Then you can run the `curl` command, or Postman with Localhost Environment, or the Swagger-UI on Localhost, from the
"How to Use" instructions above.
