# Kotlin Collections — Полная шпаргалка

## Оглавление
1. [List](#list)
2. [Set](#set)
3. [Map](#map)
4. [Трансформации](#трансформации)
5. [Задачи с собеседований](#задачи-с-собеседований)

---

# List

## Создание

```kotlin
// Неизменяемый список
val fruits = listOf("apple", "banana", "cherry")
val empty = emptyList<String>()
val nullableList = listOfNotNull("a", null, "b")  // ["a", "b"]

// Изменяемый список
val mutableFruits = mutableListOf("apple", "banana")
val arrayList = arrayListOf("a", "b", "c")

// Создание с генератором
val squares = List(5) { it * it }  // [0, 1, 4, 9, 16]
```

## Доступ к элементам

```kotlin
val list = listOf("a", "b", "c", "d", "e")

// Базовый доступ
list[0]              // "a"
list.get(1)          // "b"
list.first()         // "a"
list.last()          // "e"

// Безопасный доступ (не кидает исключение)
list.getOrNull(10)           // null
list.getOrElse(10) { "default" }  // "default"
list.firstOrNull()           // "a" или null если пустой
list.lastOrNull()            // "e" или null если пустой

// Срезы
list.take(3)         // ["a", "b", "c"]
list.takeLast(2)     // ["d", "e"]
list.drop(2)         // ["c", "d", "e"]
list.dropLast(2)     // ["a", "b", "c"]
list.slice(1..3)     // ["b", "c", "d"]
```

## Изменение (MutableList)

```kotlin
val list = mutableListOf("a", "b", "c")

// Добавление
list.add("d")            // в конец
list.add(0, "z")         // по индексу
list.addAll(listOf("x", "y"))
list += "new"            // оператор

// Удаление
list.remove("a")         // по значению (первое вхождение)
list.removeAt(0)         // по индексу
list.removeAll { it.length > 1 }  // по условию
list.clear()             // очистить всё

// Обновление
list[0] = "updated"
list.set(1, "new value")
```

## Проверки

```kotlin
val list = listOf(1, 2, 3, 4, 5)

list.isEmpty()           // false
list.isNotEmpty()        // true
list.size                // 5

list.contains(3)         // true
3 in list                // true (идиоматичный способ)
list.containsAll(listOf(1, 2))  // true

list.indexOf(3)          // 2
list.lastIndexOf(3)      // 2
list.indexOfFirst { it > 2 }   // 2
list.indexOfLast { it > 2 }    // 4
```

## Перебор

```kotlin
val list = listOf("a", "b", "c")

// Классический for
for (item in list) {
    println(item)
}

// С индексом
for ((index, item) in list.withIndex()) {
    println("$index: $item")
}

// forEach
list.forEach { println(it) }
list.forEachIndexed { index, item -> println("$index: $item") }

// ВАЖНО: break/continue НЕ работают в forEach!
// Используйте return@forEach для пропуска итерации:
list.forEach {
    if (it == "b") return@forEach  // как continue
    println(it)
}
```

---

# Set

## Главное правило
**Set хранит только уникальные элементы. Дубликаты автоматически отбрасываются.**

## Создание

```kotlin
val set = setOf(1, 2, 3, 2, 1)       // {1, 2, 3} - дубли убраны
val mutableSet = mutableSetOf<String>()
val empty = emptySet<Int>()

// Разные реализации
val hashSet = hashSetOf(1, 2, 3)     // нет гарантии порядка
val linkedSet = linkedSetOf(1, 2, 3) // сохраняет порядок вставки
val sortedSet = sortedSetOf(3, 1, 2) // {1, 2, 3} отсортирован
```

## Операции с множествами

```kotlin
val a = setOf(1, 2, 3)
val b = setOf(2, 3, 4)

// Объединение (union)
a union b            // {1, 2, 3, 4}
a + b                // то же самое

// Пересечение (intersect)
a intersect b        // {2, 3}

// Разность (subtract)
a subtract b         // {1} - элементы a, которых нет в b
a - b                // то же самое
```

## Практическое применение

```kotlin
// Убрать дубликаты из списка
val listWithDupes = listOf(1, 2, 2, 3, 3, 3)
val unique = listWithDupes.toSet().toList()  // [1, 2, 3]
// или короче:
val unique2 = listWithDupes.distinct()       // [1, 2, 3]

// Быстрая проверка наличия (O(1) вместо O(n))
val allowedIds = setOf(1, 2, 3, 4, 5)
if (userId in allowedIds) { /* ... */ }
```

---

# Map

## Создание

```kotlin
val map = mapOf(
    "name" to "Alex",
    "age" to 25,
    "city" to "Moscow"
)

val mutableMap = mutableMapOf<String, Int>()
val empty = emptyMap<String, String>()

// Разные реализации
val hashMap = hashMapOf("a" to 1)      // нет гарантии порядка
val linkedMap = linkedMapOf("a" to 1)  // сохраняет порядок вставки
val sortedMap = sortedMapOf("b" to 2, "a" to 1)  // сортировка по ключам
```

## Доступ к элементам

```kotlin
val map = mapOf("name" to "Alex", "age" to 25)

// Базовый доступ
map["name"]                  // "Alex" или null
map.get("name")              // то же самое

// Безопасный доступ
map.getOrDefault("city", "Unknown")  // "Unknown"
map.getOrElse("city") { "Computed" } // лямбда для значения
map.getValue("name")         // "Alex" или NoSuchElementException!
```

## Изменение (MutableMap)

```kotlin
val map = mutableMapOf("a" to 1, "b" to 2)

// Добавление/обновление
map["c"] = 3                 // добавить
map["a"] = 10                // обновить
map.put("d", 4)              // то же, возвращает старое значение
map.putIfAbsent("e", 5)      // только если ключа нет
map += ("f" to 6)            // оператор

// Удаление
map.remove("a")              // удалить по ключу
map.remove("b", 2)           // удалить только если значение совпадает
map.clear()                  // очистить всё

// Слияние
map.putAll(mapOf("x" to 10, "y" to 20))
map += mapOf("x" to 10, "y" to 20)
```

## Проверки

```kotlin
val map = mapOf("name" to "Alex", "age" to 25)

map.containsKey("name")      // true
"name" in map                // true (идиоматичный способ)
map.containsValue(25)        // true

map.isEmpty()                // false
map.size                     // 2
```

## Перебор

```kotlin
val map = mapOf("a" to 1, "b" to 2, "c" to 3)

// Деструктуризация (рекомендуется!)
for ((key, value) in map) {
    println("$key -> $value")
}

// Через entries
for (entry in map.entries) {
    println("${entry.key} -> ${entry.value}")
}

// Только ключи или значения
map.keys.forEach { println(it) }
map.values.forEach { println(it) }

// forEach с деструктуризацией
map.forEach { (k, v) -> println("$k: $v") }
```

---

# Трансформации

## filter — отбор по условию

```kotlin
val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

// Базовый filter
numbers.filter { it > 5 }              // [6, 7, 8, 9, 10]
numbers.filterNot { it > 5 }           // [1, 2, 3, 4, 5]

// С индексом
numbers.filterIndexed { index, _ -> index % 2 == 0 }  // [1, 3, 5, 7, 9]

// Для nullable
val mixed = listOf(1, null, 2, null, 3)
mixed.filterNotNull()                  // [1, 2, 3]

// Filter для Map
val map = mapOf("a" to 1, "b" to 2, "c" to 3)
map.filter { (_, value) -> value > 1 }           // {b=2, c=3}
map.filterKeys { it != "a" }                      // {b=2, c=3}
map.filterValues { it > 1 }                       // {b=2, c=3}
```

## map — преобразование элементов

```kotlin
val names = listOf("alice", "bob", "charlie")

// Базовый map
names.map { it.uppercase() }           // [ALICE, BOB, CHARLIE]
names.map { it.length }                // [5, 3, 7]

// С индексом
names.mapIndexed { i, name -> "$i. $name" }  // [0. alice, 1. bob, 2. charlie]

// mapNotNull — преобразовать + убрать null
val strings = listOf("1", "two", "3", "four")
strings.mapNotNull { it.toIntOrNull() }      // [1, 3]

// Map для Map (преобразование в список)
val scores = mapOf("Alice" to 90, "Bob" to 85)
scores.map { (name, score) -> "$name: $score" }  // [Alice: 90, Bob: 85]
```

## flatMap — развернуть вложенные структуры

```kotlin
// flatten — просто развернуть
val nested = listOf(listOf(1, 2), listOf(3, 4), listOf(5))
nested.flatten()                       // [1, 2, 3, 4, 5]

// flatMap — преобразовать + развернуть
val words = listOf("hello", "world")
words.flatMap { it.toList() }          // [h, e, l, l, o, w, o, r, l, d]

// Практический пример
data class Order(val items: List<String>)
val orders = listOf(
    Order(listOf("apple", "banana")),
    Order(listOf("orange"))
)
orders.flatMap { it.items }            // [apple, banana, orange]
```

## sorted — сортировка

```kotlin
val numbers = listOf(3, 1, 4, 1, 5, 9, 2, 6)

// Базовая сортировка
numbers.sorted()                       // [1, 1, 2, 3, 4, 5, 6, 9]
numbers.sortedDescending()             // [9, 6, 5, 4, 3, 2, 1, 1]

// Сортировка объектов
data class User(val name: String, val age: Int)
val users = listOf(User("Bob", 25), User("Alice", 30), User("Charlie", 20))

users.sortedBy { it.age }              // по возрасту ↑
users.sortedByDescending { it.age }    // по возрасту ↓
users.sortedBy { it.name }             // по имени ↑

// Кастомный компаратор
users.sortedWith(compareBy({ it.age }, { it.name }))  // сначала по возрасту, потом по имени

// reversed
numbers.reversed()                     // [6, 2, 9, 5, 1, 4, 1, 3]
```

## find / first / last — поиск элемента

```kotlin
val numbers = listOf(1, 2, 3, 4, 5, 6)

// find = firstOrNull
numbers.find { it > 3 }                // 4
numbers.firstOrNull { it > 3 }         // 4 (то же самое)
numbers.lastOrNull { it > 3 }          // 6

// first/last без условия
numbers.first()                        // 1
numbers.last()                         // 6
numbers.firstOrNull()                  // 1 (безопасно для пустого)

// ВНИМАНИЕ: first { } кидает NoSuchElementException если не найден!
numbers.first { it > 100 }             // Exception!
numbers.firstOrNull { it > 100 }       // null (безопасно)
```

## any / all / none — проверки

```kotlin
val numbers = listOf(1, 2, 3, 4, 5)

// any — хотя бы один соответствует
numbers.any { it > 3 }                 // true
numbers.any { it > 100 }               // false

// all — все соответствуют
numbers.all { it > 0 }                 // true
numbers.all { it > 3 }                 // false

// none — ни один не соответствует
numbers.none { it > 100 }              // true
numbers.none { it > 3 }                // false

// Без условия
numbers.any()                          // true (не пустой)
numbers.none()                         // false (есть элементы)
emptyList<Int>().none()                // true
```

## groupBy — группировка

```kotlin
val words = listOf("apple", "apricot", "banana", "blueberry", "cherry")

// Группировка по первой букве
words.groupBy { it.first() }
// {a=[apple, apricot], b=[banana, blueberry], c=[cherry]}

// Группировка с преобразованием значений
words.groupBy(
    keySelector = { it.first() },
    valueTransform = { it.uppercase() }
)
// {a=[APPLE, APRICOT], b=[BANANA, BLUEBERRY], c=[CHERRY]}

// Подсчёт частоты элементов
val letters = listOf("a", "b", "a", "c", "a", "b")
letters.groupingBy { it }.eachCount()  // {a=3, b=2, c=1}
```

## partition — разделение на два списка

```kotlin
val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

// Разделить на чётные и нечётные
val (even, odd) = numbers.partition { it % 2 == 0 }
// even = [2, 4, 6, 8, 10]
// odd = [1, 3, 5, 7, 9]

// Практический пример
data class User(val name: String, val isActive: Boolean)
val users = listOf(User("Alice", true), User("Bob", false))
val (active, inactive) = users.partition { it.isActive }
```

## take / drop — взять / пропустить

```kotlin
val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

// take — взять первые N
numbers.take(3)                        // [1, 2, 3]
numbers.takeLast(3)                    // [8, 9, 10]
numbers.takeWhile { it < 5 }           // [1, 2, 3, 4]
numbers.takeLastWhile { it > 7 }       // [8, 9, 10]

// drop — пропустить первые N
numbers.drop(3)                        // [4, 5, 6, 7, 8, 9, 10]
numbers.dropLast(3)                    // [1, 2, 3, 4, 5, 6, 7]
numbers.dropWhile { it < 5 }           // [5, 6, 7, 8, 9, 10]
```

## distinct — уникальные элементы

```kotlin
val numbers = listOf(1, 2, 2, 3, 3, 3, 4)

numbers.distinct()                     // [1, 2, 3, 4]

// distinctBy — уникальные по какому-то признаку
data class User(val id: Int, val name: String)
val users = listOf(User(1, "Alice"), User(1, "Alice2"), User(2, "Bob"))
users.distinctBy { it.id }             // [User(1, Alice), User(2, Bob)]
```

## reduce / fold — агрегация

```kotlin
val numbers = listOf(1, 2, 3, 4, 5)

// reduce — свёртка (начинает с первого элемента)
numbers.reduce { acc, n -> acc + n }   // 15 (сумма)
numbers.reduce { acc, n -> acc * n }   // 120 (произведение)

// fold — свёртка с начальным значением
numbers.fold(0) { acc, n -> acc + n }  // 15
numbers.fold(1) { acc, n -> acc * n }  // 120
numbers.fold("") { acc, n -> acc + n } // "12345" (строка)

// Практический пример
data class Item(val price: Int, val quantity: Int)
val cart = listOf(Item(100, 2), Item(50, 3))
cart.fold(0) { total, item -> total + item.price * item.quantity }  // 350
```

## associate — создание Map из List

```kotlin
data class User(val id: Int, val name: String)
val users = listOf(User(1, "Alice"), User(2, "Bob"))

// associateBy — ключ из элемента, значение = элемент
users.associateBy { it.id }
// {1=User(1, Alice), 2=User(2, Bob)}

// associateWith — ключ = элемент, значение из лямбды
users.associateWith { it.name.length }
// {User(1, Alice)=5, User(2, Bob)=3}

// associate — полный контроль
users.associate { it.id to it.name }
// {1=Alice, 2=Bob}
```

## zip — объединение двух списков

```kotlin
val names = listOf("Alice", "Bob", "Charlie")
val ages = listOf(25, 30, 35)

// zip — создать список пар
names.zip(ages)                        // [(Alice, 25), (Bob, 30), (Charlie, 35)]

// zip с трансформацией
names.zip(ages) { name, age -> "$name is $age" }
// [Alice is 25, Bob is 30, Charlie is 35]

// unzip — обратная операция
val pairs = listOf("a" to 1, "b" to 2)
val (letters, numbers) = pairs.unzip()
// letters = [a, b], numbers = [1, 2]
```

## chunked / windowed — разбиение на части

```kotlin
val numbers = listOf(1, 2, 3, 4, 5, 6, 7)

// chunked — разбить на куски
numbers.chunked(3)                     // [[1, 2, 3], [4, 5, 6], [7]]

// chunked с трансформацией
numbers.chunked(3) { it.sum() }        // [6, 15, 7]

// windowed — скользящее окно
numbers.windowed(3)                    // [[1,2,3], [2,3,4], [3,4,5], [4,5,6], [5,6,7]]
numbers.windowed(3, step = 2)          // [[1,2,3], [3,4,5], [5,6,7]]
```

## Цепочки операций

```kotlin
data class User(val name: String, val age: Int, val city: String)

val users = listOf(
    User("Alice", 28, "Moscow"),
    User("Bob", 17, "SPb"),
    User("Charlie", 35, "Moscow"),
    User("Diana", 22, "Moscow")
)

// Пример 1: Имена взрослых москвичей, отсортированные по алфавиту
users
    .filter { it.age >= 18 }
    .filter { it.city == "Moscow" }
    .sortedBy { it.name }
    .map { it.name }
// [Alice, Charlie, Diana]

// Пример 2: Количество пользователей по городам
users.groupingBy { it.city }.eachCount()
// {Moscow=3, SPb=1}

// Пример 3: Средний возраст по городам
users
    .groupBy { it.city }
    .mapValues { (_, users) -> users.map { it.age }.average() }
// {Moscow=28.33, SPb=17.0}
```

---

# Задачи с собеседований

## 1. Найти дубликаты в списке

```kotlin
fun findDuplicates(list: List<Int>): List<Int> {
    return list
        .groupingBy { it }
        .eachCount()
        .filter { it.value > 1 }
        .keys
        .toList()
}

// Пример: [1, 2, 2, 3, 3, 3] -> [2, 3]
```

## 2. Развернуть список без reverse()

```kotlin
fun reverseList(list: List<Int>): List<Int> {
    return list.foldRight(emptyList()) { item, acc -> acc + item }
}

// Или через индексы
fun reverseList2(list: List<Int>): List<Int> {
    return List(list.size) { list[list.size - 1 - it] }
}
```

## 3. Найти пересечение двух списков

```kotlin
fun intersection(a: List<Int>, b: List<Int>): List<Int> {
    return a.filter { it in b.toSet() }
}

// Или через Set
fun intersection2(a: List<Int>, b: List<Int>): List<Int> {
    return (a.toSet() intersect b.toSet()).toList()
}
```

## 4. Подсчитать частоту элементов

```kotlin
fun frequency(list: List<String>): Map<String, Int> {
    return list.groupingBy { it }.eachCount()
}

// Пример: ["a", "b", "a", "c", "a"] -> {a=3, b=1, c=1}
```

## 5. Найти второй по величине элемент

```kotlin
fun secondLargest(list: List<Int>): Int? {
    return list
        .distinct()
        .sortedDescending()
        .getOrNull(1)
}
```

## 6. Объединить списки без дубликатов

```kotlin
fun mergeUnique(a: List<Int>, b: List<Int>): List<Int> {
    return (a + b).distinct()
}

// Или через Set
fun mergeUnique2(a: List<Int>, b: List<Int>): List<Int> {
    return (a.toSet() + b.toSet()).toList()
}
```

## 7. Flatten вложенного списка

```kotlin
fun flattenDeep(list: List<Any>): List<Int> {
    return list.flatMap {
        when (it) {
            is List<*> -> flattenDeep(it as List<Any>)
            is Int -> listOf(it)
            else -> emptyList()
        }
    }
}
```

## 8. Сгруппировать анаграммы

```kotlin
fun groupAnagrams(words: List<String>): List<List<String>> {
    return words
        .groupBy { it.toCharArray().sorted().joinToString("") }
        .values
        .toList()
}

// Пример: ["eat", "tea", "tan", "ate", "nat", "bat"]
// -> [[eat, tea, ate], [tan, nat], [bat]]
```

---

# Sequence — ленивые коллекции

Для больших коллекций используйте `Sequence` — операции выполняются лениво.

```kotlin
val numbers = (1..1_000_000).asSequence()

// Операции выполняются только при терминальной операции
val result = numbers
    .filter { it % 2 == 0 }
    .map { it * 2 }
    .take(10)
    .toList()  // терминальная операция

// Когда использовать Sequence:
// - Большие коллекции (>10000 элементов)
// - Цепочка из 3+ операций
// - Нужны только первые N элементов (take)
```

---

# Полезные tips

## 1. Set для быстрого поиска
```kotlin
// Плохо O(n) на каждую проверку
val list = listOf(1, 2, 3, 4, 5)
if (x in list) { }  // O(n)

// Хорошо O(1)
val set = list.toSet()
if (x in set) { }   // O(1)
```

## 2. associate вместо forEach + put
```kotlin
// Плохо
val map = mutableMapOf<Int, String>()
users.forEach { map[it.id] = it.name }

// Хорошо
val map = users.associate { it.id to it.name }
```

## 3. mapNotNull вместо map + filterNotNull
```kotlin
// Плохо
list.map { it.toIntOrNull() }.filterNotNull()

// Хорошо
list.mapNotNull { it.toIntOrNull() }
```

## 4. firstOrNull вместо filter + first
```kotlin
// Плохо
list.filter { it > 5 }.firstOrNull()

// Хорошо
list.firstOrNull { it > 5 }
```

---

*Создано: 2026-02-01*
*Проект: KotlinRepetition*
