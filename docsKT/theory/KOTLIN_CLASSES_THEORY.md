# Kotlin Classes & Objects — Полная шпаргалка

## Оглавление
1. [Базовые классы](#базовые-классы)
2. [Конструкторы](#конструкторы)
3. [Свойства (Properties)](#свойства-properties)
4. [Методы](#методы)
5. [Модификаторы видимости](#модификаторы-видимости)
6. [Companion Object](#companion-object)
7. [Object (одиночки)](#object-одиночки)
8. [Вложенные и внутренние классы](#вложенные-и-внутренние-классы)
9. [Практические паттерны для Android](#практические-паттерны-для-android)

---

# Базовые классы

## Простейший класс

```kotlin
// Пустой класс
class Empty

// Создание экземпляра (без 'new'!)
val obj = Empty()
```

## Класс с primary constructor

```kotlin
// Самый простой способ
class Person(val name: String, val age: Int)

val alice = Person("Alice", 25)
println(alice.name)  // Alice
println(alice.age)   // 25
```

**Важно:**
- `val` в конструкторе = read-only свойство класса
- `var` в конструкторе = изменяемое свойство класса
- Без `val/var` = просто параметр конструктора (НЕ сохраняется)

```kotlin
class Example(
    val a: Int,      // ✅ свойство (read-only)
    var b: Int,      // ✅ свойство (mutable)
    c: Int           // ❌ просто параметр, не доступен после создания
) {
    init {
        println(c)   // ✅ доступен здесь
    }

    fun test() {
        // println(c)  // ❌ ОШИБКА - c не свойство!
        println(a)   // ✅ OK
        println(b)   // ✅ OK
    }
}
```

## Класс со значениями по умолчанию

```kotlin
class User(
    val name: String,
    val age: Int = 18,           // значение по умолчанию
    val country: String = "Russia"
)

val user1 = User("Bob")                    // age=18, country="Russia"
val user2 = User("Alice", 25)              // country="Russia"
val user3 = User("Charlie", 30, "USA")
val user4 = User("Diana", country = "UK")  // именованные аргументы
```

---

# Конструкторы

## Primary Constructor (основной)

```kotlin
// Полный синтаксис
class Person constructor(val name: String, val age: Int) {
    // тело класса
}

// Обычно 'constructor' опускают
class Person(val name: String, val age: Int)

// С модификатором видимости (тогда 'constructor' нужен)
class Person private constructor(val name: String)
```

## Init блок

Выполняется сразу после primary constructor.

```kotlin
class Person(val name: String, val age: Int) {
    init {
        println("Person created: $name, age $age")

        // Валидация
        require(age >= 0) { "Age cannot be negative" }
        require(name.isNotBlank()) { "Name cannot be empty" }
    }

    // Можно несколько init блоков (выполняются по порядку)
    init {
        println("Second init block")
    }
}
```

**`require` vs `check` vs `assert`:**
```kotlin
require(age >= 0) { "..." }   // для проверки аргументов (IllegalArgumentException)
check(isInitialized) { "..." } // для проверки состояния (IllegalStateException)
assert(condition) { "..." }    // только в debug режиме
```

## Secondary Constructor (дополнительный)

Должен вызывать primary constructor через `this()`.

```kotlin
class Person(val name: String, val age: Int) {
    var email: String = ""

    // Secondary constructor
    constructor(name: String, age: Int, email: String) : this(name, age) {
        this.email = email
        println("Secondary constructor called")
    }
}

val person1 = Person("Alice", 25)                    // primary
val person2 = Person("Bob", 30, "bob@mail.com")      // secondary
```

**Когда нужен secondary constructor:**
- Дополнительные параметры, которые не должны быть в primary constructor
- Разная логика инициализации
- Но чаще можно обойтись значениями по умолчанию!

```kotlin
// Вместо secondary constructor лучше использовать:
class Person(
    val name: String,
    val age: Int,
    val email: String = ""  // значение по умолчанию
)
```

## Несколько secondary конструкторов

```kotlin
class Rectangle {
    var width: Int = 0
    var height: Int = 0

    // Конструктор с шириной и высотой
    constructor(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    // Конструктор для квадрата
    constructor(side: Int) : this(side, side)
}

val rect = Rectangle(10, 20)
val square = Rectangle(10)  // квадрат 10x10
```

---

# Свойства (Properties)

## Базовые свойства

```kotlin
class Person {
    val name: String = "Unknown"  // read-only
    var age: Int = 0              // mutable
}
```

## Вычисляемые свойства (custom getter)

```kotlin
class Rectangle(val width: Int, val height: Int) {
    // Вычисляется каждый раз при обращении
    val area: Int
        get() = width * height

    // Или в одну строку
    val perimeter: Int get() = 2 * (width + height)
}

val rect = Rectangle(5, 10)
println(rect.area)       // 50 (вычисляется)
println(rect.perimeter)  // 30 (вычисляется)
```

**Нет backing field** — значение не хранится, а вычисляется.

## Custom getter и setter

```kotlin
class Temperature {
    var celsius: Double = 0.0
        set(value) {
            require(value >= -273.15) { "Below absolute zero!" }
            field = value  // 'field' = backing field
        }
}

val temp = Temperature()
temp.celsius = 25.0  // OK
// temp.celsius = -300.0  // Exception!
```

**`field`** — ключевое слово для доступа к backing field внутри getter/setter.

## Преобразование значений в setter/getter

```kotlin
class Person {
    var name: String = ""
        get() = field.uppercase()        // всегда возвращает UPPERCASE
        set(value) {
            field = value.trim()         // сохраняет с обрезанными пробелами
        }
}

val person = Person()
person.name = "  alice  "
println(person.name)  // "ALICE" (trim в setter, uppercase в getter)
```

## Свойство с преобразованием типа

```kotlin
class Temperature {
    private var celsius: Double = 0.0

    var fahrenheit: Double
        get() = celsius * 1.8 + 32
        set(value) {
            celsius = (value - 32) / 1.8
        }
}

val temp = Temperature()
temp.fahrenheit = 68.0
println(temp.fahrenheit)  // 68.0
// celsius внутри = 20.0
```

## Late-initialized properties

```kotlin
class MyTest {
    // lateinit - инициализация позже (только var, не nullable)
    lateinit var dependency: SomeDependency

    fun setup() {
        dependency = SomeDependency()
    }

    fun test() {
        // Проверка инициализации
        if (::dependency.isInitialized) {
            dependency.doSomething()
        }
    }
}
```

**Использование в Android:**
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
    }
}
```

## Lazy properties

```kotlin
class DataManager {
    // Инициализируется при первом обращении
    val database: Database by lazy {
        println("Initializing database...")
        Database.connect()  // выполнится только один раз
    }
}

val manager = DataManager()
// database ещё не создана

manager.database.query()  // "Initializing database..." + запрос
manager.database.query()  // только запрос (уже создана)
```

**`lazy` vs `lateinit`:**
- `lazy` — для `val`, инициализация автоматическая при первом обращении
- `lateinit` — для `var`, инициализация вручную

---

# Методы

## Обычные методы

```kotlin
class Calculator {
    fun add(a: Int, b: Int): Int {
        return a + b
    }

    // Single-expression
    fun multiply(a: Int, b: Int) = a * b

    // Без возвращаемого значения
    fun printSum(a: Int, b: Int) {
        println(a + b)
    }
}
```

## Методы со значениями по умолчанию

```kotlin
class Logger {
    fun log(message: String, level: String = "INFO", timestamp: Boolean = true) {
        val time = if (timestamp) "[${System.currentTimeMillis()}]" else ""
        println("$time [$level] $message")
    }
}

val logger = Logger()
logger.log("Hello")                           // level=INFO, timestamp=true
logger.log("Error!", "ERROR")                 // timestamp=true
logger.log("Debug", timestamp = false)        // level=INFO
```

## Перегрузка методов

```kotlin
class Printer {
    fun print(value: Int) {
        println("Int: $value")
    }

    fun print(value: String) {
        println("String: $value")
    }

    fun print(values: List<Int>) {
        println("List: $values")
    }
}

val printer = Printer()
printer.print(42)              // Int: 42
printer.print("Hello")         // String: Hello
printer.print(listOf(1, 2))    // List: [1, 2]
```

## Infix функции

```kotlin
class Point(val x: Int, val y: Int) {
    infix fun moveTo(other: Point): Point {
        return Point(other.x, other.y)
    }
}

val p1 = Point(0, 0)
val p2 = Point(10, 20)
val p3 = p1 moveTo p2  // вместо p1.moveTo(p2)
```

## Operator overloading

```kotlin
class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun times(scale: Int) = Point(x * scale, y * scale)

    override fun toString() = "Point($x, $y)"
}

val p1 = Point(1, 2)
val p2 = Point(3, 4)
val p3 = p1 + p2        // Point(4, 6)
val p4 = p1 * 3         // Point(3, 6)
```

---

# Модификаторы видимости

## Все модификаторы

| Модификатор | Видимость |
|-------------|-----------|
| `public` (по умолчанию) | Везде |
| `private` | Только внутри класса/файла |
| `protected` | В классе и подклассах |
| `internal` | В пределах модуля |

## Public (по умолчанию)

```kotlin
class Person {
    val name: String = "Alice"  // public
    fun greet() = "Hello!"      // public
}

val person = Person()
println(person.name)    // ✅ OK
println(person.greet()) // ✅ OK
```

## Private

```kotlin
class BankAccount(private val accountNumber: String) {
    private var balance: Double = 0.0

    fun deposit(amount: Double) {
        if (amount > 0) {
            balance += amount  // ✅ доступ к private свойству
        }
    }

    fun getInfo(): String {
        return "Account: $accountNumber, Balance: $balance"  // ✅ OK
    }
}

val account = BankAccount("123456")
account.deposit(100.0)
// println(account.balance)       // ❌ ОШИБКА
// println(account.accountNumber) // ❌ ОШИБКА
println(account.getInfo())        // ✅ OK
```

## Protected

```kotlin
open class Animal {
    protected var energy: Int = 100

    fun status() {
        println("Energy: $energy")  // ✅ доступ
    }
}

class Dog : Animal() {
    fun run() {
        energy -= 10  // ✅ доступ в подклассе
        println("Running! Energy: $energy")
    }
}

val dog = Dog()
dog.run()
// dog.energy  // ❌ ОШИБКА - protected недоступен снаружи
```

## Internal (модуль)

```kotlin
// Файл в модуле :app
internal class DatabaseHelper {
    internal fun query() = "Data"
}

internal val config = "secret"

// В другом файле модуля :app
val helper = DatabaseHelper()  // ✅ OK - тот же модуль
helper.query()                 // ✅ OK
println(config)                // ✅ OK

// В модуле :feature-login
// val helper = DatabaseHelper()  // ❌ ОШИБКА - другой модуль
```

**Когда использовать:**
- `private` — скрыть детали реализации внутри класса
- `protected` — для наследования (будет в следующих темах)
- `internal` — для внутреннего API модуля
- `public` — для публичного API

---

# Companion Object

Аналог `static` из Java — члены, принадлежащие классу, а не экземпляру.

## Базовое использование

```kotlin
class MathUtils {
    companion object {
        const val PI = 3.14159  // константа времени компиляции

        fun square(x: Int) = x * x

        fun isPrime(n: Int): Boolean {
            if (n < 2) return false
            for (i in 2..Math.sqrt(n.toDouble()).toInt()) {
                if (n % i == 0) return false
            }
            return true
        }
    }
}

// Использование
val pi = MathUtils.PI
val result = MathUtils.square(5)
val prime = MathUtils.isPrime(17)
```

## const val vs val

```kotlin
class Constants {
    companion object {
        const val MAX_SIZE = 100        // ✅ константа времени компиляции
        val API_URL = getUrl()          // ✅ вычисляется в runtime
        // const val DYNAMIC = getUrl() // ❌ ОШИБКА - const только для примитивов/String
    }
}
```

**`const val`:**
- Только для примитивов и String
- Значение известно во время компиляции
- Встраивается компилятором (быстрее)

## Factory методы

```kotlin
class User private constructor(val id: Int, val name: String) {
    companion object {
        private var nextId = 1

        fun create(name: String): User {
            return User(nextId++, name)
        }

        fun createGuest(): User {
            return User(0, "Guest")
        }
    }
}

// Primary constructor приватный, создаём через фабричные методы
val user1 = User.create("Alice")    // id = 1
val user2 = User.create("Bob")      // id = 2
val guest = User.createGuest()      // id = 0
// val bad = User(5, "Bad")  // ❌ ОШИБКА - конструктор private
```

## Именованный companion object

```kotlin
class MyClass {
    companion object Factory {
        fun create(): MyClass = MyClass()
    }
}

// Можно вызывать по имени или без
val obj1 = MyClass.create()         // без имени
val obj2 = MyClass.Factory.create() // с именем
```

## Практика для Android

```kotlin
class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
        private const val REQUEST_CODE = 100
        const val EXTRA_USER_ID = "user_id"  // для Intent extras

        // Factory метод для создания Intent
        fun newIntent(context: Context, userId: String): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(EXTRA_USER_ID, userId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = intent.getStringExtra(EXTRA_USER_ID)
        Log.d(TAG, "User ID: $userId")
    }
}

// Использование
val intent = MainActivity.newIntent(context, "12345")
startActivity(intent)
```

**Fragment factory:**
```kotlin
class UserFragment : Fragment() {
    companion object {
        private const val ARG_USER_ID = "user_id"

        fun newInstance(userId: String): UserFragment {
            return UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USER_ID, userId)
                }
            }
        }
    }
}
```

---

# Object (одиночки)

`object` = singleton (один экземпляр на всё приложение).

## Object declaration

```kotlin
object Database {
    private var connection: String? = null

    fun connect(url: String) {
        connection = url
        println("Connected to $url")
    }

    fun query(sql: String): String {
        return "Result from $connection: $sql"
    }
}

// Использование
Database.connect("localhost:5432")
val result = Database.query("SELECT * FROM users")

// Нельзя создать экземпляр
// val db = Database()  // ❌ ОШИБКА
```

**Object vs Class с companion object:**
- `object` — весь класс singleton
- `class` с `companion object` — обычный класс + статические члены

## Object expression (анонимный класс)

```kotlin
interface ClickListener {
    fun onClick(x: Int, y: Int)
}

val listener = object : ClickListener {
    override fun onClick(x: Int, y: Int) {
        println("Clicked at ($x, $y)")
    }
}

// Или без интерфейса
val counter = object {
    var count = 0
    fun increment() { count++ }
}
```

## Практика для Android

```kotlin
// Singleton для SharedPreferences
object PreferencesManager {
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    fun saveUser(userId: String) {
        prefs.edit().putString("user_id", userId).apply()
    }

    fun getUser(): String? = prefs.getString("user_id", null)
}

// Использование в Application
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PreferencesManager.init(this)
    }
}
```

---

# Вложенные и внутренние классы

## Вложенный класс (nested) — без доступа к outer class

```kotlin
class Outer {
    private val outerProperty = "Outer"

