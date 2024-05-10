# Cycling Route Utils

A RESTful Service that takes a Strava Route URL, or a RideWithGPS Route URL, and a DateTime, and returns the VeloViewer
URL (Strava Route only) and MyWindSock URL with the timecode of the DateTime supplied.

### Upcoming Features

Adding Cloud Function/Lambda support.  
Adding support for Strava App Link Short URLs.

### Deploy Buttons

Heroku, Railways, Render, Amazon Lambda

### How to Use

Curl instructions & PostMan Instructions

    curl -X POST localhost:8080/route \
         -H 'Content-type:application/json' \
         -d '{"url": "https://www.strava.com/routes/123", "dateTime": "06/09/2023 16:45 IST"}'

Then it will return

    {
      "sourceRoute":"https://www.strava.com/routes/123",
      "veloViewerRoute":"https://www.veloviewer.com/routes/123",
      "myWindSockRoute":"https://mywindsock.com/route/123/#forecast=1694015100"
    }

### How to Build and Run

CLI Gradle & Docker

    ./gradlew build bootRun

Postman
Collection: [for testing on Localhost](https://www.postman.com/bold-moon-552911/workspace/cyclingrouteutils/collection/3947605-dfff5988-bae7-479c-9a3d-9045ce20eae1?action=share&creator=3947605).

Swagger UI: [on Localhost](http://localhost:8080/swagger-ui/index.html).
