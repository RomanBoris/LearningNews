# Теория: try / catch

## Базовый синтаксис

```kotlin
try {
    // код который может упасть
    val result = riskyOperation()
} catch (e: SomeException) {
    // обработка ошибки
    println(e.message)
} finally {
    // выполняется всегда (необязательный блок)
    println("Готово")
}
```

---

## try как выражение

В Kotlin `try` возвращает значение — как `if` и `when`:

```kotlin
val result: Int = try {
    "42".toInt()
} catch (e: NumberFormatException) {
    -1  // возвращается при ошибке
}
```

---

## Несколько catch блоков

```kotlin
try {
    riskyOperation()
} catch (e: IllegalArgumentException) {
    println("Неверный аргумент")
} catch (e: IOException) {
    println("Ошибка ввода/вывода")
} catch (e: Exception) {
    println("Что-то пошло не так: ${e.message}")
}
```

Порядок важен — `Exception` (родитель всех) всегда последний.

---

## Иерархия исключений

```
Throwable
├── Error          — критические ошибки JVM (не ловим)
└── Exception
    ├── RuntimeException   — NPE, IndexOutOfBounds, etc.
    └── IOException        — файлы, сеть
```

---

## throw

```kotlin
fun divide(a: Int, b: Int): Int {
    if (b == 0) throw IllegalArgumentException("Делитель не может быть 0")
    return a / b
}
```

`throw` тоже выражение — можно использовать с Elvis:
```kotlin
val name = input ?: throw IllegalStateException("Имя не может быть null")
```

---

## В корутинах

`try/catch` работает так же — внутри `suspend` функции или корутины:

```kotlin
launch {
    try {
        val data = fetchData()  // suspend функция
        println(data)
    } catch (e: Exception) {
        println("Ошибка: ${e.message}")
    }
}
```

⚠️ `CancellationException` — не нужно ловить вручную, это нормальная отмена корутины. Если поймал — перебрось:
```kotlin
} catch (e: CancellationException) {
    throw e  // не глотаем отмену!
} catch (e: Exception) {
    // вот здесь обрабатываем реальные ошибки
}
```