    class Nested {
        fun foo() {
            // println(outerProperty)  // ❌ ОШИБКА - нет доступа к outer
            println("Nested class")
        }
    }
}

val nested = Outer.Nested()  // создаём без экземпляра Outer
nested.foo()
```

## Внутренний класс (inner) — с доступом к outer class

```kotlin
class Outer {
    private val outerProperty = "Outer"

    inner class Inner {
        fun foo() {
            println(outerProperty)  // ✅ OK - доступ к outer
            println(this@Outer.outerProperty)  // явная ссылка
        }
    }
}

val outer = Outer()
val inner = outer.Inner()  // создаём через экземпляр outer
inner.foo()  // "Outer"
```

**Использование в Android (ViewHolder):**

```kotlin
class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) {
            // Доступ к полям MyAdapter через this@MyAdapter
            itemView.setOnClickListener {
                notifyItemChanged(adapterPosition)  // доступ к методам adapter
            }
        }
    }
}
```

---

# Практические паттерны для Android

## 1. Activity с companion object

```kotlin
class UserDetailActivity : AppCompatActivity() {
    companion object {
        private const val EXTRA_USER_ID = "user_id"
        private const val EXTRA_USER_NAME = "user_name"

        fun newIntent(context: Context, userId: String, userName: String): Intent {
            return Intent(context, UserDetailActivity::class.java).apply {
                putExtra(EXTRA_USER_ID, userId)
                putExtra(EXTRA_USER_NAME, userName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = intent.getStringExtra(EXTRA_USER_ID)
        val userName = intent.getStringExtra(EXTRA_USER_NAME)
    }
}

// Использование
startActivity(UserDetailActivity.newIntent(this, "123", "Alice"))
```

## 2. Fragment с arguments

```kotlin
class UserFragment : Fragment() {
    companion object {
        private const val ARG_USER_ID = "user_id"

        fun newInstance(userId: String) = UserFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_USER_ID, userId)
            }
        }
    }

