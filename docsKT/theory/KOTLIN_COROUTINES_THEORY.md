# Теория: Корутины (Coroutines)

---

## 1. Зачем вообще нужны корутины

Любое реальное приложение делает асинхронную работу — сетевые запросы, чтение файлов, работа с базой данных. Вопрос в том, **как это организовать**.

### Проблема 1 — блокирующий код

На Android UI работает в одном потоке (Main thread). Если заблокировать его — приложение зависнет.

```kotlin
// ❌ НЕЛЬЗЯ делать на Main thread
fun loadUser() {
    Thread.sleep(2000)          // блокируем поток на 2 сек — ANR (App Not Responding)
    val user = api.getUser(1)   // сетевой запрос — тоже блокирует
    textView.text = user.name
}
```

### Проблема 2 — callback hell

Классическое решение — колбэки. Но они превращают код в нечитаемую пирамиду:

```kotlin
// ❌ Callback hell — реальный код из Android до 2018 года
fun loadUserProfile(userId: Int) {
    api.getUser(userId) { user ->
        api.getPosts(user.id) { posts ->
            api.getComments(posts[0].id) { comments ->
                api.getLikes(comments[0].id) { likes ->
                    // Мы на 4-м уровне вложенности
                    // Обработка ошибок? Отмена запросов? Кошмар.
                    runOnUiThread {
                        updateUI(user, posts, comments, likes)
                    }
                }
            }
        }
    }
}
```

### Решение — корутины

```kotlin
// ✅ С корутинами — читается как синхронный код, но не блокирует поток
fun loadUserProfile(userId: Int) {
    viewModelScope.launch {
        val user     = api.getUser(userId)           // suspend — ждёт, не блокирует
        val posts    = api.getPosts(user.id)          // suspend
        val comments = api.getComments(posts[0].id)  // suspend
        val likes    = api.getLikes(comments[0].id)  // suspend
        updateUI(user, posts, comments, likes)        // всё в одном месте
    }
}
```

**Главная идея корутин**: писать асинхронный код так же прямо, как синхронный — сверху вниз, без вложенности.

---

## 2. Что такое корутина

Корутина — это **блок кода который может приостановить своё выполнение** (не блокируя поток), подождать результата, и продолжить с того же места.

Ключевое отличие от потока:

```
Поток:   [===работает===|===ЗАБЛОКИРОВАН waiting===|===работает===]
           поток занят      поток занят и ждёт         поток занят

Корутина:[===работает===|  приостановлена   |===продолжает===]
           поток занят    поток СВОБОДЕН        поток занят снова
```

Пока корутина приостановлена — поток свободен и выполняет другую работу.

---

## 3. Поток vs Корутина — под капотом

| | Поток (Thread) | Корутина |
|---|---|---|
| Память | ~1 MB на поток | несколько десятков байт |
| Создание | дорого (системный вызов ОС) | дёшево (объект в heap) |
| Максимум | сотни | миллионы |
| Переключение | ОС (OS context switch) | Kotlin runtime (дёшево) |
| Блокировка | блокирует поток целиком | приостанавливается, поток свободен |
| Отмена | сложно (Thread.interrupt ненадёжен) | встроена в язык |

Наглядный пример:

```kotlin
// Попробуй запустить 100,000 потоков — OutOfMemoryError
fun badExample() {
    repeat(100_000) {
        Thread {
            Thread.sleep(1000)
            println("Поток $it")
        }.start()
    }
}

// 100,000 корутин — работает нормально, ~несколько MB памяти
fun goodExample() = runBlocking {
    repeat(100_000) {
        launch {
            delay(1000)
            println("Корутина $it")
        }
    }
}
```

---

## 4. suspend — ключевое слово

`suspend` перед функцией означает: **"эта функция может приостановиться"**.

```kotlin
suspend fun fetchUserFromNetwork(id: Int): User {
    delay(1000)               // приостанавливаемся на 1 сек — поток НЕ заблокирован
    return User(id, "Alex")   // возобновляемся и возвращаем результат
}
```

