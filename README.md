# Cycling Route Utils

A RESTful Service that takes a Strava Route URL, or a RideWithGPS Route URL, and a DateTime, and returns the VeloViewer
URL (Strava Route only) and MyWindSock URL with the timecode of the DateTime supplied.

### Deploy Buttons

Heroku, Railways, Render, Amazon Lambda

### How to Use

Curl instructions & PostMan Instructions

    curl -X POST localhost:8080/convertroute \
         -H 'Content-type:application/json' \
         -d '{"url": "https://www.strava.com/routes/123", "dateTime": "6/9/2023 16:45 BST"}'

Then it will return

    {
      "sourceRoute":"https://www.strava.com/routes/123",
      "veloViewerRoute":"https://www.veloviewer.com/routes/123",
      "myWindSockRoute":"https://mywindsock.com/route/123/#forecast=1694015100"
    }

### How to Build

CLI Gradle & Docker

    ./gradlew build bootRun