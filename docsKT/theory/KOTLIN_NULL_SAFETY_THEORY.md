# Теория: Null Safety в Kotlin

## Зачем это вообще существует

В Java любая переменная-объект может быть null. Это приводит к `NullPointerException` — одной из самых частых причин крашей приложений. Kotlin решил эту проблему на уровне системы типов: **тип должен явно разрешать null, иначе компилятор не пропустит**.

```java
// Java — компилируется, но крашится в рантайме
String name = null;
System.out.println(name.length()); // NullPointerException
```

```kotlin
// Kotlin — не компилируется, ошибка на этапе сборки
val name: String = null        // ❌ Ошибка компилятора
val name: String? = null       // ✅ Явно разрешаем null
println(name.length)           // ❌ Ошибка компилятора — name может быть null
println(name?.length)          // ✅ Безопасный вызов
```

---

## Nullable и Non-nullable типы

```kotlin
var a: String = "hello"    // Non-nullable — null запрещён
var b: String? = "hello"   // Nullable — null разрешён

a = null   // ❌ Ошибка компилятора
b = null   // ✅ OK

a.length   // ✅ Безопасно, компилятор знает что не null
b.length   // ❌ Ошибка компилятора — нужна проверка
```

**Правило**: `String` и `String?` — это **разные типы** в Kotlin. Nullable тип — это надмножество, он включает все значения non-nullable типа + null.

```kotlin
// Все примитивы тоже бывают nullable
var x: Int = 5
var y: Int? = null

// Но компилятор оптимизирует Int? → Integer (Java) только если нужно
// В остальных случаях Int остаётся примитивом
```

---

## Safe call оператор `?.`

Вызывает метод или свойство **только если объект не null**. Если null — возвращает null.

```kotlin
val name: String? = "Alex"
val length: Int? = name?.length   // 4

val empty: String? = null
val result: Int? = empty?.length  // null — без краша
```

### Цепочка safe calls

```kotlin
data class Address(val city: String?)
data class User(val address: Address?)

val user: User? = getUser()

// Без safe calls — громоздко
val city: String? = if (user != null && user.address != null) {
    user.address.city
} else null

// С safe calls — чисто
val city: String? = user?.address?.city
```

Если любое звено в цепочке null — вся цепочка возвращает null, без NPE.

### Safe call с методами

```kotlin
val name: String? = "  Alex  "
val upper: String? = name?.trim()?.uppercase()   // "ALEX"

val nullName: String? = null
val result: String? = nullName?.trim()?.uppercase()   // null
```

---

## Elvis оператор `?:`

Возвращает левую часть если она не null, иначе — правую. Это "значение по умолчанию".

```kotlin
val name: String? = null
val result: String = name ?: "Unknown"   // "Unknown"

val name2: String? = "Alex"
val result2: String = name2 ?: "Unknown"  // "Alex"
```

### Комбинация с safe call

```kotlin
val user: User? = getUser()
val city = user?.address?.city ?: "Город не указан"
// Если user null, address null, или city null — вернёт дефолт
```

### Elvis с return и throw

Elvis — это выражение, поэтому можно бросить исключение или вернуться из функции:

```kotlin
fun processUser(user: User?) {
    val name = user?.name ?: return   // выход из функции если null
    val email = user.email ?: throw IllegalStateException("Email обязателен")
    // здесь name и email точно не null
}
```

```kotlin
fun getAge(user: User?): Int {
    return user?.age ?: -1
}
```

---

## Non-null assertion `!!`

Говорит компилятору: "Я уверен что это не null, доверяй мне". Если окажется null — бросит `NullPointerException`.

```kotlin
val name: String? = "Alex"
val length: Int = name!!.length   // 4 — OK

val empty: String? = null
val crash: Int = empty!!.length   // NullPointerException в рантайме!
```

**Когда использовать `!!`**:
- Когда ты **архитектурно уверен** что значение не null, но компилятор не может это проверить
- В тестах где NPE = провал теста (это ок)
- Никогда просто чтобы "заткнуть компилятор"

```kotlin
// Плохо — !! без причины
val user = getUser()!!.name!!.trim()!!

// Лучше — явная проверка или let
val user = getUser() ?: return
val name = user.name?.trim() ?: "Unknown"
```

---

## Smart cast

Kotlin отслеживает проверки на null и **автоматически** снимает nullable после проверки.

