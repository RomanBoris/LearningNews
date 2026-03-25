# Задания: Null Safety (12 заданий)

**Проект**: KotlinRepetition
**Файл**: `src/nullsafety/NullSafety.kt`
**Теория**: `docs/theory/KOTLIN_NULL_SAFETY_THEORY.md`

---

## 🟢 УРОВЕНЬ 1: Основы (4 задания)

### **1.1 Safe call + Elvis**

Есть функция которая возвращает имя пользователя по id:
```kotlin
fun getUserName(id: Int): String? {
    return when (id) {
        1 -> "Alexander"
        2 -> "maria.johnson"
        3 -> "  Bob  "
        else -> null
    }
}
```

Напиши функцию `fun formatName(id: Int): String` которая:
1. Получает имя через `getUserName(id)`
2. Через safe call делает `trim()` и `uppercase()`
3. Через elvis возвращает `"UNKNOWN"` если id не найден

Вызови с id = 1, 2, 3, 99 и выведи результаты.

---

### **1.2 Smart cast**

Напиши функцию `fun describeValue(value: Any?): String` которая через `when` обрабатывает:
- `null` → `"Получили null"`
- `is String` → `"Строка: '${value}', длина: ${value.length}"` (smart cast — length без `?.`)
- `is Int` → `"Число: $value, квадрат: ${value * value}"` (smart cast — умножение без `?.`)
- `is Boolean` → `"Булево: $value"`
- `is List<*>` → `"Список из ${value.size} элементов"` (smart cast — size без `?.`)
- иначе → `"Неизвестный тип"`

Протестируй: передай `"Hello"`, `42`, `true`, `listOf(1,2,3)`, `null`, `3.14`.

---

### **1.3 !! и когда он уместен**

Есть конфиг приложения — некоторые поля обязательны при старте:
```kotlin
data class AppConfig(
    val apiUrl: String?,
    val apiKey: String?,
    val timeout: Int?
)
```

Напиши функцию `fun initConfig(config: AppConfig): String` которая:
1. Проверяет `apiUrl` — если null, бросает `IllegalStateException("apiUrl обязателен")`  используя elvis + throw
2. Проверяет `apiKey` — если null, бросает `IllegalStateException("apiKey обязателен")` так же
3. Берёт `timeout` через elvis с дефолтом `30`
4. Возвращает строку `"Config OK: $apiUrl, timeout: $timeout"`

Протестируй три сценария:
- Все поля заполнены
- `apiUrl` = null (должно бросить исключение — оберни в try/catch)
- `apiKey` = null (то же)

---

### **1.4 Safe cast as?**

Напиши функцию `fun processInput(input: Any?): String` которая пробует привести `input` к разным типам через `as?`:
1. Пробует привести к `String` — если успешно, возвращает `"String: ${value.uppercase()}"`
2. Пробует привести к `Int` — если успешно, возвращает `"Int: ${value * 2}"`
3. Пробует привести к `List<*>` — если успешно, возвращает `"List с ${value.size} элементами"`
4. Если ничего не подошло — `"Неизвестный тип: ${input}"`

Протестируй с: `"hello"`, `42`, `listOf(1,2,3)`, `3.14`, `null`.

---

## 🟡 УРОВЕНЬ 2: Комбинации (4 задания)

### **2.1 Цепочка safe calls**

Есть структура данных:
```kotlin
data class Country(val name: String, val code: String?)
data class City(val name: String, val country: Country?)
data class Address(val street: String?, val city: City?)
data class Profile(val username: String, val address: Address?)
```

Напиши функцию `fun getCountryCode(profile: Profile?): String` которая достаёт `country.code` через цепочку safe calls и возвращает его, либо `"Неизвестно"` через elvis.

Затем напиши функцию `fun describeProfile(profile: Profile?): String` которая возвращает строку вида:
`"Пользователь: Alex | Город: Moscow | Страна: Russia (RU)"`
или заменяет недостающие части на `"не указано"`.

Протестируй несколько вариантов — с полными данными, с частично null, с null профилем.

---

### **2.2 takeIf + цепочка**

Есть форма регистрации:
```kotlin
data class RegistrationForm(
    val username: String?,
    val email: String?,
    val age: Int?,
    val password: String?
)
```

Напиши функцию `fun validateForm(form: RegistrationForm): String` которая проверяет каждое поле через `takeIf` и возвращает первую найденную ошибку или `"OK"`:
- `username` не null и длина >= 3
- `email` не null и содержит `@`
- `age` не null и от 18 до 120
- `password` не null и длина >= 8

Пример: если username = "Al" → вернуть `"Ошибка: username слишком короткий"`

Протестируй с валидной формой и с несколькими вариантами невалидных данных.

---

### **2.3 lateinit**

Напиши класс `UserRepository` который имитирует репозиторий:

```kotlin
class UserRepository {
    lateinit var database: String        // имитация подключения к БД
    lateinit var currentUser: String     // текущий пользователь

    fun connect(dbName: String) { ... }
    fun login(username: String) { ... }
    fun getUserInfo(): String { ... }    // использует database и currentUser
    fun isConnected(): Boolean { ... }   // проверяет isInitialized
}
```

