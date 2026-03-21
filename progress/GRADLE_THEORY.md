# Теория: Gradle

## Что это такое

Gradle — система сборки проекта. Ты описываешь **что** нужно (зависимости, версии, настройки), Gradle **делает**: скачивает библиотеки, компилирует код, собирает итоговый файл.

Используется во всех Android проектах и большинстве Kotlin/Java проектов.

---

## Ключевые файлы

### `settings.gradle.kts`
Имя проекта и список модулей.

```kotlin
rootProject.name = "KotlinRepetition"

// если несколько модулей:
include(":app", ":feature-login", ":core")
```

### `build.gradle.kts`
Главный файл сборки. Содержит всё: плагины, зависимости, настройки.

```kotlin
plugins {
    kotlin("jvm") version "1.9.22"   // используем Kotlin
}

repositories {
    mavenCentral()   // откуда скачивать библиотеки
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}
```

---

## Блоки build.gradle.kts

### `plugins { }`
Что используем для сборки:
```kotlin
plugins {
    kotlin("jvm") version "1.9.22"        // Kotlin консольный проект
    kotlin("android") version "1.9.22"    // Kotlin Android
    id("com.android.application")          // Android приложение
    id("com.android.library")              // Android библиотека
}
```

### `repositories { }`
Откуда Gradle скачивает библиотеки:
```kotlin
repositories {
    mavenCentral()   // главный репозиторий (JVM мир)
    google()         // репозиторий Google (Android библиотеки)
}
```

### `dependencies { }`
Сторонние библиотеки которые использует проект:
```kotlin
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    testImplementation("junit:junit:4.13.2")
}
```

**Конфигурации зависимостей:**
| Конфигурация | Когда использовать |
|---|---|
| `implementation` | обычная зависимость (основной код) |
| `testImplementation` | только для тестов |
| `debugImplementation` | только для debug сборки (Android) |
| `api` | зависимость доступна и модулям которые подключают этот модуль |

### `kotlin { }`
Настройки Kotlin:
```kotlin
kotlin {
    jvmToolchain(17)   // версия JVM
}
```

### `sourceSets { }`
Где лежат исходники (если нестандартная структура):
```kotlin
sourceSets {
    main {
        kotlin.srcDirs("src")   // наши исходники в src/ вместо src/main/kotlin/
    }
}
```

---

## Gradle Wrapper

`gradlew` (или `gradlew.bat` на Windows) — обёртка вокруг Gradle.

**Зачем нужна**: позволяет проекту использовать конкретную версию Gradle без глобальной установки. Каждый разработчик в команде получает одинаковую версию.

```
gradle/
  wrapper/
    gradle-wrapper.jar         // сам wrapper
    gradle-wrapper.properties  // версия Gradle
gradlew        // скрипт для Linux/Mac
gradlew.bat    // скрипт для Windows
```

**`gradle-wrapper.properties`:**
```properties
distributionUrl=https://services.gradle.org/distributions/gradle-8.5-bin.zip
```

Запускать всегда через wrapper, не через глобальный gradle:
```bash
./gradlew build       # Linux/Mac
gradlew.bat build     # Windows
```

---

## Стандартная структура Gradle проекта

```
project/
  src/
    main/
      kotlin/       # исходный код
      resources/    # ресурсы
    test/
      kotlin/       # тесты
  build.gradle.kts
  settings.gradle.kts
  gradlew
  gradlew.bat
```

В нашем проекте `src/` используется напрямую (настроено через `sourceSets`).

---

## .kts vs .gradle

| | `.gradle` | `.gradle.kts` |
|---|---|---|
| Язык | Groovy | Kotlin |
| Статус | старый формат | современный формат |
| IDE поддержка | слабее | полная (автодополнение, проверка типов) |
| Android проекты | старые проекты | новые проекты |

---

## Формат зависимости

```kotlin
implementation("group:artifact:version")
//              ^     ^        ^
//              org   имя      версия
```

Пример:
```kotlin
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
//              org.jetbrains.kotlinx  = группа (организация)
//              kotlinx-coroutines-core = артефакт (конкретная библиотека)
//              1.7.3                   = версия
```

---

## В Android проекте

Android проект имеет два `build.gradle.kts`:
- **корневой** (`/build.gradle.kts`) — общие настройки для всего проекта
- **модульный** (`/app/build.gradle.kts`) — настройки конкретного модуля

```kotlin
// app/build.gradle.kts
android {
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
        }
    }
}
```

---

## Частые команды

```bash
./gradlew build          # собрать проект
./gradlew run            # запустить (если есть mainClass)
./gradlew test           # запустить тесты
./gradlew clean          # очистить build/
./gradlew dependencies   # показать дерево зависимостей
```
