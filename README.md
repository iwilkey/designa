![logo](web/logo.png)

[![Discord](https://img.shields.io/discord/766121641247375371.svg)](https://discord.gg/ZKfy8P7)

An open-source sandbox tower defense game focused on creating effective systems to acquire and utilize resources.

---

_[Trello Board](https://trello.com/b/KGBrnVi1/designa-trello)_

### Building

Bleeding-edge builds will be generated for every commit. They will be [here](https://github.com/iwilkey/Designa/releases).

If you want to complie the game on your own, follow the instructions below.
First, make sure you have [JDK 8](https://adoptopenjdk.net/) installed. 
Next, open a terminal and `cd` to the Designa folder and run the following commands:

#### Windows

_Running:_ `gradlew desktop:run`
_Building:_ `gradlew desktop:dist`

#### Linux/Mac OS

_Running:_ `./gradlew desktop:run`
_Building:_ `./gradlew desktop:dist`

#### Android

1. Install the Android SDK [here.](https://developer.android.com/studio#downloads) Make sure you're downloading the "Command line tools only", as Android Studio is not required.
2. Set the `ANDROID_HOME` environment variable to point to your unzipped Android SDK directory.
3. Run `gradlew android:assembleDebug` (or `./gradlew` if on linux/mac). This will create an unsigned APK in `android/build/outputs/apk`.

To debug the application on a connected phone, run `gradlew android:installDebug android:run`.

##### Troubleshooting

If the terminal returns `Permission denied` or `Command not found` on Mac/Linux, run `chmod +x ./gradlew` before running `./gradlew`. *This is a one-time procedure.*

Gradle may take up to several minutes to download files. <br>
After building, the output .JAR file should be in `/desktop/build/libs/Designa.jar` for desktop builds.


### Feature/Future Requests

Post feature requests and feedback [here](https://discord.gg/ZKfy8P7).

---

*Oh yeah, Designa Dave says hi I guess :/*

![dave](web/dave.png)

*("Hi!")*
