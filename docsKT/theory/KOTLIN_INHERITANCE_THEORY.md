# Наследование и интерфейсы в Kotlin

---

## 1. Наследование (Inheritance)

### Основы

По умолчанию все классы в Kotlin — **final** (нельзя наследоваться).
Чтобы разрешить наследование — нужно пометить класс как `open`:

```kotlin
open class Animal(val name: String) {
    open fun sound(): String = "..."  // open — можно переопределить
    fun breathe() = "дышит"          // без open — нельзя переопределить
}

class Dog(name: String) : Animal(name) {
    override fun sound(): String = "Гав"  // override обязателен
}

val dog = Dog("Шарик")
println(dog.sound())   // Гав
println(dog.breathe()) // дышит — унаследовано от Animal
```

### Конструкторы при наследовании

```kotlin
open class Person(val name: String, val age: Int)

// Передаём параметры в конструктор родителя
class Employee(name: String, age: Int, val company: String) : Person(name, age)

val emp = Employee("Alice", 30, "Google")
println(emp.name)    // Alice — из Person
println(emp.company) // Google — из Employee
```

### super — обращение к родителю

```kotlin
open class Shape {
    open fun describe() = "Это фигура"
}

class Circle(val radius: Double) : Shape() {
    override fun describe() = super.describe() + ", круг радиуса $radius"
}

println(Circle(5.0).describe())
// Это фигура, круг радиуса 5.0
```

### abstract — абстрактный класс

Нельзя создать экземпляр. Содержит методы без реализации — наследники обязаны их реализовать:

```kotlin
abstract class Vehicle(val brand: String) {
    abstract fun fuelType(): String   // нет реализации — наследник обязан реализовать
    fun describe() = "$brand работает на ${fuelType()}"  // есть реализация
}

class ElectricCar(brand: String) : Vehicle(brand) {
    override fun fuelType() = "электричестве"
}

class GasCar(brand: String) : Vehicle(brand) {
    override fun fuelType() = "бензине"
}

// val v = Vehicle("BMW")  // ОШИБКА — нельзя создать абстрактный класс
val tesla = ElectricCar("Tesla")
println(tesla.describe()) // Tesla работает на электричестве
```

---

## 2. Интерфейсы (Interface)

### Основы

Интерфейс — контракт. Класс обязуется выполнять определённые функции.
Класс может реализовывать **несколько** интерфейсов (в отличие от наследования — только один класс).

```kotlin
interface Clickable {
    fun onClick()                          // без реализации — обязательно реализовать
    fun onLongClick() = println("Долгий клик")  // с реализацией по умолчанию
}

class Button : Clickable {
    override fun onClick() = println("Клик!")
    // onLongClick() — не обязательно переопределять, уже есть реализация
}

val btn = Button()
btn.onClick()      // Клик!
btn.onLongClick()  // Долгий клик
```

### Несколько интерфейсов

```kotlin
interface Flyable {
    fun fly() = println("Летит")
}

interface Swimmable {
    fun swim() = println("Плывёт")
}

class Duck : Flyable, Swimmable {
    // fly() и swim() уже реализованы в интерфейсах
}

val duck = Duck()
duck.fly()   // Летит
duck.swim()  // Плывёт
```

### Интерфейс с свойствами

```kotlin
interface Named {
    val name: String          // без значения — обязательно реализовать
    val greeting: String      // есть getter по умолчанию — переопределять НЕ обязательно
        get() = "Привет, $name"
}

class User(override val name: String) : Named

val user = User("Alice")
println(user.greeting) // Привет, Alice
```

---

## 3. Наследование vs Интерфейс — когда что использовать

| | Наследование (`open class`) | Интерфейс |
|---|---|---|
| Количество | Только один родитель | Несколько интерфейсов |
| Состояние | Может хранить поля | Не хранит состояние (только val без backing field) |
| Конструктор | Есть | Нет |
| Когда использовать | "является чем-то" (Dog is Animal) | "умеет делать что-то" (Duck can Fly) |

```kotlin
// Dog IS AN Animal — наследование
class Dog : Animal("Шарик")

// Duck CAN FLY и CAN SWIM — интерфейсы
class Duck : Flyable, Swimmable
```

