# Data Classes & Sealed Classes — Полная шпаргалка

## Оглавление
1. [Data Class — основы](#data-class--основы)
2. [Data Class — copy()](#data-class--copy)
3. [Data Class — деструктуризация](#data-class--деструктуризация)
4. [Data Class — ограничения и правила](#data-class--ограничения-и-правила)
5. [Data Class — паттерны в Android](#data-class--паттерны-в-android)
6. [Sealed Class — основы](#sealed-class--основы)
7. [Sealed Class — when и exhaustive check](#sealed-class--when-и-exhaustive-check)
8. [Sealed Class — вложенные данные](#sealed-class--вложенные-данные)
9. [Sealed Interface](#sealed-interface)
10. [Sealed Class — паттерны в Android](#sealed-class--паттерны-в-android)
11. [Enum vs Sealed — когда что](#enum-vs-sealed--когда-что)

---

# Data Class — основы

## Что это?

Класс, предназначенный для **хранения данных**. Kotlin автоматически генерирует:
- `equals()` — сравнение по содержимому полей
- `hashCode()` — хеш на основе полей
- `toString()` — красивый вывод: `User(id=1, name=Kotlin)`
- `copy()` — создание копии с возможностью изменить поля
- `componentN()` — функции для деструктуризации

## Синтаксис

```kotlin
data class User(val id: Int, val name: String)

val user = User(1, "Kotlin")
println(user)  // User(id=1, name=Kotlin)
```

**Обязательные требования:**
- Минимум **один параметр** в primary constructor
- Все параметры конструктора должны быть `val` или `var`
- Не может быть `abstract`, `open`, `sealed` или `inner`

## equals() — сравнение по содержимому

```kotlin
data class User(val id: Int, val name: String)

val user1 = User(1, "Alice")
val user2 = User(1, "Alice")

println(user1 == user2)   // true — одинаковые данные
println(user1 === user2)  // false — разные объекты в памяти
```

Обычный класс:
```kotlin
class Product(val id: Int, val name: String)

val p1 = Product(1, "Phone")
val p2 = Product(1, "Phone")

println(p1 == p2)   // false — обычный класс сравнивает по ссылке!
println(p1 === p2)  // false
```

## hashCode() — одинаковые данные = одинаковый хеш

```kotlin
data class User(val id: Int, val name: String)

val user1 = User(1, "Alice")
val user2 = User(1, "Alice")

println(user1.hashCode() == user2.hashCode())  // true

// Это важно для коллекций:
val set = setOf(user1, user2)
println(set.size)  // 1 — потому что equals + hashCode одинаковые
```

## toString() — красивый вывод

```kotlin
data class User(val id: Int, val name: String)
println(User(1, "Alice"))  // User(id=1, name=Alice)

class Product(val id: Int, val name: String)
println(Product(1, "Phone"))  // objects.Product@4a574795 (адрес в памяти)
```

---

# Data Class — copy()

## Зачем?

Когда нужно создать **копию объекта с изменением отдельных полей**. Это основа иммутабельного подхода: не изменяем объект, а создаём новый.

## Синтаксис

```kotlin
data class User(val id: Int, val name: String, val email: String)

val original = User(1, "Alice", "alice@mail.com")

// Полная копия
val copy1 = original.copy()
println(copy1)  // User(id=1, name=Alice, email=alice@mail.com)

// Копия с изменённым полем
val copy2 = original.copy(name = "Bob")
println(copy2)  // User(id=1, name=Bob, email=alice@mail.com)

// Копия с несколькими изменёнными полями
val copy3 = original.copy(name = "Charlie", email = "charlie@mail.com")
println(copy3)  // User(id=1, name=Charlie, email=charlie@mail.com)

// Оригинал НЕ изменился!
println(original)  // User(id=1, name=Alice, email=alice@mail.com)
```

## Зачем copy(), а не просто создать новый объект?

```kotlin
data class User(val id: Int, val name: String, val email: String, val age: Int, val role: String)

val user = User(1, "Alice", "alice@mail.com", 25, "admin")

// Без copy — нужно указать ВСЕ поля:
val updated = User(1, "Alice", "newemail@mail.com", 25, "admin")  // муторно

// С copy — указываешь только то, что менялось:
val updated2 = user.copy(email = "newemail@mail.com")  // удобно!
```

## Важно: copy() делает ПОВЕРХНОСТНУЮ копию

```kotlin
data class User(val name: String, val tags: MutableList<String>)

val user1 = User("Alice", mutableListOf("admin", "active"))
val user2 = user1.copy()

user2.tags.add("banned")

println(user1.tags)  // [admin, active, banned] — ИЗМЕНИЛСЯ!
println(user2.tags)  // [admin, active, banned]
// Оба указывают на ОДИН и тот же список!
```

**Правило**: используй `val` + неизменяемые типы (`List`, `String`, `Int`) в data class. Избегай `MutableList` в data class.

---

# Data Class — деструктуризация

## componentN() функции

Kotlin автоматически создаёт функции `component1()`, `component2()`, ... для каждого свойства в том порядке, в каком они объявлены в конструкторе.

```kotlin
data class User(val id: Int, val name: String, val email: String)

val user = User(1, "Alice", "alice@mail.com")

// Эти вызовы эквивалентны:
val id = user.component1()    // 1
val name = user.component2()  // "Alice"
val email = user.component3() // "alice@mail.com"

// Но обычно используют деструктуризацию:
val (id2, name2, email2) = user
println("$id2, $name2, $email2")  // 1, Alice, alice@mail.com
```

## Деструктуризация в циклах

```kotlin
data class User(val id: Int, val name: String)

val users = listOf(
    User(1, "Alice"),
    User(2, "Bob"),
    User(3, "Charlie")
)

// Без деструктуризации
for (user in users) {
    println("${user.id}: ${user.name}")
}

// С деструктуризацией — удобнее:
for ((id, name) in users) {
    println("$id: $name")
}
```

## Пропуск ненужных полей

```kotlin
data class User(val id: Int, val name: String, val email: String)

val user = User(1, "Alice", "alice@mail.com")

// Нужно только имя — используем _ для пропуска
val (_, name, _) = user
println(name)  // Alice
```

## Деструктуризация в map

```kotlin
val map = mapOf("key1" to "value1", "key2" to "value2")

// Это уже знакомо из темы коллекций:
for ((key, value) in map) {
    println("$key -> $value")
}
// Работает потому что Map.Entry имеет component1() и component2()
```

---

# Data Class — ограничения и правила

## Что попадает в generated-методы?

**Только свойства из primary constructor!**

```kotlin
data class User(val id: Int, val name: String) {
    var loginCount: Int = 0  // НЕ участвует в equals/hashCode/toString/copy!
}

val user1 = User(1, "Alice")
val user2 = User(1, "Alice")
user1.loginCount = 5
user2.loginCount = 10

println(user1 == user2)      // true!!! loginCount игнорируется
println(user1.toString())    // User(id=1, name=Alice) — loginCount нет
```

## Нельзя наследоваться от data class

```kotlin
data class User(val name: String)

// ОШИБКА КОМПИЛЯЦИИ:
// class Admin(name: String, val level: Int) : User(name)

// Решение — используй обычное наследование или sealed class
```

## Data class может реализовывать интерфейс

```kotlin
interface Printable {
    fun prettyPrint(): String
}

data class User(val id: Int, val name: String) : Printable {
    override fun prettyPrint(): String = "[$id] $name"
}
```

## Можно переопределить generated-методы

```kotlin
data class User(val id: Int, val name: String) {
    // Свой toString
    override fun toString(): String = name
}

println(User(1, "Alice"))  // Alice (а не User(id=1, name=Alice))
```

---

# Data Class — паттерны в Android

## Модели данных из API

```kotlin
data class Article(
    val id: Int,
    val title: String,
    val content: String,
    val author: String,
    val publishedAt: String
)

// Пришёл ответ от API — легко сравнивать, выводить, копировать
val article = Article(1, "Kotlin Guide", "...", "Alice", "2026-01-01")
```

## State в ViewModel (иммутабельный подход)

```kotlin
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

// В ViewModel:
var state = LoginState()

// Пользователь ввёл email:
state = state.copy(email = "user@mail.com")

// Начали загрузку:
state = state.copy(isLoading = true, error = null)

// Ошибка:
state = state.copy(isLoading = false, error = "Wrong password")
```

## RecyclerView — DiffUtil

```kotlin
data class Product(val id: Int, val name: String, val price: Double)

// DiffUtil использует equals() для определения изменений.
// С data class это работает автоматически!
// Без data class пришлось бы писать equals() вручную.
```

---

# Sealed Class — основы

## Что это?

Класс с **ограниченной иерархией наследников**. Все наследники известны на этапе компиляции. Это как `enum`, но каждый вариант может хранить **разные данные**.

## Синтаксис

```kotlin
sealed class Result {
    data class Success(val data: String) : Result()
    data class Error(val message: String, val code: Int) : Result()
    object Loading : Result()
}
```

**Ключевые правила:**
- Все наследники объявляются **в том же файле**
- Наследники могут быть: `data class`, `class`, `object`, другой `sealed class`
- Сам `sealed class` — **абстрактный**, нельзя создать его экземпляр напрямую

## Создание экземпляров

```kotlin
val success = Result.Success("Hello")
val error = Result.Error("Not found", 404)
val loading = Result.Loading  // object — без скобок!
```

## Зачем, если есть enum?

Enum:
```kotlin
enum class Status { LOADING, SUCCESS, ERROR }
// Все варианты одинаковые — просто имена, без данных (или с одинаковыми полями)
```

Sealed class:
```kotlin
sealed class Status {
    object Loading : Status()                          // без данных
    data class Success(val data: List<String>) : Status()  // со списком
    data class Error(val exception: Throwable) : Status()  // с исключением
}
// Каждый вариант хранит СВОИ уникальные данные!
```

---

# Sealed Class — when и exhaustive check

## Главная суперсила sealed class

Компилятор **знает все варианты** и заставляет тебя обработать каждый:

```kotlin
sealed class Result {
    data class Success(val data: String) : Result()
    data class Error(val message: String) : Result()
    object Loading : Result()
}

fun handleResult(result: Result): String {
    return when (result) {
        is Result.Success -> "Data: ${result.data}"
        is Result.Error -> "Error: ${result.message}"
        is Result.Loading -> "Loading..."
        // Не нужен else! Компилятор знает что все варианты покрыты
    }
}
```

## Что если забыть вариант?

```kotlin
fun handleResult(result: Result): String {
    return when (result) {
        is Result.Success -> "Data: ${result.data}"
        is Result.Loading -> "Loading..."
        // ОШИБКА КОМПИЛЯЦИИ: 'when' expression must be exhaustive,
        // add necessary 'is Error' branch or 'else' branch instead
    }
}
```

**Это защита от ошибок!** Если добавишь новый вариант в sealed class — компилятор покажет все места, где его нужно обработать.

## Smart cast внутри when

```kotlin
fun handleResult(result: Result) {
    when (result) {
        is Result.Success -> {
            // Kotlin автоматически кастит result к Result.Success
            println(result.data)  // ← доступ к data без приведения типа!
        }
        is Result.Error -> {
            println(result.message)  // ← доступ к message
        }
        is Result.Loading -> {
            println("Loading...")
        }
    }
}
```

## when без return — нужен else или все ветки

```kotlin
// Как ВЫРАЖЕНИЕ (return/присвоение) — компилятор ТРЕБУЕТ все ветки
val text = when (result) {
    is Result.Success -> "ok"
    is Result.Error -> "fail"
    is Result.Loading -> "wait"
}

// Как STATEMENT (просто вызов) — можно без else, но НЕ рекомендуется
when (result) {
    is Result.Success -> println("ok")
    // Остальные тихо пропущены — ОПАСНО!
}
```

---

# Sealed Class — вложенные данные

## Наследники разных типов

```kotlin
sealed class PaymentResult {
    // data class — когда нужны данные
    data class Success(val transactionId: String, val amount: Double) : PaymentResult()

    // data class — разные данные для разных ошибок
    data class CardError(val cardNumber: String, val reason: String) : PaymentResult()
    data class NetworkError(val retryAfter: Int) : PaymentResult()

    // object — когда данных нет
    object Cancelled : PaymentResult()
}
```

## Sealed class как наследник sealed class

```kotlin
sealed class Screen {
    object Home : Screen()
    object Settings : Screen()

    sealed class Auth : Screen() {
        object Login : Auth()
        object Register : Auth()
        data class ResetPassword(val email: String) : Auth()
    }
}

fun navigate(screen: Screen) {
    when (screen) {
        is Screen.Home -> println("Home")
        is Screen.Settings -> println("Settings")
        is Screen.Auth.Login -> println("Login")
        is Screen.Auth.Register -> println("Register")
        is Screen.Auth.ResetPassword -> println("Reset: ${screen.email}")
    }
}
```

## Sealed class с общими свойствами

```kotlin
sealed class Animal(val name: String) {
    class Dog(name: String, val breed: String) : Animal(name)
    class Cat(name: String, val isIndoor: Boolean) : Animal(name)
    class Fish(name: String, val waterType: String) : Animal(name)
}

fun greet(animal: Animal) {
    // name доступен для всех — это общее свойство
    println("Hello, ${animal.name}!")

    when (animal) {
        is Animal.Dog -> println("Breed: ${animal.breed}")
        is Animal.Cat -> println("Indoor: ${animal.isIndoor}")
        is Animal.Fish -> println("Water: ${animal.waterType}")
    }
}
```

---

# Sealed Interface

## Что это? (Kotlin 1.5+)

То же самое что sealed class, но **интерфейс**. Класс может реализовывать несколько sealed interface.

```kotlin
sealed interface Error {
    data class NetworkError(val code: Int) : Error
    data class DatabaseError(val table: String) : Error
    object Unknown : Error
}

// Используется так же:
fun handleError(error: Error) = when (error) {
    is Error.NetworkError -> "Network: ${error.code}"
    is Error.DatabaseError -> "DB: ${error.table}"
    is Error.Unknown -> "Unknown error"
}
```

## Зачем sealed interface вместо sealed class?

```kotlin
// Класс может реализовывать НЕСКОЛЬКО sealed interface:
sealed interface Error
sealed interface Recoverable

data class NetworkError(val code: Int) : Error, Recoverable
data class FatalError(val reason: String) : Error
object Timeout : Error, Recoverable
```

С sealed class так нельзя — в Kotlin нет множественного наследования классов.

---

# Sealed Class — паттерны в Android

## UI State (самый частый паттерн)

```kotlin
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

// В ViewModel:
class ArticleViewModel : ViewModel() {
    private val _state = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)
    val state: StateFlow<UiState<List<Article>>> = _state

    fun loadArticles() {
        _state.value = UiState.Loading
        try {
            val articles = repository.getArticles()
            _state.value = UiState.Success(articles)
        } catch (e: Exception) {
            _state.value = UiState.Error(e.message ?: "Unknown error")
        }
    }
}

// В Activity/Fragment:
lifecycleScope.launch {
    viewModel.state.collect { state ->
        when (state) {
            is UiState.Loading -> showProgress()
            is UiState.Success -> showArticles(state.data)
            is UiState.Error -> showError(state.message)
        }
    }
}
```

## Navigation Events

```kotlin
sealed class NavigationEvent {
    data class ToDetail(val itemId: Int) : NavigationEvent()
    data class ToProfile(val userId: String) : NavigationEvent()
    object Back : NavigationEvent()
    object ToHome : NavigationEvent()
}
```

## Результат операции

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val error: Throwable) : Result<Nothing>()

    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Failure -> null
    }

    fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(data)
        return this
    }

    fun onFailure(action: (Throwable) -> Unit): Result<T> {
        if (this is Failure) action(error)
        return this
    }
}

// Использование:
val result: Result<User> = repository.getUser(id)
result
    .onSuccess { user -> showUser(user) }
    .onFailure { error -> showError(error.message) }
```

---

# Enum vs Sealed — когда что

## Enum — когда варианты ОДИНАКОВЫЕ

```kotlin
enum class Color { RED, GREEN, BLUE }
enum class Direction { UP, DOWN, LEFT, RIGHT }

// У всех вариантов одинаковые поля:
enum class Planet(val mass: Double, val radius: Double) {
    EARTH(5.97e24, 6.37e6),
    MARS(6.42e23, 3.39e6)
}
```

## Sealed — когда варианты РАЗНЫЕ

```kotlin
sealed class Shape {
    data class Circle(val radius: Double) : Shape()
    data class Rectangle(val width: Double, val height: Double) : Shape()
    object Point : Shape()
}
```

## Таблица сравнения

| | Enum | Sealed Class |
|---|---|---|
| Данные у вариантов | Одинаковые поля | Разные поля у каждого |
| Количество экземпляров | Фиксированное (синглтоны) | Можно создавать много |
| `values()` / `entries` | Есть | Нет |
| `valueOf()` | Есть | Нет |
| `when` exhaustive | Да | Да |
| Наследование | Нет | Да (вложенные sealed) |
| Использование | Статусы, типы, направления | State, Result, события |

---

## Шпаргалка — что запомнить

```
data class  →  хранение данных (equals, toString, copy, componentN)
sealed class  →  ограниченная иерархия (exhaustive when, разные данные)
sealed interface  →  то же, но с множественной реализацией

copy()  →  иммутабельное обновление: state.copy(loading = true)
when + sealed  →  компилятор проверяет все ветки

Android:  data class = модели, state
          sealed class = UiState, Result, Navigation
```
