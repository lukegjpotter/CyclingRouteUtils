# Cycling Route Utils

A RESTful Service that takes a Strava Segment URL, Strava Route URL, or a RideWithGPS Route URL, and a DateTime, and
returns the VeloViewer URL (Strava only) and MyWindSock URL with the timecode of the DateTime supplied. It even works
with Strava.App.Link links.

### Upcoming Features

Adding Cloud Function/Lambda support.
Adding Heroku support.

### Deploy Buttons

[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy?repo=https://github.com/lukegjpotter/CyclingRouteUtils)

Coming Soon: Heroku, Railways, Amazon Lambda

### How to Use

#### Command Line

Optional: Install JSON to format/pretty print the Response.

    sudo npm i -g json

#### Route Endpoint

Curl instructions

    curl -X POST localhost:8080/route \
         -H 'Content-type:application/json' \
         -d '{"url": "https://www.strava.com/routes/123", "dateTime": "06/09/2023 16:45 Europe/Dublin"}' | json

Then it will return

    {
      "sourceRoute":"https://www.strava.com/routes/123",
      "veloViewerRoute":"https://www.veloviewer.com/routes/123",
      "myWindSockRoute":"https://mywindsock.com/route/123/#forecast=1694015100"
      "error":""
    }

#### Segment Endpoint

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

#### Postman REST Client

PostMan Instructions

You can use this Collection to run the REST Requests. You can change the hostname and the body contents, to suit your
Cloud instance or Route URL and Time.

Postman Collection: [prefilled JSON bodies](https://www.postman.com/bold-moon-552911/workspace/cyclingrouteutils/collection/3947605-dfff5988-bae7-479c-9a3d-9045ce20eae1?action=share&creator=3947605).

In this Collection, you'll need to set the Environment (top-right where it says "No Environment", click that). The Collection already has Environments for localhost and the service's live deployment on Render (give it about 50 seconds to start-up for your first request, so maybe just send a request to the `/health` endpoint first, to wake up the instance, and it'll be rapid response after that). The Collection also has prefilled JSON bodies, to ensure that you get the correct `ZonedDateTime` format and the Route URL.

#### Swagger UI (SpringDoc-OpenApi)

Swagger Instructions

When the Service is running, you can add `/swagger-ui/index.html` to your host URL and execute requests from there.

Swagger UI: [on Localhost](http://localhost:8080/swagger-ui/index.html)
or [on Render](https://cyclingrouteutils.onrender.com/swagger-ui/index.html)
(give it about 50 seconds to start up).

### How to Build and Run

CLI Gradle

    ./gradlew build bootRun

Docker CLI Instructions

    docker build --pull -t cycling-route-utils:latest .
    
    docker run --name cycling_route_utils \
      -p 8080:8080 \
      -d --rm cycling-route-utils:latest

Then you can run the `curl` command, or Postman with Localhost Environment, or the Swagger-UI on Localhost, from the "How to Use" instructions above.