---

## 4. Abstract class + Interface вместе

Это самая частая комбинация в реальном коде. Идея простая:
- **Интерфейс** описывает *что умеет делать* объект
- **Abstract class** описывает *чем является* объект и содержит общие поля/логику
- **Конкретный класс** наследует abstract и реализует интерфейс

```kotlin
interface Serializable {
    fun serialize(): String          // каждый реализует по-своему
    fun label(): String = "data"     // default — можно переопределить
}

abstract class Document(val title: String, val author: String) : Serializable {
    abstract fun wordCount(): Int    // наследники обязаны реализовать
    fun summary() = "$title by $author (${wordCount()} слов)"  // общая логика
}

class PdfDocument(title: String, author: String, val pages: Int) : Document(title, author) {
    override fun wordCount() = pages * 250
    override fun serialize() = "PDF:$title:$author:$pages"
}

class TextDocument(title: String, author: String, val content: String) : Document(title, author) {
    override fun wordCount() = content.split(" ").size
    override fun serialize() = "TXT:$title:$content"
}
```

### Что наследуется от кого

```
interface Serializable       ← контракт (serialize, label)
      ↓
abstract class Document      ← общие поля (title, author), общие методы (summary)
      ↓                         абстрактные методы (wordCount) — наследник реализует
class PdfDocument            ← реализует wordCount() и serialize()
class TextDocument           ← реализует wordCount() и serialize()
```

### Полиморфизм работает через оба типа

```kotlin
val docs: List<Document> = listOf(
    PdfDocument("Clean Code", "Martin", 464),
    TextDocument("Notes", "Alice", "some text here")
)

docs.forEach { println(it.summary()) }  // через abstract class

val items: List<Serializable> = docs
items.forEach { println(it.serialize()) }  // через интерфейс
```

### Правило записи — порядок не важен, но convention: сначала класс, потом интерфейсы

```kotlin
class Foo : AbstractBase(), InterfaceA, InterfaceB
```

---

## 5. Полиморфизм

Переменная типа родителя может хранить объект наследника:

```kotlin
open class Animal { open fun sound() = "..." }
class Cat : Animal() { override fun sound() = "Мяу" }
class Dog : Animal() { override fun sound() = "Гав" }

val animals: List<Animal> = listOf(Cat(), Dog(), Cat())
animals.forEach { println(it.sound()) }
// Мяу
// Гав
// Мяу
```

То же с интерфейсами:

```kotlin
interface Drawable { fun draw() }
class Circle : Drawable { override fun draw() = println("○") }
class Square : Drawable { override fun draw() = println("□") }

val shapes: List<Drawable> = listOf(Circle(), Square(), Circle())
shapes.forEach { it.draw() }
```

---

## 6. Проверка типа — is / as

```kotlin
open class Animal
class Cat : Animal()
class Dog : Animal()

val animal: Animal = Cat()

// is — проверка типа (как instanceof в Java)
if (animal is Cat) println("Это кот")

// Smart cast — после is компилятор знает тип
if (animal is Cat) {
    animal.purr()  // можно вызывать методы Cat без явного каста
}

// as — явное приведение типа (опасно — может упасть с ClassCastException)
val cat = animal as Cat

// as? — безопасное приведение (вернёт null если не тот тип)
val dog = animal as? Dog  // вернёт null, не упадёт
println(dog)  // null
```

---

## 7. Android контекст

### Где это используется:

```kotlin
// RecyclerView.ViewHolder — наследование
class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: Item) { ... }
}

// ViewModel — наследование
class MyViewModel : ViewModel() {
    val state = MutableStateFlow(...)
}

// Fragment — наследование
class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { ... }
}

// Интерфейсы — слушатели событий
interface OnItemClickListener {
    fun onItemClick(item: Item)
}

class MyAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<...>()
```

---

## Шпаргалка

```
open class    — можно наследоваться
abstract class — нельзя создать экземпляр, есть абстрактные методы
interface      — контракт, можно реализовывать несколько
override       — переопределяем метод родителя/интерфейса
super          — обращение к родителю
is             — проверка типа
as             — приведение типа
as?            — безопасное приведение (null если не тот тип)
```
