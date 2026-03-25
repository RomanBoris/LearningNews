# Задания: Наследование и интерфейсы (14 заданий)

**Проект**: KotlinRepetition
**Дата создания**: 2026-02-28
**Файлы**: `src/inheritance/` (создать папку)
**Теория**: `KOTLIN_INHERITANCE_THEORY.md`

---

## 🟢 УРОВЕНЬ 1: Наследование — основы (4 задания)

### **1.1 Animal — первое наследование**
Создай `open class Animal` с параметром `name: String` и `open fun sound(): String = "..."`.

Создай классы `Dog` и `Cat`, которые наследуются от `Animal` и переопределяют `sound()`:
- Dog возвращает `"Гав"`
- Cat возвращает `"Мяу"`

В main создай список `List<Animal>` с несколькими собаками и кошками.
Пройдись циклом и выведи: `"Шарик говорит: Гав"`.

---

### **1.2 Shape — свойства и переопределение**
Создай `open class Shape` с `open val name: String = "Фигура"` и `open fun area(): Double = 0.0`.

Создай:
- `class Circle(val radius: Double) : Shape()` — переопределяет `name = "Круг"` и `area()` (π × r²)
- `class Rectangle(val width: Double, val height: Double) : Shape()` — переопределяет `name = "Прямоугольник"` и `area()` (w × h)

В main создай список фигур, выведи для каждой: `"Круг: площадь = 78.54"`.

---

### **1.3 Vehicle — super и цепочка конструкторов**
Создай `open class Vehicle(val brand: String, val year: Int)` с `open fun describe(): String`.

Создай `class Car(brand: String, year: Int, val doors: Int) : Vehicle(brand, year)`.
Переопредели `describe()` так чтобы она вызывала `super.describe()` и добавляла `"$doors дверей"`.

Создай `class Truck(brand: String, year: Int, val payload: Double) : Vehicle(brand, year)`.
Переопредели `describe()` — добавляет `"грузоподъёмность $payload т"`.

В main создай несколько машин и грузовиков, выведи описание каждого.

---

### **1.4 Person — цепочка наследования**
Создай `open class Person(val name: String, val age: Int)` с `open fun introduce(): String`.

Создай `open class Employee(name: String, age: Int, val company: String) : Person(name, age)`.
Переопредели `introduce()` — добавляет компанию.

Создай `class Manager(name: String, age: Int, company: String, val teamSize: Int) : Employee(name, age, company)`.
Переопредели `introduce()` — добавляет размер команды.

В main создай Person, Employee, Manager. Сохрани их в `List<Person>` и выведи `introduce()` для каждого.

---

## 🟡 УРОВЕНЬ 2: Abstract + Interface — основы (4 задания)

### **2.1 Abstract Notification — абстрактный класс**
Создай `abstract class Notification(val title: String, val message: String)`.
Добавь абстрактный метод `fun deliver(): String` (без реализации).
Добавь обычный метод `fun preview(): String = "[$title]: $message"` (с реализацией).

Создай:
- `class PushNotification(title: String, message: String, val deviceToken: String) : Notification(...)` — `deliver()` возвращает строку о пуш-отправке
- `class EmailNotification(title: String, message: String, val email: String) : Notification(...)` — `deliver()` возвращает строку об отправке на email
- `class SmsNotification(title: String, message: String, val phone: String) : Notification(...)` — `deliver()` возвращает строку об SMS

В main создай список `List<Notification>`, для каждого вызови `deliver()` и `preview()`.

---

### **2.2 Printable — первый интерфейс**
Создай интерфейс `Printable` с методом `fun printInfo(): String` (без реализации).

Создай:
- `data class Book(val title: String, val author: String) : Printable` — `printInfo()` возвращает `"Книга: $title — $author"`
- `data class Movie(val title: String, val year: Int) : Printable` — `printInfo()` возвращает `"Фильм: $title ($year)"`
- `data class Song(val title: String, val artist: String, val durationSec: Int) : Printable` — своё описание

В main создай список `List<Printable>` из разных объектов и вызови `printInfo()` для каждого.

---

### **2.3 Validatable — интерфейс с default методом**
Создай интерфейс `Validatable`:
- `fun validate(): Boolean` (без реализации — каждый реализует сам)
- `fun validationMessage(): String = if (validate()) "Валидно" else "Не валидно"` (реализация по умолчанию)

Создай:
- `data class Email(val value: String) : Validatable` — валидна если содержит `@` и `.`
- `data class PhoneNumber(val value: String) : Validatable` — валиден если содержит только цифры и длина 10-12 символов
- `data class Password(val value: String) : Validatable` — валиден если длина >= 8

В main создай несколько объектов (валидных и нет), выведи `validationMessage()` для каждого.

---

### **2.4 Abstract + Interface вместе**
Создай интерфейс `Discountable`:
- `fun getDiscount(): Double` — процент скидки (0.0 до 1.0)
- `fun priceWithDiscount(original: Double): Double = original * (1 - getDiscount())` — default метод

Создай `abstract class Product(val name: String, val price: Double) : Discountable`.
Добавь абстрактный метод `fun category(): String`.

Создай:
- `class FoodProduct(name: String, price: Double) : Product(name, price)` — скидка 5%, категория "Еда"
- `class ElectronicsProduct(name: String, price: Double) : Product(name, price)` — скидка 15%, категория "Электроника"
- `class ClothingProduct(name: String, price: Double) : Product(name, price)` — скидка 20%, категория "Одежда"

