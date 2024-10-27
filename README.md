# OpenWeather

## About:

A simple Android weather app that utilizes:
+ [OpenWeather API](https://openweathermap.org/api) to retrieve weather data
+ [Firebase Authentication](https://firebase.google.com/docs/auth) for the user sign-in and sign-up.
+ [Android Jetpack](https://developer.android.com/jetpack) libraries like:
    + Compose UI
    + Hilt Dependency Injection
    + Material Design Components
    + Room Database
    + Startup
+ [Retrofit](https://github.com/square/retrofit)
+ Kotlin
+ Android MVVM with Clean Architecture
+ Coroutines
+ Google Location

## Features:

+ Fetch weather information of a selected city/location
    + Search location by city name
    + Use device&apos;s location
+ Display weather conditions, temperature, and other details of a selected city/location
    + First Tab: Display the current weather data
    + Second Tab: Display list of data fetched for the selected city/location
+ User sign-in and sign-up using Google or email/password authentication with Firebase

## Setting up the project:

### Application configurations:

The project uses `appconfig.properties` which holds the API keys and `keystore.properties` for the project&apos;s keystore configurations. Configurations are formatted in `BUILD_VARIANT.KEY=VALUE`.


Create the `appconfig.properties` and `keystore.properties` files inside the project directory to setup the configurations. Examples can be seen in [appconfig.properties.example](/appconfig.properties.example) which holds the API keys and [keystore.properties.example](/keystore.properties.example) for the keystore configurations.

#### /appconfig.properties:

```
release.googleOauthServerClientId=GOOGLE_OAUTH_SERVER_CLIENT_ID
debug.googleOauthServerClientId=GOOGLE_OAUTH_SERVER_CLIENT_ID

release.openWeatherApiKey=OPEN_WEATHER_APP_ID
debug.openWeatherApiKey=OPEN_WEATHER_APP_ID
```

#### /keystore.properties:

```
release.keyAlias=KEY_ALIAS
release.keyPassword=KEY_PASSWORD
release.storeFile=DIRECTORY\\STORE_FILE.JKS
release.storePassword=STORE_PASSWORD

debug.keyAlias=KEY_ALIAS
debug.keyPassword=KEY_PASSWORD
debug.storeFile=DIRECTORY\\STORE_FILE.JKS
debug.storePassword=STORE_PASSWORD
```

---

### OpenWeather API configurations:

Signup and get your own OpenWeather API key here: [OpenWeatherAPI](https://openweathermap.org/api), and select a plan for the _**[Current weather and forecast](https://openweathermap.org/price)**_ subscription.

Then place your API Key into your `/appconfig.properties:`

#### /appconfig.properties:

```
release.openWeatherApiKey=OPEN_WEATHER_API_KEY
debug.openWeatherApiKey=OPEN_WEATHER_API_KEY
```

---
### Google Firebase Authentication configurations:

[Sign into Firebase](https://console.firebase.google.com/) using your Google account and create your own firebase project.

Add an Android App and set the package name to the application&apos;s package name (`com.nielaclag.openweather`), and set the `Signing Certificate SHA-1` to the application&apos;s SHA-1 certificate. Please refer to this link for [generating the signing information](https://developers.google.com/android/guides/client-auth#using_gradles_signing_report) for each of your app&apos;s variants.

After setting up the Firebase Android App, download and put the `google-services.json` into your Android Project&apos;s `/app` directory.


In your Firebase project, make sure to add the `Email/Password` and `Google` in your **Sign-in Providers** for both Email/Password and Google Sign-In to work.
Get your `Web client ID` from your `Google` Sign-in Provider then place it into your `/appconfig.properties:`

#### /appconfig.properties:

```
release.googleOauthServerClientId=GOOGLE_OAUTH_SERVER_CLIENT_ID
debug.googleOauthServerClientId=GOOGLE_OAUTH_SERVER_CLIENT_ID
```

Please refer to the docs for more info:

+ [Add Firebase to your Android project](https://firebase.google.com/docs/android/setup)
+ [Authenticate with Google on Android](https://firebase.google.com/docs/auth/android/google-signin)
+ [Authenticate with Firebase using Password-Based Accounts on Android](https://firebase.google.com/docs/auth/android/password-auth)