```kotlin
val name: String? = getName()

if (name != null) {
    println(name.length)   // ✅ Smart cast — компилятор знает что name: String
}

// Вне блока if — снова String?
println(name?.length)
```

```kotlin
// Smart cast работает и с when
fun describe(value: Any?) {
    when (value) {
        null -> println("null")
        is String -> println("String длиной ${value.length}")   // value: String
        is Int -> println("Int: ${value * 2}")                  // value: Int
        else -> println("Что-то другое")
    }
}
```

### Когда smart cast НЕ работает

```kotlin
class Container {
    var value: String? = null
}

val c = Container()
if (c.value != null) {
    println(c.value.length)   // ❌ Ошибка! var может измениться между проверкой и использованием
}

// Решение — скопировать в val
val v = c.value
if (v != null) {
    println(v.length)   // ✅ val не может измениться
}
```

Smart cast работает только с `val` и локальными переменными, не с `var` свойствами класса.

---

## let с nullable

`?.let` — самый идиоматичный способ выполнить блок кода только если значение не null.

```kotlin
val email: String? = getEmail()

// Без let
if (email != null) {
    sendEmail(email)
    println("Отправлено на $email")
}

// С let — чище, особенно когда значение вычисляется
email?.let {
    sendEmail(it)
    println("Отправлено на $it")
}
```

```kotlin
// let возвращает результат блока
val length: Int? = email?.let { it.length }

// Комбинация с elvis
val displayEmail: String = email?.let { "Email: $it" } ?: "Email не указан"
```

```kotlin
// Цепочка трансформаций через let
val result = getInput()
    ?.trim()
    ?.takeIf { it.isNotEmpty() }
    ?.let { it.uppercase() }
    ?: "DEFAULT"
```

---

## takeIf и takeUnless

Возвращают объект если условие выполняется, иначе null. Удобно в цепочках.

```kotlin
val age: Int = 15

val adult = age.takeIf { it >= 18 }        // null (15 < 18)
val minor = age.takeUnless { it >= 18 }    // 15 (условие не выполнено)
```

```kotlin
// Типичный паттерн — фильтрация в цепочке
val name = input
    .trim()
    .takeIf { it.length >= 3 }    // null если строка короче 3 символов
    ?.uppercase()
    ?: "TOO SHORT"
```

```kotlin
// Android пример
val user = repository.getUser(id)
    .takeIf { it.isActive }   // null если пользователь неактивен
    ?: throw UserNotFoundException("User $id is inactive")
```

---

## Safe cast `as?`

Пробует привести тип, возвращает null если не получается (вместо `ClassCastException`).

```kotlin
val obj: Any = "Hello"

val str: String? = obj as? String   // "Hello"
val num: Int? = obj as? Int         // null — без исключения

// Небезопасный cast — бросит ClassCastException
val crash: Int = obj as Int   // ClassCastException!
```

```kotlin
// Типичное использование
fun process(input: Any) {
    val text = input as? String ?: return
    println(text.uppercase())   // text — String, не Any
}
```

---

## lateinit

Для `var` свойств ненулевого типа, которые инициализируются позже (не в конструкторе).

```kotlin
class MyFragment : Fragment() {
    lateinit var binding: FragmentMainBinding   // ✅ без ?

    override fun onCreateView(...): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(...) {
        binding.tvTitle.text = "Hello"   // ✅ безопасно, уже инициализировано
    }
}
```

**Отличие от nullable**:
```kotlin
var binding: FragmentMainBinding? = null   // нужно binding?.tvTitle или binding!!.tvTitle везде
lateinit var binding: FragmentMainBinding  // просто binding.tvTitle — без ? и !!
```

**Проверка инициализации**:
```kotlin
if (::binding.isInitialized) {
    binding.tvTitle.text = "Safe"
}
```

**Ограничения `lateinit`**:
- Только `var`, не `val`
- Только ненулевые типы
- Не работает с примитивами (`Int`, `Boolean` и т.д.) — используй `Int?` вместо этого
- Если обратиться до инициализации — `UninitializedPropertyAccessException`

---

## Nullable в коллекциях

```kotlin
// Список nullable элементов
val list: List<String?> = listOf("Alex", null, "Bob", null)

// Фильтр null — filterNotNull()
val names: List<String> = list.filterNotNull()   // ["Alex", "Bob"]

// Nullable список
val nullableList: List<String>? = getList()
val size = nullableList?.size ?: 0
val first = nullableList?.firstOrNull()
```

