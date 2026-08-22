# DanialFood

DanialFood is a simple Android food-listing app built with Kotlin. It demonstrates a clean MVP-style structure, local persistence with Room, and modern Android UI patterns using RecyclerView and ViewBinding.

## Features

- Browse a list of food items with image, city, price, distance, and rating
- Search foods by name in real time
- Add a new food item
- Edit an existing food item
- Delete a food item with confirmation
- Delete all food items
- Seed initial sample data on first run
- Persist data locally with Room database

## Tech Stack

- **Language:** Kotlin
- **UI:** XML layouts, ViewBinding, RecyclerView
- **Architecture:** MVP (Contract + Presenter + View)
- **Database:** Room
- **Image Loading:** Glide + glide-transformations
- **Build System:** Gradle (Android Application plugin)

## Requirements

- Android Studio (recommended: latest stable)
- JDK 8+
- Android SDK:
  - compileSdk: 32
  - minSdk: 21
  - targetSdk: 32

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/danialtavakoli/DanialFood.git
   ```
2. Open the project in Android Studio.
3. Let Gradle sync finish.
4. Run the app on an emulator or Android device.

## Build & Run from Command Line

```bash
./gradlew assembleDebug
```

The generated APK will be available under:

`app/build/outputs/apk/debug/`

## Project Structure

```text
app/src/main/java/com/danialtavakoli/danialfood/
├── mainScreen/
│   ├── MainScreenActivity.kt
│   ├── MainScreenContract.kt
│   ├── MainScreenPresenter.kt
│   └── FoodAdapter.kt
├── model/
│   ├── Food.kt
│   ├── FoodDao.kt
│   └── MyDatabase.kt
└── utils/
    ├── BaseContract.kt
    ├── Constants.kt
    └── Extensions.kt
```

## Notes

- The app inserts a predefined list of foods on first launch.
- Room is currently configured with `allowMainThreadQueries()` for simplicity in this sample project.

## License

This project is available for learning and demonstration purposes.