- `connect()` инициализирует `database`
- `login()` инициализирует `currentUser`
- `getUserInfo()` возвращает `"User: $currentUser @ $database"` — но если не подключены, возвращает ошибку
- `isConnected()` использует `::database.isInitialized` для проверки

Протестируй: вызови `getUserInfo()` до `connect()`, потом после, потом после `login()`.

---

### **2.4 Nullable коллекции**

Есть список пользователей где некоторые данные могут отсутствовать:
```kotlin
data class User(val name: String?, val age: Int?, val email: String?)

val users = listOf(
    User("Alice", 30, "alice@example.com"),
    User(null, 25, "anon@example.com"),
    User("Bob", null, null),
    User("Charlie", 17, "charlie@example.com"),
    User(null, null, null)
)
```

Напиши функции:
1. `fun getValidNames(users: List<User>): List<String>` — имена которые не null (используй `mapNotNull`)
2. `fun getAdults(users: List<User>): List<User>` — пользователи где age не null и >= 18
3. `fun getEmailOrDefault(user: User): String` — email или `"no-reply@example.com"` если null
4. `fun getSummary(users: List<User>): String` — строка вида `"Всего: 5, с именем: 3, взрослых: 2"`

---

## 🔴 УРОВЕНЬ 3: Продвинутое (4 задания)

### **3.1 Обработка ошибок через nullable**

Напиши класс `SafeParser` с методами:
```kotlin
object SafeParser {
    fun parseInt(value: String?): Int?
    fun parseDouble(value: String?): Double?
    fun parseDate(value: String?): String?   // формат "dd.mm.yyyy" — проверь через regex
    fun parseEmail(value: String?): String?  // должен содержать @ и .
}
```

Каждый метод через safe call + `toIntOrNull()`/`toDoubleOrNull()`/`takeIf` возвращает null если парсинг не удался.

Затем напиши функцию `fun parseUserData(raw: Map<String, String?>): String` которая парсит Map с ключами `"age"`, `"salary"`, `"birthdate"`, `"email"` и возвращает отчёт:
```
Age: 25
Salary: 50000.0
Birthdate: 12.05.1999
Email: user@test.com
```
Если поле не спарсилось — `"Age: не указан"` и т.д.

---

### **3.2 Elvis с return/throw (early return паттерн)**

Напиши функцию `fun processOrder(data: Map<String, Any?>): String` которая обрабатывает заказ.

Внутри используй **только** elvis + return/throw для проверок (без if/else):
```kotlin
val userId = data["userId"] as? Int ?: return "Ошибка: userId обязателен"
val productId = data["productId"] as? Int ?: return "Ошибка: productId обязателен"
val quantity = data["quantity"] as? Int ?: 1
val discount = data["discount"] as? Double ?: 0.0
// ... и т.д.
```

Функция должна:
1. Достать и провалидировать `userId`, `productId` (обязательные)
2. `quantity` (дефолт 1), `discount` (дефолт 0.0), `address` (необязательный String)
3. Вернуть строку-резюме заказа

Протестируй: с полными данными, без `userId`, без `productId`, с частичными данными.

---

### **3.3 Nullable + sealed class**

Совмести null safety с sealed классом для обработки результатов:

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

Напиши функцию `fun fetchUser(id: Int): Result<String>` которая:
- id = 1..3 → `Result.Success` с именем
- id < 0 → `Result.Error("Некорректный id")`
- id > 100 → `Result.Error("Пользователь не найден")`
- id = 0 → `Result.Loading`

Напиши функцию `fun handleResult(result: Result<String>): String` которая обрабатывает результат через `when`.

Напиши функцию `fun getDisplayName(id: Int): String` которая:
1. Вызывает `fetchUser(id)`
2. Через `let` обрабатывает только `Success` — достаёт `data`
3. Через elvis или when возвращает строку для отображения

Протестируй с id: -1, 0, 1, 50, 101.

---

### **3.4 Android-style: полная цепочка**

Имитируй типичную Android логику загрузки данных:

```kotlin
data class ApiUser(val id: Int?, val name: String?, val email: String?, val role: String?)
data class DomainUser(val id: Int, val name: String, val email: String, val isAdmin: Boolean)
```

Напиши функцию `fun mapToDomain(apiUser: ApiUser?): DomainUser?` которая конвертирует ApiUser в DomainUser:
- Если `apiUser` null — вернуть null
- Если `id` или `name` null — вернуть null (обязательные поля)
- `email` — если null, использовать `"no-reply@app.com"`
- `isAdmin` — `role == "admin"` (если role null — false)

Напиши функцию `fun processApiResponse(users: List<ApiUser?>): String` которая:
1. Фильтрует null через `filterNotNull()`
2. Маппит через `mapToDomain()` и убирает null через `mapNotNull()`
3. Разделяет на обычных и админов
4. Возвращает строку-отчёт:
```
Всего получено: 6
Валидных пользователей: 4
Обычных: 3, Админов: 1
Админы: [Alice]
```

---

## Прогресс

| Уровень | Задания | Статус |
|---------|---------|--------|
| 1. Основы | 0/4 | ⏸️ |
| 2. Комбинации | 0/4 | ⏸️ |
| 3. Продвинутое | 0/4 | ⏸️ |
| **Итого** | **0/12** | |
