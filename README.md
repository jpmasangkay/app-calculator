# 🧮 APP CALCULATOR
[![Ask DeepWiki](https://devin.ai/assets/askdeepwiki.png)](https://deepwiki.com/jpmasangkay/app-calculator)

### Android Calculator App

**Tap. Calculate. Done.**

A clean, native Android calculator app built entirely in Kotlin — delivering a smooth, responsive calculation experience with an intuitive interface designed for everyday use.

[![Kotlin](https://img.shields.io/badge/Kotlin-100%25-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Gradle](https://img.shields.io/badge/Gradle-KTS-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org)
[![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white)](https://developer.android.com/studio)

---

## ✨ Features

### 🔢 Core Calculator

- Perform standard arithmetic operations — **addition**, **subtraction**, **multiplication**, and **division**
- Real-time expression display as you build your calculation
- Instant result on pressing **equals**
- **Clear** and **delete** controls for quick corrections

### 📱 Native Android UI

- Built with **Android Views** and XML layouts for a clean, pixel-perfect interface
- Responsive button grid that adapts to different screen sizes and orientations
- Follows **Material Design** principles for a familiar, polished feel

### ⚡ Lightweight & Fast

- Pure Kotlin — no third-party UI libraries
- Minimal footprint with near-instant launch time
- All logic handled on-device with zero network dependencies

---

## 🛠️ Tech Stack

| Technology | Purpose |
|-----------|---------|
| **Kotlin** | 100% of app logic, UI event handling, and the calculation engine |
| **Android SDK** | Platform APIs, Views, and Activity lifecycle management |
| **Gradle (KTS)** | Build system using Kotlin DSL (`build.gradle.kts`) |
| **Android Studio** | IDE and project configuration (`.idea/` settings) |
| **XML Layouts** | UI design via Android's native View system |

---

## 🚀 Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (latest stable recommended)
- Android SDK with a minimum API level matching `app/build.gradle.kts`
- A physical Android device or an AVD (Android Virtual Device) emulator

### Installation

```bash
# Clone the repository
git clone https://github.com/jpmasangkay/app-calculator.git
cd app-calculator
```

### Running the App

1. Open **Android Studio**
2. Select **File → Open** and choose the `app-calculator` folder
3. Wait for Gradle to sync and index the project
4. Select a connected device or start an **AVD emulator**
5. Click **▶ Run** (or press `Shift + F10`) to build and launch the app

### Building an APK

```bash
# Build a debug APK from the command line
./gradlew assembleDebug
```

The output APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

---

## 📁 Project Structure

```
app-calculator/
├── .idea/                              # Android Studio project settings
├── app/                                # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/                   # Kotlin source files
│   │   │   │   └── com/.../
│   │   │   │       ├── MainActivity.kt         # Entry point — UI logic & button handlers
│   │   │   │       └── Calculator.kt           # Core arithmetic and expression engine
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   └── activity_main.xml       # Calculator UI layout
│   │   │   │   ├── values/
│   │   │   │   │   ├── colors.xml              # Colour palette
│   │   │   │   │   ├── strings.xml             # String resources
│   │   │   │   │   └── themes.xml              # App theme / Material Design tokens
│   │   │   │   └── drawable/                   # Button backgrounds and icons
│   │   │   └── AndroidManifest.xml             # App manifest
│   │   └── test/                               # Unit tests
│   └── build.gradle.kts                        # App-level Gradle build config
├── gradle/
│   └── wrapper/                        # Gradle wrapper files
├── build.gradle.kts                    # Project-level Gradle build config
├── gradle.properties                   # Gradle JVM and project properties
├── settings.gradle.kts                 # Module inclusion settings
├── gradlew                             # Unix Gradle wrapper script
├── gradlew.bat                         # Windows Gradle wrapper script
├── .gitattributes
└── .gitignore
```

---

## 🧠 How It Works

App Calculator uses a straightforward **Activity + logic separation** pattern common in native Android development:

1. **MainActivity** — The single Activity that hosts the calculator UI. It binds click listeners to every button in the layout and delegates input to the calculation engine. The display `TextView` updates after every keypress to reflect the current expression or result.

2. **Calculation Engine** — Parses and evaluates arithmetic expressions as the user builds them. Handles operator precedence, chained operations, and edge cases like division by zero.

3. **XML Layout** — The calculator grid is defined in `activity_main.xml` using a `GridLayout` or `ConstraintLayout`, ensuring buttons are evenly spaced and scale correctly across all screen densities.

4. **Gradle KTS Build** — The project uses Kotlin DSL (`.kts`) for both the project-level and app-level build scripts, keeping configuration type-safe and consistent with the rest of the Kotlin codebase.

---

## 📲 How to Use

1. **Launch the app** on your Android device or emulator
2. **Tap number buttons** to enter values
3. **Tap an operator** (`+`, `-`, `×`, `÷`) to set the operation
4. **Continue entering numbers** to build a full expression
5. **Press `=`** to evaluate and display the result
6. **Press `C`** to clear the display and start over, or **`⌫`** to delete the last character

---