    private val userId: String?
        get() = arguments?.getString(ARG_USER_ID)
}
```

## 3. ViewHolder pattern

```kotlin
class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val products = mutableListOf<Product>()

    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                tvName.text = product.name
                tvPrice.text = "$${product.price}"

                root.setOnClickListener {
                    // Доступ к adapter через this@ProductAdapter
                    onItemClick(product, adapterPosition)
                }
            }
        }
    }

    private fun onItemClick(product: Product, position: Int) {
        // обработка клика
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size
}
```

## 4. Singleton для Repository

```kotlin
object UserRepository {
    private val users = mutableListOf<User>()

    fun addUser(user: User) {
        users.add(user)
    }

    fun getUser(id: String): User? {
        return users.find { it.id == id }
    }

    fun getAllUsers(): List<User> = users.toList()
}
```

## 5. Класс с валидацией

```kotlin
class Email private constructor(val value: String) {
    companion object {
        private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

        fun create(value: String): Email? {
            return if (EMAIL_REGEX.matches(value)) {
                Email(value)
            } else {
                null
            }
        }
    }

    override fun toString() = value
}

// Использование
val email = Email.create("user@example.com")
if (email != null) {
    println("Valid email: $email")
} else {
    println("Invalid email")
}
```

## 6. Builder pattern с apply

```kotlin
class DialogConfig {
    var title: String = ""
    var message: String = ""
    var positiveButton: String = "OK"
    var negativeButton: String = "Cancel"
    var cancelable: Boolean = true

