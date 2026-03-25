# Задания: Data Classes & Sealed Classes (15 заданий)

**Проект**: KotlinRepetition
**Дата создания**: 2026-02-17
**Файлы**: `src/datasealed/` (создать папку)

---

## 🟢 УРОВЕНЬ 1: Data Class — основы (4 задания)

### **1.1 Contact — базовый data class**
Создай `data class Contact` с полями: `name: String`, `phone: String`, `email: String`.
В main создай два контакта с одинаковыми данными. Выведи их через `println` и сравни через `==`.
Создай третий контакт — копию первого, но с другим email (используй `copy()`).
Выведи все три контакта.

---

### **1.2 Student — деструктуризация**
Создай `data class Student` с полями: `id: Int`, `name: String`, `grade: Int`.
Создай список из 5 студентов. Пройдись по списку циклом с деструктуризацией `(id, name, grade)` и выведи каждого в формате: `"#1 Alice — grade: 5"`.

---

### **1.3 Settings — copy() для обновления состояния**
Создай `data class Settings` с полями: `theme: String`, `fontSize: Int`, `notificationsEnabled: Boolean`, `language: String`.
Создай дефолтные настройки: theme = "light", fontSize = 14, notifications = true, language = "ru".
Затем создай три варианта настроек через `copy()`:
- Тёмная тема (только theme меняется)
- Крупный шрифт (только fontSize = 20)
- Английский без уведомлений (language + notifications меняются)
Выведи все четыре варианта.

---

### **1.4 Coordinate — свойства вне конструктора**
Создай `data class Coordinate` с полями `x: Double`, `y: Double`.
Добавь свойство `distanceFromOrigin: Double` НЕ в конструктор, а в теле класса (вычисляемое через getter — корень из x² + y²).
В main создай два объекта `Coordinate(3.0, 4.0)` и `Coordinate(3.0, 4.0)`.
Проверь: `==` вернёт `true`? А `toString()` покажет `distanceFromOrigin`? Объясни почему.

---

## 🟡 УРОВЕНЬ 2: Sealed Class — основы (4 задания)

### **2.1 Weather — первый sealed class**
Создай `sealed class Weather` с вариантами:
- `Sunny` — без данных (object)
- `Rainy(val millimeters: Double)` — количество осадков
- `Snowy(val centimeters: Int)` — толщина снега
- `Windy(val speedKmh: Int)` — скорость ветра

Напиши функцию `describeWeather(weather: Weather): String`, которая через `when` возвращает описание для каждого варианта.
В main вызови с каждым вариантом и выведи результат.

---

### **2.2 Shape — площадь через sealed class**
Создай `sealed class Shape` с вариантами:
- `Circle(val radius: Double)`
- `Rectangle(val width: Double, val height: Double)`
- `Triangle(val base: Double, val height: Double)`

Напиши функцию `calculateArea(shape: Shape): Double`, которая вычисляет площадь.
Формулы: круг = π × r², прямоугольник = w × h, треугольник = base × h / 2.
В main создай по одной фигуре каждого типа и выведи площадь.

---

### **2.3 OrderStatus — статусы заказа**
Создай `sealed class OrderStatus` с вариантами:
- `Created(val orderId: String)` — заказ создан
- `Processing` — в обработке (object)
- `Shipped(val trackingNumber: String)` — отправлен
- `Delivered(val deliveryDate: String)` — доставлен
- `Cancelled(val reason: String)` — отменён

Напиши функцию `getStatusMessage(status: OrderStatus): String`, которая возвращает сообщение для пользователя.
В main проведи заказ через все стадии: Created → Processing → Shipped → Delivered. И отдельно покажи отменённый заказ.

---

### **2.4 MathResult — результат вычисления**
Создай `sealed class MathResult` с вариантами:
- `Success(val value: Double)` — результат
- `DivisionByZero` — деление на ноль (object)
- `NegativeRoot` — корень из отрицательного числа (object)

Напиши функцию `divide(a: Double, b: Double): MathResult` — возвращает Success или DivisionByZero.
Напиши функцию `squareRoot(n: Double): MathResult` — возвращает Success или NegativeRoot.
Напиши функцию `showResult(result: MathResult): String` — красивый вывод.
В main протестируй: `divide(10, 3)`, `divide(5, 0)`, `squareRoot(16)`, `squareRoot(-4)`.

---

## 🔴 УРОВЕНЬ 3: Комбинации и практика (4 задания)

### **3.1 Response<T> — generic sealed class**
Создай `sealed class Response<out T>` с вариантами:
- `Success<T>(val data: T)` — данные
- `Error(val message: String, val code: Int)` — ошибка
- `Loading` — загрузка (object)

Напиши функцию `handleResponse(response: Response<String>)`, которая через when выводит:
- Success → "Данные: ..."
- Error → "Ошибка [код]: сообщение"
- Loading → "Загрузка..."

В main вызови с каждым вариантом.

---

### **3.2 Inventory — data class + коллекции**
Создай `data class Product(val id: Int, val name: String, val price: Double, val category: String)`.
Создай список из 8+ товаров разных категорий.
Используя трансформации коллекций (filter, map, groupBy, sortedBy — то что учил в теме 5):
- Найди все товары дороже 1000
- Сгруппируй товары по категориям
- Найди самый дорогой товар в каждой категории
- Посчитай среднюю цену всех товаров

