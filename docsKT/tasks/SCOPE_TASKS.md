# Задания: Scope функции (10 заданий)

**Проект**: KotlinRepetition
**Файлы**: `src/scope/`
**Теория**: `docs/theory/KOTLIN_SCOPE_THEORY.md`

---

## 🟢 УРОВЕНЬ 1: Основы (3 задания)

### **1.1 apply — инициализация объектов**
Есть классы:
```kotlin
data class Address(var city: String = "", var street: String = "", var zip: String = "")
data class Person(var name: String = "", var age: Int = 0, var address: Address = Address())
```
Создай объект `Person` используя `apply` (без прямого присваивания через `=` после создания).
Вложенный `Address` тоже создай через `apply`.

В main выведи результат.

---

### **1.2 let — null safety**
Есть функция:
```kotlin
fun findUser(id: Int): String? {
    return if (id == 1) "Alex" else null
}
```
Используй `let` чтобы:
1. Если пользователь найден — вывести `"Привет, Alex! Длина имени: 4"`
2. Если не найден — вывести `"Пользователь не найден"`

Протестируй с id=1 и id=99. Используй `?.let { } ?: run { }`.

---

### **1.3 also — логирование**
Есть список чисел `listOf(3, 1, 4, 1, 5, 9, 2, 6)`.

Напиши цепочку операций:
1. `also` — вывести исходный список
2. `filter` — оставить только чётные
3. `also` — вывести после фильтрации
4. `map` — умножить каждый на 10
5. `also` — вывести финальный результат

Всё в одной цепочке без промежуточных переменных.

---

## 🟡 УРОВЕНЬ 2: Комбинации (3 задания)

### **2.1 run — вычисления**
Создай `data class Order(val items: List<String>, val prices: List<Double>, val discount: Double)`.

Используя `run`, вычисли и выведи:
- Сумму без скидки
- Размер скидки в деньгах
- Итоговую сумму
- Вывод: `"Итого: 850.0 (скидка: 150.0)"`

Всё вычисли внутри одного блока `run`, верни строку-результат.

---

### **2.2 with — работа с объектом**
Создай `data class Config(val host: String, val port: Int, val dbName: String, val maxConnections: Int)`.

Используя `with`, выведи полную информацию о конфиге в виде:
```
=== Database Config ===
Host: localhost:5432
Database: mydb
Max connections: 10
Connection string: jdbc:postgresql://localhost:5432/mydb
```

---

### **2.3 Выбор правильной функции**
Реализуй функцию `fun processUser(id: Int): String`.

Внутри используй **разные** scope функции по назначению:
1. `apply` — создать и настроить `StringBuilder`
2. `let` — безопасно обработать nullable результат `findUser(id)`
3. `also` — залогировать результат (`println("Processing: ...")`)
4. `run` — вернуть финальную строку

Функция должна возвращать `"User: Alex"` если найден, `"Unknown user"` если нет.

---

## 🔴 УРОВЕНЬ 3: Продвинутое (4 задания)

### **3.1 Цепочка трансформаций**
Есть данные:
```kotlin
data class RawData(val input: String?)
data class ProcessedData(val value: Int, val label: String)
```

Напиши функцию `fun process(raw: RawData): ProcessedData?` которая:
1. Через `let` — достаёт `input` если не null
2. Через `let` — конвертирует в Int (используй `toIntOrNull()`)
3. Через `let` — создаёт `ProcessedData(value, if (value > 0) "positive" else "non-positive")`
4. Через `also` — логирует результат

Протестируй с `"42"`, `"-5"`, `"abc"`, `null`.

---

### **3.2 Builder паттерн через apply**
Напиши класс `HttpRequest` с полями:
```kotlin
class HttpRequest {
    var url: String = ""
    var method: String = "GET"
    var headers: MutableMap<String, String> = mutableMapOf()
    var body: String? = null
    var timeout: Int = 30

    fun addHeader(key: String, value: String) { headers[key] = value }
    fun execute(): String = "Response from $method $url"
}
```

Напиши функцию `fun buildRequest(setup: HttpRequest.() -> Unit): HttpRequest`.

В main создай несколько запросов через неё:
```kotlin
val getRequest = buildRequest {
    url = "https://api.example.com/users"
    addHeader("Authorization", "Bearer token123")
}

val postRequest = buildRequest {
    url = "https://api.example.com/users"
    method = "POST"
    body = """{"name": "Alex"}"""
    addHeader("Content-Type", "application/json")
    timeout = 60
}
```

---

### **3.3 Android-style ViewModel**
Имитируй паттерн обновления UI через scope функции:

```kotlin
data class UiState(
    val isLoading: Boolean = false,
    val users: List<String> = emptyList(),
    val error: String? = null
)
```

Напиши функцию `fun simulateLoad(shouldFail: Boolean): UiState` которая:
1. Начинает с `UiState()` и через `copy` + `also` — устанавливает `isLoading = true` и логирует
2. Через `run` — "загружает данные" (если `shouldFail` бросает исключение)
3. Через `let` — обрабатывает успешный результат (возвращает state с users)
4. Обрабатывает ошибку — возвращает state с error

Протестируй оба сценария.

---

### **3.4 Scope функции без объекта**
Напиши функцию `fun initApp(): AppConfig` используя `run` **без объекта** (просто блок кода):

```kotlin
data class AppConfig(
    val appName: String,
    val version: String,
    val isDebug: Boolean,
    val features: List<String>
)
```

Внутри `run`:
- Считай переменные окружения через `System.getenv()` с дефолтами
- Собери список фич на основе `isDebug`
- Верни `AppConfig`

В main вызови и выведи конфиг через `with`.

---

## Прогресс

| Уровень | Задания | Статус |
|---------|---------|--------|
| 1. Основы | 3/3 | ✅ |
| 2. Комбинации | 3/3 | ✅ |
| 3. Продвинутое | 4/4 | ✅ |
| **Итого** | **10/10** | ✅ |