    companion object {
        fun build(block: DialogConfig.() -> Unit): DialogConfig {
            return DialogConfig().apply(block)
        }
    }
}

// Использование
val config = DialogConfig.build {
    title = "Warning"
    message = "Are you sure?"
    positiveButton = "Yes"
    negativeButton = "No"
    cancelable = false
}
```

---

# Полезные Tips

## 1. Предпочитайте val вместо var

```kotlin
// Плохо
class User {
    var name: String = ""
}

// Хорошо
class User(val name: String)
```

## 2. Primary constructor для простых классов

```kotlin
// Плохо
class Point {
    val x: Int
    val y: Int

    constructor(x: Int, y: Int) {
        this.x = x
        this.y = y
    }
}

// Хорошо
class Point(val x: Int, val y: Int)
```

## 3. Используйте require() для валидации

```kotlin
class Age(val value: Int) {
    init {
        require(value >= 0) { "Age cannot be negative: $value" }
        require(value <= 150) { "Age too large: $value" }
    }
}
```

## 4. Private constructor + factory методы

```kotlin
class UserId private constructor(val value: String) {
    companion object {
        fun fromString(value: String): UserId? {
            return if (value.length == 36) UserId(value) else null
        }

        fun random(): UserId {
            return UserId(UUID.randomUUID().toString())
        }
    }
}
```

## 5. Immutable классы (все val)

```kotlin
// Неизменяемый класс - потокобезопасен
class User(
    val id: String,
    val name: String,
    val email: String
) {
    // Методы возвращают новые экземпляры вместо изменения
    fun withName(newName: String) = User(id, newName, email)
    fun withEmail(newEmail: String) = User(id, name, newEmail)
}

val user1 = User("1", "Alice", "alice@mail.com")
val user2 = user1.withName("Bob")  // новый объект
```

---

# Сравнение с Java

| Концепция | Java | Kotlin |
|-----------|------|--------|
| Создание объекта | `new Person()` | `Person()` |
| Static методы | `static void foo()` | `companion object { fun foo() }` |
| Геттеры/сеттеры | `getName() / setName()` | Автоматически (свойства) |
| Singleton | Шаблонный код | `object Singleton` |
| Константы | `public static final` | `const val` |
| Конструкторы | Явные конструкторы | Primary + secondary |

---

*Создано: 2026-02-02*
*Проект: KotlinRepetition*
*Тема: Классы и объекты*