### Правила вызова suspend функций

`suspend` функцию можно вызвать только из:
1. Другой `suspend` функции
2. Корутины (внутри `launch`, `async`, `runBlocking`)

```kotlin
fun regularFunction() {
    fetchUserFromNetwork(1)  // ❌ Ошибка компиляции!
                             // "Suspend function can only be called from coroutine"
}

suspend fun anotherSuspend() {
    fetchUserFromNetwork(1)  // ✅ Из suspend функции — можно
}

fun main() = runBlocking {
    fetchUserFromNetwork(1)  // ✅ Из корутины — можно
}
```

### Что происходит под капотом

Компилятор Kotlin превращает `suspend` функцию в конечный автомат (state machine). Каждая точка приостановки — это состояние. Между состояниями поток свободен.

```
fetchUserFromNetwork вызвана
    ↓
[Состояние 0] → запустить delay(1000)
    ↓ поток освобождается
    ↓ ... 1000ms проходит ...
    ↓ поток возвращается
[Состояние 1] → return User(id, "Alex")
    ↓
функция завершена
```

---

## 5. Coroutine Builders — как запустить корутину

### `runBlocking`

Блокирует **текущий поток** пока все корутины внутри не завершатся. Создаёт мост между обычным и корутинным кодом.

```kotlin
fun main() = runBlocking {
    println("Начало: ${Thread.currentThread().name}")
    delay(500)
    println("Конец: ${Thread.currentThread().name}")
}
// Начало: main
// Конец: main
// (main поток был заблокирован на 500ms)
```

**Когда использовать**: `main()` функции, юнит-тесты. **Никогда** в Android UI коде — заморозит экран.

---

### `launch`

Запускает новую корутину **без возврата результата**. Возвращает `Job`.

```kotlin
fun main() = runBlocking {
    println("До launch")

    val job = launch {
        println("Корутина запущена")
        delay(1000)
        println("Корутина завершена")
    }

    println("После launch — корутина ещё работает")
    job.join()  // ждём завершения корутины
    println("Всё готово")
}
// До launch
// После launch — корутина ещё работает  ← main продолжил работу сразу
// Корутина запущена
// Корутина завершена                    ← через 1 сек
// Всё готово
```

`launch` — это **fire and forget**: запустили и не ждём результата.

---

### `async`

Запускает корутину **с возвратом результата**. Возвращает `Deferred<T>`. Результат получаем через `await()`.

```kotlin
fun main() = runBlocking {
    // Запускаем оба запроса параллельно
    val userDeferred  = async { fetchUser(1) }    // запустился сразу
    val postsDeferred = async { fetchPosts(1) }   // запустился сразу, параллельно

    // Ждём оба результата
    val user  = userDeferred.await()   // suspend — ждём если ещё не готов
    val posts = postsDeferred.await()  // suspend — ждём если ещё не готов

    println("${user.name}: ${posts.size} постов")
}
```

**Разница в скорости — последовательно vs параллельно:**

```kotlin
fun main() = runBlocking {
    // ПОСЛЕДОВАТЕЛЬНО — суммарное время = 1000 + 800 = 1800ms
    val start1 = System.currentTimeMillis()
    val user1  = fetchUser(1)    // suspend — ждём 1000ms
    val posts1 = fetchPosts(1)   // suspend — ждём 800ms
    println("Последовательно: ${System.currentTimeMillis() - start1}ms") // ~1800ms

    // ПАРАЛЛЕЛЬНО — суммарное время = max(1000, 800) = 1000ms
    val start2 = System.currentTimeMillis()
    val userD  = async { fetchUser(1) }   // запустили
    val postsD = async { fetchPosts(1) }  // запустили параллельно
    userD.await()
    postsD.await()
    println("Параллельно: ${System.currentTimeMillis() - start2}ms")     // ~1000ms
}
```

---

### Сравнение builders

| | `runBlocking` | `launch` | `async` |
|---|---|---|---|
| Блокирует поток | ДА | нет | нет |
| Возвращает | Unit | Job | Deferred\<T\> |
| Результат | — | — | через await() |
| Применение | main/тесты | fire and forget | параллельные задачи с результатом |

