# Задания: Lambda и Higher-order функции (10 заданий)

**Проект**: KotlinRepetition
**Файлы**: `src/lambdas/`
**Теория**: `KOTLIN_LAMBDA_THEORY.md`

---

## 🟢 УРОВЕНЬ 1: Основы (3 задания)

### **1.1 Лямбды как переменные**
Объяви следующие лямбды в переменных и вызови каждую в main:
- `square` — возводит Int в квадрат
- `isPositive` — проверяет что Int > 0
- `greet` — принимает имя (String) и возвращает `"Hello, $name!"`
- `add` — принимает два Int, возвращает их сумму

---

### **1.2 Higher-order функции**
Напиши следующие функции:
- `fun applyToAll(list: List<Int>, transform: (Int) -> Int): List<Int>` — применяет transform к каждому элементу
- `fun filterWith(list: List<Int>, predicate: (Int) -> Boolean): List<Int>` — фильтрует список
- `fun printEach(list: List<String>, printer: (String) -> Unit)` — вызывает printer для каждого элемента

В main протестируй каждую с разными лямбдами.

---

### **1.3 Возврат функции**
Напиши:
- `fun multiplier(factor: Int): (Int) -> Int` — возвращает функцию умножения на factor
- `fun adder(base: Int): (Int) -> Int` — возвращает функцию прибавления base
- `fun validator(min: Int, max: Int): (Int) -> Boolean` — возвращает функцию проверки что число в диапазоне [min, max]

В main создай несколько функций через эти фабрики и вызови их.

---

## 🟡 УРОВЕНЬ 2: Комбинации (3 задания)

### **2.1 Работа с коллекциями**
Есть список:
```kotlin
data class Product(val name: String, val price: Double, val category: String)

val products = listOf(
    Product("Phone", 999.0, "Electronics"),
    Product("Shirt", 29.99, "Clothes"),
    Product("Laptop", 1499.0, "Electronics"),
    Product("Jeans", 59.99, "Clothes"),
    Product("Tablet", 599.0, "Electronics")
)
```
Используя `filter`, `map`, `sortedBy`, `groupBy`, ответь:
1. Названия всех продуктов дороже 100.0
2. Средняя цена Electronics
3. Продукты сгруппированные по категории
4. Самый дешёвый продукт в каждой категории

---

### **2.2 fold и reduce**
Напиши функции **без** использования встроенных `sum`, `max`, `min`, `joinToString`:
- `fun sumList(list: List<Int>): Int` — через `fold`
- `fun maxList(list: List<Int>): Int?` — через `reduce` (null если пустой)
- `fun joinWith(list: List<String>, separator: String): String` — через `fold`

---

### **2.3 Функции с callback**
Напиши:
```kotlin
fun runIf(condition: Boolean, action: () -> Unit)
fun runWithRetry(times: Int, action: () -> Boolean): Boolean
// runWithRetry запускает action до times раз, возвращает true если хоть раз вернул true
```

В main продемонстрируй использование.

---

## 🔴 УРОВЕНЬ 3: Продвинутое (4 задания)

### **3.1 Цепочки трансформаций**
Напиши функцию:
```kotlin
fun <T, R> List<T>.transform(
    filter: (T) -> Boolean,
    mapper: (T) -> R,
    sorter: Comparator<R>
): List<R>
```
Функция фильтрует, трансформирует и сортирует список.
Протестируй на `List<Product>` из задания 2.1.

---

### **3.2 Мемоизация**
Напиши функцию `fun memoize(f: (Int) -> Int): (Int) -> Int` — возвращает версию функции `f`, которая кэширует результаты (при повторном вызове с тем же аргументом не вычисляет заново).

Протестируй на медленной функции (добавь `println` внутрь чтобы видеть вызовы).

---

### **3.3 Compose**
Напиши:
```kotlin
fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C
```
`compose(f, g)(x)` = `f(g(x))`

Пример:
```kotlin
val doubleAndAddOne = compose({ x: Int -> x + 1 }, { x: Int -> x * 2 })
println(doubleAndAddOne(3)) // (3 * 2) + 1 = 7
```

---

### **3.4 Event система**
Напиши простой event bus:
```kotlin
class EventBus {
    fun <T> subscribe(eventType: String, handler: (T) -> Unit)
    fun <T> publish(eventType: String, event: T)
}
```
В main подпишись на несколько событий разных типов и опубликуй их.

---

## Прогресс

| Уровень | Задания | Статус |
|---------|---------|--------|
| 1. Основы | 0/3 | ⏸️ |
| 2. Комбинации | 0/3 | ⏸️ |
| 3. Продвинутое | 0/4 | ⏸️ |
| **Итого** | **0/10** | |