В main создай список продуктов, выведи для каждого: `"iPhone [Электроника]: 1000.0 → 850.0 (скидка 15%)"`.

---

## 🔴 УРОВЕНЬ 3: Полиморфизм и несколько интерфейсов (3 задания)

### **3.1 Duck — несколько интерфейсов**
Создай интерфейсы:
- `Flyable` с методом `fun fly(): String`
- `Swimmable` с методом `fun swim(): String`
- `Walkable` с методом `fun walk(): String = "Идёт пешком"` (default реализация)

Создай классы:
- `class Duck : Flyable, Swimmable, Walkable` — летит "машет крыльями", плывёт "гребёт лапками"
- `class Fish : Swimmable` — плывёт "виляет хвостом"
- `class Eagle : Flyable, Walkable` — летит "парит в небе"

В main создай список `List<Swimmable>`, добавь туда всех кто умеет плавать. Вызови `swim()` для каждого.

---

### **3.2 Sensor — is и as**
Создай `open class Sensor(val id: String)` с `open fun read(): Double = 0.0`.

Создай:
- `class TemperatureSensor(id: String) : Sensor(id)` с полем `val unit: String = "°C"` и override `read()` — случайное число 15.0–35.0
- `class PressureSensor(id: String) : Sensor(id)` с полем `val unit: String = "hPa"` и override `read()`— случайное число 950.0–1050.0
- `class HumiditySensor(id: String) : Sensor(id)` с override `read()` — случайное число 30.0–90.0

Напиши функцию `describeSensor(sensor: Sensor): String` которая:
- Через `is` проверяет тип
- Для TemperatureSensor выводит значение с единицей измерения (`"Температура: 23.5°C"`)
- Для PressureSensor аналогично
- Для остальных — `"Сенсор ${sensor.id}: ${sensor.read()}"`

В main создай список `List<Sensor>` из разных сенсоров и выведи описание каждого.

---

### **3.3 Logger — интерфейс как тип параметра**
Создай интерфейс `Logger`:
- `fun log(message: String)`
- `fun warn(message: String)`
- `fun error(message: String)`

Создай реализации:
- `class ConsoleLogger : Logger` — выводит в println с префиксами `[LOG]`, `[WARN]`, `[ERROR]`
- `class SilentLogger : Logger` — ничего не делает (заглушка для тестов)

Создай `class UserService(private val logger: Logger)` с методами:
- `fun createUser(name: String)` — логирует создание пользователя
- `fun deleteUser(id: Int)` — логирует удаление, если id < 0 — логирует ошибку

В main создай UserService с ConsoleLogger и протестируй. Потом замени на SilentLogger — код не меняется, только логгер.

---

## ⭐ УРОВЕНЬ 4: Реальные паттерны (3 задания)

### **4.1 Repository — главный Android паттерн**
Создай `data class User(val id: Int, val name: String, val email: String)`.

Создай интерфейс `UserRepository`:
- `fun getById(id: Int): User?`
- `fun getAll(): List<User>`
- `fun save(user: User)`
- `fun delete(id: Int): Boolean`

Создай `class InMemoryUserRepository : UserRepository` — хранит данные в `mutableListOf<User>()`.

В main создай репозиторий, добавь несколько пользователей, найди по id, удали, выведи всех.

> Это основа Android архитектуры: ViewModel работает с Repository через интерфейс.
> В реальном проекте InMemoryRepository заменяется на RoomRepository без изменения ViewModel.

---

### **4.2 EventBus — Observer паттерн**
Создай `interface EventListener<T>` с методом `fun onEvent(event: T)`.

Создай `sealed class AppEvent`:
- `data class UserLoggedIn(val username: String)`
- `data class UserLoggedOut(val username: String)`
- `data class ErrorOccurred(val message: String)`

Создай `class EventBus` с методами:
- `fun subscribe(listener: EventListener<AppEvent>)` — добавить слушателя
- `fun publish(event: AppEvent)` — разослать событие всем слушателям

Создай несколько слушателей (AnalyticsListener, LogListener) и подпиши их на EventBus.
Опубликуй несколько событий, убедись что все слушатели их получают.

---

### **4.3 Strategy — стратегии сортировки**
Создай интерфейс `SortStrategy<T>` с методом `fun sort(items: List<T>): List<T>`.

Создай реализации для `List<Int>`:
- `class AscendingSort : SortStrategy<Int>` — по возрастанию
- `class DescendingSort : SortStrategy<Int>` — по убыванию
- `class AbsoluteValueSort : SortStrategy<Int>` — по абсолютному значению

Создай `class Sorter<T>(private var strategy: SortStrategy<T>)` с методами:
- `fun sort(items: List<T>): List<T>` — применяет текущую стратегию
- `fun changeStrategy(strategy: SortStrategy<T>)` — менять стратегию

В main создай список чисел и примени все три стратегии. Покажи что можно менять стратегию не меняя основной код.

---

## Прогресс

| Уровень | Задания | Статус |
|---------|---------|--------|
| 1. Наследование основы | 0/4 | ⏳ |
| 2. Abstract + Interface | 0/4 | ⏸️ |
| 3. Полиморфизм | 0/3 | ⏸️ |
| 4. Реальные паттерны | 0/3 | ⏸️ |
| **Итого** | **0/14** | |
