# 🎬 MyMovies - Multi-Module Android App

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)
![Clean Architecture](https://img.shields.io/badge/Clean%20Architecture-%20%20MVI-orange?style=for-the-badge)

A modern Android application built to preview movies, view details, and search for content using **The Movie Database (TMDb) API**. The project is developed using **Kotlin**, **Jetpack Compose**, and follows **Clean Architecture** with the **MVI (Model-View-Intent)** pattern, demonstrating best practices in modularity, caching, and state management.

---

## 📱 Screenshots

| Home (Shimmer Effect) | Home (Movies List) | Movie Details
|:---:|:---:|:---:|
| <img width="250" alt="image" src="https://github.com/user-attachments/assets/c6d16af6-5934-4447-92d2-652aec8e1d3f" /> | <img alt="image" src="https://github.com/user-attachments/assets/aedfe5ab-4ff2-4cf3-9096-9f88e113dbb2" width="250"/> | <img alt="image" src="https://github.com/user-attachments/assets/cfb0d9ba-b231-4333-bbff-745a07f6e6af" width="250"/> |

---

| Search (Animation Effect) | Real-time Search | Wrong Title BG
|:---:|:---:|:---:|
| <img width="250" alt="image" src="https://github.com/user-attachments/assets/1ffbbd13-3bb9-4a12-9cda-90a16fc99ac3" /> | <img alt="image" src="https://github.com/user-attachments/assets/83bc4c68-cdfe-411b-9161-c406a35aff81" width="250"/> | <img alt="image" src="https://github.com/user-attachments/assets/51c2dda2-fc60-4c26-933a-b6311c9460a4" width="250"/> |

---

## ✨ Features

Based on the project requirements:

* **🎥 Browse Movies:** Displays a paginated list of movies
* **🔍 Real-time Search:** Search for movies by title with instant results as you type.
* **📄 Movie Details:** View comprehensive info (synopsis, rating, release year, etc).
* **⚡ Offline First (Caching):**
    * Automatically caches data locally using **Room**.
    * Displays cached content immediately while fetching fresh data in the background.
* **🛡️ Content Filtering:** Implements strict content filtering (**PG-13, No Adult content**).
* **🎨 Material Design 3:** Modern, responsive UI with Dark/Light theme support.

---

## 🛠️ Tech Stack & Libraries

This project uses the latest modern Android development tools:

| Category | Library/Tool |
| :--- | :--- |
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose (Material 3) |
| **Architecture** | Clean Architecture + MVI |
| **DI** | Koin |
| **Network** | Ktor |
| **Local Database** | Room Database |
| **Async** | Coroutines & Flow |
| **Pagination** | Paging 3 (Remote Mediator) |
| **Image Loading** | Coil |
| **Testing** | JUnit4, Mockk, Turbine, Compose UI Test |
| **Build System** | Gradle (Version Catalogs - TOML) |
| **Version Control** | Git Flow Workflow |

---

## ⚙️ Setup & Installation:

To run this project locally, follow these steps:

## 1. Prerequisites:

* Android Studio Ladybug (or newer).
* JDK 17 or higher.
---

## 2. Clone the Repository:

```bash
git clone [https://github.com/YOUR_USERNAME/MyMovies.git](https://github.com/YOUR_USERNAME/MyMovies.git)
cd MyMovies
```
---

## 3. API Key Configuration 🔑:

This project requires a TMDb API Key.

Register at themoviedb.org.

Request an API Key.

Create a local.properties file in the root directory (if not exists).

Add your **key and token** as follows:

**Properties**
```bash

sdk.dir=/path/to/your/android/sdk
API_KEY="your_api_key_here"
API_ACCESS_TOKEN="your_access_token_here"

```
---

## 4. Build and Run:

Sync Project with Gradle Files.

Select the app configuration.

Run on an Emulator or Physical Device.
---
## 🧪 Testing:

The project includes comprehensive testing strategies:

Unit Tests: Covers ViewModels (MVI logic), Repositories using Mockk and Turbine.

UI Tests: Smoke tests to verify app launch and list rendering using Compose UI Test.

To run tests via terminal:

```bash

./gradlew testDebugUnitTest  # Run Unit Tests
./gradlew connectedDebugAndroidTest # Run UI Tests

```
---
## ✅ API Compliance

As per the task requirements, all API requests strictly enforce the following filters to ensure appropriate content:
```bash
include_adult=false

certification_country=US

certification.lte=PG-13
```
---
## 🤝 Git Flow

This project utilizes the Git Flow branching model:

```bash main```: Production-ready code.

```bash develop```: Integration branch for features.

```bash feature/*```: Individual features (e.g., feature/caching, feature/search).

```bash release/*```: Preparation for a new production release.

---
<div align="center">
    <p>Made with ❤️ by <a href="https://github.com/MohammadAshrf">Mohammad Ashrf</a></p>
    <p>Happy exploring! 🚀</p>
</div>
