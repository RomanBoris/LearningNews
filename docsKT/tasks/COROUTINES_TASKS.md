# Задания: Корутины (Coroutines)

**Теория**: `docs/theory/KOTLIN_COROUTINES_THEORY.md`
**Файл реализации**: `src/coroutines/Coroutines.kt`

---

## Уровень 1 — Базовые (launch, delay, suspend)

### task 1.1 - launch basics
Запустить 3 корутины через `launch`. Каждая печатает своё имя, делает `delay` разной длины и снова печатает.
Наблюдаем: они работают параллельно (порядок завершения ≠ порядок запуска).

```
Запущена: Fast
Запущена: Medium
Запущена: Slow
Завершена: Fast
Завершена: Medium
Завершена: Slow
```

### task 1.2 - suspend function
Написать `suspend fun fetchData(source: String): String` — делает `delay(500)`, возвращает `"Данные из $source"`.
Вызвать из `runBlocking`, напечатать результат.

---

## Уровень 2 — async/await, Job

### task 2.1 - async parallel
Симулировать два "сетевых запроса": `fetchUser()` (delay 1000ms) и `fetchPosts()` (delay 800ms).
- Сначала выполнить **последовательно** — замерить время (`System.currentTimeMillis()`).
- Потом выполнить **параллельно** через `async` — замерить время.
- Напечатать оба результата и разницу во времени.

### task 2.2 - job control
Запустить корутину которая каждые 300ms печатает счётчик (`Тик: 0`, `Тик: 1`, ...).
Через 1000ms — отменить через `job.cancel()`. Напечатать `"Корутина остановлена"`.

---

## Уровень 3 — withContext, Structured Concurrency

### task 3.1 - withContext
Написать `suspend fun loadAndProcess(): String`:
- Внутри `withContext(Dispatchers.IO)` — "загружаем данные" (delay 500ms, возвращаем строку)
- Внутри `withContext(Dispatchers.Default)` — "обрабатываем" (преобразуем строку в верхний регистр)
- Вернуть результат

Вызвать из `runBlocking`, напечатать имя потока на каждом шаге (`Thread.currentThread().name`).

### task 3.2 - structured concurrency
Запустить родительскую корутину. Внутри неё — 3 дочерние корутины с разными delay.
Наблюдать: `runBlocking` ждёт пока все дочерние завершатся.
Напечатать порядок событий.

---

## Уровень 4 — Обработка ошибок

### task 4.1 - try/catch
Написать `suspend fun riskyOperation(shouldFail: Boolean): Int` — при `shouldFail=true` бросает `RuntimeException`.
Вызвать дважды: с `false` (успех) и `true` (ошибка). Поймать ошибку через `try/catch`.

### task 4.2 - exception handler
Создать `CoroutineExceptionHandler` который логирует ошибку.
Создать `CoroutineScope` с этим handler'ом.
Запустить корутину которая выбрасывает исключение — убедиться что handler его поймал, и программа не упала.