```kotlin
// Map с nullable значениями
val map: Map<String, Int?> = mapOf("a" to 1, "b" to null)
val value = map["b"] ?: 0   // 0
```

---

## Platform types (типы из Java)

Когда Kotlin вызывает Java код — типы становятся "platform types" (помечаются `!`). Kotlin не знает nullable они или нет.

```kotlin
// Java метод: public String getName() { return null; }

val name = javaObject.getName()   // тип: String! (platform type)
// Компилятор не предупредит о возможном null — опасно!

// Безопасно — явно объявить как nullable
val safeName: String? = javaObject.getName()
val result = safeName?.uppercase() ?: "Unknown"
```

В Android с этим сталкиваешься при работе с Bundle, Intent, старыми API.

---

## Паттерны в Android

### 1. Аргументы Fragment

```kotlin
// Получение аргументов — могут быть null
val userId = arguments?.getString("userId") ?: return
// Дальше userId — String (не String?)
```

### 2. SharedPreferences

```kotlin
val prefs = getSharedPreferences("app", Context.MODE_PRIVATE)
val token = prefs.getString("token", null)   // String?

token?.let {
    api.setAuthToken(it)
} ?: navigateToLogin()
```

### 3. ViewModel + LiveData/StateFlow

```kotlin
// Вместо nullable LiveData лучше использовать sealed/дефолтные значения
private val _state = MutableStateFlow(UiState())   // не nullable!

// Но если нужно nullable
private val _user = MutableLiveData<User?>()

_user.value?.let { user ->
    binding.tvName.text = user.name
}
```

### 4. Безопасная навигация

```kotlin
fun navigateToDetail(id: String?) {
    val safeId = id ?: run {
        showError("ID не найден")
        return
    }
    findNavController().navigate(
        MainFragmentDirections.toDetail(safeId)
    )
}
```

### 5. Работа с Bundle

```kotlin
val bundle: Bundle? = savedInstanceState
val count = bundle?.getInt("count") ?: 0
val name = bundle?.getString("name")   // String?
```

---

## Частые ошибки

### 1. !! без необходимости

```kotlin
// ❌ Это крашнется если getUser() вернёт null
val name = getUser()!!.name

// ✅ Явная обработка
val name = getUser()?.name ?: "Unknown"
```

### 2. Проверка и тут же !!

```kotlin
// ❌ Абсурдно — зачем проверять если потом !!
if (user != null) {
    println(user!!.name)
}

// ✅ После if компилятор сам знает
if (user != null) {
    println(user.name)   // smart cast
}
```

### 3. Игнорирование результата ?.let

```kotlin
val email: String? = getEmail()

// ❌ Результат let игнорируется — зачем тогда let?
val result = email?.let { "Email: $it" }
println(result)  // может напечатать null

// ✅ Добавь elvis
val result = email?.let { "Email: $it" } ?: "Нет email"
println(result)  // всегда строка
```

### 4. var вместо val для smart cast

```kotlin
class Foo {
    var name: String? = "Alex"

    fun print() {
        if (name != null) {
            println(name!!.length)   // !! нужен — var может стать null
        }

        val n = name   // копируем в val
        if (n != null) {
            println(n.length)   // ✅ smart cast работает
        }
    }
}
```

### 5. Nullable коллекция vs коллекция nullable

```kotlin
val a: List<String>? = null        // сама коллекция может быть null
val b: List<String?> = listOf()    // коллекция не null, но элементы могут быть null
val c: List<String?>? = null       // и то и другое — обычно это перебор

// Почти всегда лучше:
val d: List<String> = emptyList()  // просто пустой список вместо null
```

---

## Итоговая шпаргалка

```
Есть nullable значение → что делать?

?.          — безопасный доступ к свойству/методу
?:          — значение по умолчанию если null
?.let { }   — выполнить блок только если не null
if != null  — обычная проверка (даёт smart cast для val)
!!          — когда уверен что не null (использовать редко)
as?         — безопасное приведение типа
takeIf { } — фильтрация в цепочке
filterNotNull() — убрать null из коллекции
lateinit    — отложенная инициализация non-null var
```

```
Когда что использовать:

?.          → всегда как первый вариант для доступа
?:          → когда нужен дефолт или ранний выход (?: return / ?: throw)
?.let       → когда нужно выполнить несколько операций или трансформировать
!!          → только когда архитектурно невозможен null (и готов обосновать)
lateinit    → DI, Fragment binding, тесты
```