---

## 6. Job — управление жизненным циклом

Каждый `launch` возвращает `Job` — это хэндл корутины, через который можно управлять ею.

```kotlin
fun main() = runBlocking {
    val job = launch {
        repeat(10) { i ->
            println("Тик: $i")
            delay(300)
        }
    }

    delay(1000)      // ждём 1 сек — корутина успеет напечатать ~3 тика
    job.cancel()     // отменяем корутину
    job.join()       // ждём завершения (уже отменённой)
    println("Корутина остановлена")
}
// Тик: 0
// Тик: 1
// Тик: 2
// Тик: 3
// Корутина остановлена
```

### Состояния Job

```
     launch()
        ↓
    [Active]  ←─────────────────┐
        ↓                        │ нельзя вернуться назад
   job.cancel()              job запущен
        ↓
  [Cancelling]   ← выполняются finally блоки
        ↓
  [Cancelled]

  или завершился сам:
    [Active] → [Completing] → [Completed]
```

### cancelAndJoin()

Удобный shortcut вместо `cancel()` + `join()`:

```kotlin
job.cancelAndJoin()  // отменить и дождаться завершения
```

### isActive — проверка отмены в цикле

Если корутина делает тяжёлые вычисления без `delay`, она не отменится автоматически. Нужно проверять `isActive`:

```kotlin
val job = launch {
    var i = 0
    while (isActive) {        // проверяем не отменили ли нас
        i++
        // тяжёлые вычисления...
    }
    println("Остановились на $i")
}
```

---

## 7. Dispatchers — на каком потоке работает корутина

Dispatcher определяет **в каком потоке или пуле потоков** будет выполняться корутина.

| Dispatcher | Поток | Для чего |
|---|---|---|
| `Dispatchers.Main` | UI поток | обновление UI, Android only |
| `Dispatchers.IO` | пул IO потоков (до 64) | сеть, файлы, база данных |
| `Dispatchers.Default` | пул CPU потоков (= кол-во ядер) | тяжёлые вычисления, парсинг |
| `Dispatchers.Unconfined` | не привязан | редко, специфические случаи |

```kotlin
fun main() = runBlocking {
    // Без dispatcher — наследует от родителя (main поток)
    launch {
        println("Без dispatcher: ${Thread.currentThread().name}")
        // → main
    }

    // IO — для блокирующих операций (сеть, файлы)
    launch(Dispatchers.IO) {
        println("IO dispatcher: ${Thread.currentThread().name}")
        // → DefaultDispatcher-worker-1 (из IO пула)
    }

    // Default — для CPU-интенсивных задач
    launch(Dispatchers.Default) {
        println("Default dispatcher: ${Thread.currentThread().name}")
        // → DefaultDispatcher-worker-2
    }
}
```

### Почему важно правильно выбирать dispatcher

```kotlin
// ❌ Неправильно — блокирующий IO в Default (занимаем CPU поток)
launch(Dispatchers.Default) {
    val text = File("big.txt").readText()  // блокирует CPU поток
}

// ✅ Правильно — блокирующий IO в IO dispatcher
launch(Dispatchers.IO) {
    val text = File("big.txt").readText()  // IO поток создан для этого
}

// ❌ Неправильно — тяжёлые вычисления в IO
launch(Dispatchers.IO) {
    val result = (1..1_000_000).map { it * it }.sum()  // займёт IO поток
}

// ✅ Правильно — вычисления в Default
launch(Dispatchers.Default) {
    val result = (1..1_000_000).map { it * it }.sum()
}
```

---

## 8. withContext — переключение потока внутри корутины

`withContext` позволяет переключить dispatcher **внутри одной корутины** без создания новой.