---

### **3.3 Messenger — sealed + data class вместе**
Создай `data class User(val id: Int, val name: String)`.
Создай `sealed class Message`:
- `Text(val from: User, val content: String)`
- `Image(val from: User, val url: String, val sizeKb: Int)`
- `Voice(val from: User, val durationSeconds: Int)`
- `SystemMessage(val content: String)` — без отправителя

Напиши функцию `formatMessage(message: Message): String`, которая форматирует сообщение.
В main создай список из разных сообщений и выведи каждое через `formatMessage`.

---

### **3.4 Calculator — sealed для операций**
Создай `sealed class Operation`:
- `Add(val a: Double, val b: Double)`
- `Subtract(val a: Double, val b: Double)`
- `Multiply(val a: Double, val b: Double)`
- `Divide(val a: Double, val b: Double)`

Напиши функцию `execute(op: Operation): Double` (для деления на 0 — throw).
Напиши функцию `describe(op: Operation): String` — возвращает строку вида `"5.0 + 3.0 = 8.0"`.
В main создай список операций и выведи описание + результат для каждой.

---

## ⭐ УРОВЕНЬ 4: Продвинутый (3 задания)

### **4.1 StateManager — имитация ViewModel**
Создай `data class AppState(val count: Int, val name: String, val isLoggedIn: Boolean)`.
Создай `sealed class Action`:
- `Increment`
- `Decrement`
- `SetName(val name: String)`
- `Login`
- `Logout`

Напиши функцию `reduce(state: AppState, action: Action): AppState`, которая:
- Принимает текущий state и action
- Возвращает **новый** state через `copy()` (НЕ изменяет старый!)
- Для Increment — count + 1, Decrement — count - 1, и т.д.

В main создай начальный state и проведи через серию actions, выводя state после каждого шага.

---

### **4.2 Парсер JSON-like — вложенный sealed class**
Создай `sealed class JsonValue`:
- `JsonString(val value: String)`
- `JsonNumber(val value: Double)`
- `JsonBool(val value: Boolean)`
- `JsonNull` (object)
- `JsonArray(val items: List<JsonValue>)`

Напиши функцию `stringify(value: JsonValue): String`, которая превращает JsonValue в строковое представление:
- JsonString → `"hello"`
- JsonNumber → `42.0`
- JsonBool → `true`
- JsonNull → `null`
- JsonArray → `["hello", 42.0, true, null]`

В main создай вложенную структуру (массив с разными типами) и выведи через stringify.

---

### **4.3 Pipeline — цепочка обработки**
Создай `sealed class Step`:
- `Filter(val predicate: (Int) -> Boolean)` — фильтрация
- `Transform(val mapper: (Int) -> Int)` — преобразование
- `Sort(val ascending: Boolean)` — сортировка

Напиши функцию `executePipeline(data: List<Int>, steps: List<Step>): List<Int>`, которая последовательно применяет все шаги к данным.

В main создай список чисел и pipeline из нескольких шагов:
1. Отфильтровать только положительные
2. Умножить каждое на 2
3. Отсортировать по убыванию

Выведи результат.

---

## Прогресс

| Уровень | Задания | Статус |
|---------|---------|--------|
| 1. Data Class основы | 4/4 | ✅ |
| 2. Sealed Class основы | 4/4 | ✅ |
| 3. Комбинации | 4/4 | ✅ |
| 4. Продвинутый | 0/3 | ⏸️ |
| **Итого** | **12/15** | |

### Детальный прогресс

#### ✅ Уровень 1: Data Class основы (4/4)
- ✅ 1.1 Contact — базовый data class
- ✅ 1.2 Student — деструктуризация
- ✅ 1.3 Settings — copy() для обновления состояния
- ✅ 1.4 Coordinate — свойства вне конструктора

#### ✅ Уровень 2: Sealed Class основы (4/4)
- ✅ 2.1 Weather — первый sealed class
- ✅ 2.2 Shape — площадь через sealed class
- ✅ 2.3 OrderStatus — статусы заказа
- ✅ 2.4 MathResult — divide, squareRoot, showResult

#### ✅ Уровень 3: Комбинации (4/4)
- ✅ 3.1 Response\<T\> — generic sealed class
- ✅ 3.2 Inventory — data class + коллекции
- ✅ 3.3 Messenger — sealed + data class
- ✅ 3.4 Calculator — sealed для операций

#### ✅ Уровень 4: Продвинутый (1/3)
- ✅ **4.1 StateManager — 9/10 ⚠️ СЛАБОЕ МЕСТО** (выполнил но понимание слабое — reduce/MVI паттерн, зачем это нужно)
- 🤖 **4.2 Парсер JSON-like — ПРОЙТИ В ANDROID ПРОЕКТЕ** (реальный JSON через Gson/Moshi, sealed для парсинга ответов API)
- 🤖 **4.3 Pipeline — ПРОЙТИ В ANDROID ПРОЕКТЕ** (реальные pipeline через Flow операторы: filter, map, transform)
- 🤖 **4.1 StateManager (повтор) — ПРОЙТИ В ANDROID ПРОЕКТЕ** (ViewModel + StateFlow + MVI архитектура на реальном экране)
