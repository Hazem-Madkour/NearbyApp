# Nearby Application
App to get the venues near you using Foursquare API

# Screenshots
![Portrait screen](https://github.com/Hazem-Madkour/NearbyApp/blob/master/screenshots/portrait_screen.png)
![Landscape screen](https://github.com/Hazem-Madkour/NearbyApp/blob/master/screenshots/land_screen.png) 

# Description
This application
* Gets Venues near you.
* Store Venues offline to see it while you're offline.
* Updated every time app opened or after you change your location by 500 meters.

# Foursquare API
Nearby app uses Foursquare api to get the venues online, you can check it here [Foursquare API](https://developer.foursquare.com)

# Notes
In build.grade in the project level you will need to add your ClientId and ClientSecret for your Foursquare App.
```
ext {
    androidSupportVersion = "26.0.0"
    fourSquareClientId = "\"???\""
    fourSquareClientSecret = "\"???\""
}
```

# Copyright
Copyright Hazem Madkour