```kotlin
suspend fun loadAndProcess(): String {
    // Шаг 1: загрузка данных — в IO потоке
    val rawData = withContext(Dispatchers.IO) {
        println("Загружаем в: ${Thread.currentThread().name}")
        delay(500)
        "данные из сети"
    }
    // Вернулись на предыдущий dispatcher

    // Шаг 2: обработка — в Default потоке (CPU)
    val processed = withContext(Dispatchers.Default) {
        println("Обрабатываем в: ${Thread.currentThread().name}")
        rawData.uppercase()
    }

    return processed
}

fun main() = runBlocking {
    val result = loadAndProcess()
    println("Результат: $result")
}
// Загружаем в: DefaultDispatcher-worker-1
// Обрабатываем в: DefaultDispatcher-worker-2
// Результат: ДАННЫЕ ИЗ СЕТИ
```

`withContext` vs `async`:
- `withContext` — переключает поток, **ждёт результата**, возвращает значение
- `async` — запускает **параллельно**, нужен `await()` для получения результата

---

## 9. CoroutineScope — область жизни корутин

Scope — это **контейнер для корутин**. Когда scope отменяется — все корутины внутри него тоже отменяются автоматически.

```kotlin
fun main() = runBlocking {
    val scope = CoroutineScope(Dispatchers.IO)

    scope.launch { delay(5000); println("Корутина 1") }
    scope.launch { delay(5000); println("Корутина 2") }
    scope.launch { delay(5000); println("Корутина 3") }

    delay(1000)
    scope.cancel()  // Отменяем всё сразу — ни одна не напечатает
    println("Scope отменён")
}
// Scope отменён
// (корутины не успели завершиться — были отменены)
```

### Android: scope привязан к жизненному циклу

Это главная причина почему корутины так хороши в Android:

```kotlin
class UserViewModel(private val repo: UserRepository) : ViewModel() {

    fun loadUser(id: Int) {
        viewModelScope.launch {           // scope живёт пока жива ViewModel
            val user = repo.getUser(id)  // suspend — не блокирует UI
            _uiState.value = UiState.Success(user)
        }
        // Когда ViewModel уничтожится — запрос отменится автоматически!
        // Не нужно писать onCleared() вручную.
    }
}

class UserFragment : Fragment() {

    fun onButtonClick() {
        lifecycleScope.launch {              // scope живёт пока жив Fragment
            val data = viewModel.loadData() // если Fragment уничтожится — отменится
            updateUI(data)
        }
    }
}
```

---

## 10. Structured Concurrency — структурная многозадачность

Это **главный принцип** дизайна корутин. Корутины организованы в дерево:
- Родительская корутина **ждёт** завершения всех дочерних
- Если дочерняя падает — родитель **отменяется** (и все остальные дочерние тоже)
- Если родитель отменяется — все дочерние **отменяются**

```kotlin
fun main() = runBlocking {                    // родитель
    println("Родитель начал")

    launch {                                  // дочерняя 1
        delay(300)
        println("Дочерняя 1 завершена")
    }

    launch {                                  // дочерняя 2
        delay(100)
        println("Дочерняя 2 завершена")
    }

    launch {                                  // дочерняя 3
        delay(500)
        println("Дочерняя 3 завершена")
    }

    println("Родитель продолжает работу")
    // runBlocking НЕ завершится пока все 3 дочерних не завершатся
}
// Родитель начал
// Родитель продолжает работу
// Дочерняя 2 завершена  (100ms)
// Дочерняя 1 завершена  (300ms)
// Дочерняя 3 завершена  (500ms)
// ← только тогда завершается runBlocking
```

### Ошибка в дочерней — падает всё

```kotlin
fun main() = runBlocking {
    launch {
        delay(100)
        println("Дочерняя 1 — работаю")
        throw RuntimeException("Что-то пошло не так!")
    }

    launch {
        delay(500)
        println("Дочерняя 2 — этот текст НЕ напечатается")
    }

    delay(1000)
    println("Родитель — этот текст тоже НЕ напечатается")
}
// Дочерняя 1 — работаю
// Exception in thread "main" java.lang.RuntimeException: Что-то пошло не так!
// ← дочерняя 2 и родитель были отменены
```

Это **намеренное поведение**: ошибки не теряются молча, а всплывают.

---

## 11. Обработка ошибок

### try/catch внутри корутины

```kotlin
fun main() = runBlocking {
    launch {
        try {
            println("Начинаем рискованную операцию")
            riskyOperation(shouldFail = true)
            println("Успех")  // не выполнится
        } catch (e: RuntimeException) {
            println("Поймали ошибку: ${e.message}")
        } finally {
            println("finally всегда выполняется")
        }
    }
}

suspend fun riskyOperation(shouldFail: Boolean): Int {
    delay(200)
    if (shouldFail) throw RuntimeException("Операция провалилась")
    return 42
}
// Начинаем рискованную операцию
// Поймали ошибку: Операция провалилась
// finally всегда выполняется
```

### CoroutineExceptionHandler

Глобальный обработчик ошибок для scope — перехватывает необработанные исключения:

```kotlin
fun main() = runBlocking {
    val handler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("Handler поймал [${coroutineContext}]: $exception")
    }

    val scope = CoroutineScope(Dispatchers.Default + handler)

    scope.launch {
        delay(100)
        throw IllegalStateException("Упал!")
    }

    delay(500)
    println("Программа не упала — handler спас нас")
}
// Handler поймал [...]: java.lang.IllegalStateException: Упал!
// Программа не упала — handler спас нас
```

⚠️ `CoroutineExceptionHandler` работает только с `launch`, не с `async`. Ошибки из `async` нужно ловить через `try/catch` при вызове `await()`.

```kotlin
// async — ошибка хранится в Deferred, бросается при await()
val deferred = async { throw RuntimeException("async ошибка") }
try {
    deferred.await()  // ← здесь бросается исключение
} catch (e: RuntimeException) {
    println("Поймали: ${e.message}")
}
```

---

## 12. Краткая шпаргалка

```
ЧТО ДЕЛАЕМ                          КАК
─────────────────────────────────────────────────────────────
Запустить без результата           → launch { }
Запустить с результатом            → async { } + .await()
Войти в мир корутин из main()      → runBlocking { }
Приостановить на время             → delay(ms)
Переключить поток                  → withContext(Dispatcher)
Отменить корутину                  → job.cancel()
Дождаться завершения               → job.join() / .await()
Отменить и дождаться               → job.cancelAndJoin()
Проверить не отменили ли нас       → isActive
Группировать и отменять скопом     → CoroutineScope + .cancel()

ВЫБОР DISPATCHER
─────────────────────────────────────────────────────────────
Сеть, файлы, БД (блокирующий IO)   → Dispatchers.IO
Тяжёлые вычисления (CPU)           → Dispatchers.Default
Обновление UI (Android)            → Dispatchers.Main
```

---

## 13. Типичный паттерн в Android

```kotlin
// --- Слой данных (Repository) ---
class UserRepository(private val api: ApiService, private val db: UserDao) {

    // suspend функция — вызывающий код сам выбирает когда вызвать
    suspend fun getUser(id: Int): User {
        return withContext(Dispatchers.IO) {  // уходим в IO поток
            val cached = db.getUser(id)
            if (cached != null) return@withContext cached

            val user = api.fetchUser(id)      // сетевой запрос
            db.insertUser(user)               // сохраняем в БД
            user
        }
        // автоматически возвращаемся на dispatcher вызывающего
    }
}

// --- ViewModel ---
class UserViewModel(private val repo: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadUser(id: Int) {
        viewModelScope.launch {                    // запускаем в viewModelScope
            _uiState.value = UiState.Loading
            try {
                val user = repo.getUser(id)        // suspend — не блокирует UI
                _uiState.value = UiState.Success(user)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
        // viewModelScope автоматически отменит запрос если ViewModel уничтожится
    }
}

// --- Fragment ---
class UserFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->   // наблюдаем за состоянием
                when (state) {
                    is UiState.Loading  -> showLoading()
                    is UiState.Success  -> showUser(state.user)
                    is UiState.Error    -> showError(state.message)
                }
            }
        }
    }
}
```
