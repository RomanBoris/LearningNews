# Теория: Lambda и Higher-order функции

## 1. Что такое лямбда

Лямбда — это анонимная функция, которую можно сохранить в переменную или передать как аргумент.

```kotlin
val square: (Int) -> Int = { x -> x * x }
println(square(5)) // 25
```

Синтаксис: `{ параметры -> тело }`

---

## 2. Типы функций

```kotlin
() -> Unit          // нет параметров, нет возвращаемого значения
(Int) -> Int        // один параметр Int, возвращает Int
(Int, String) -> Boolean  // два параметра, возвращает Boolean
```

Примеры:
```kotlin
val greet: () -> Unit = { println("Hello") }
val add: (Int, Int) -> Int = { a, b -> a + b }
val isLong: (String) -> Boolean = { it.length > 5 }
```

---

## 3. `it` — неявный параметр

Когда у лямбды **один параметр**, его можно не объявлять — используй `it`:

```kotlin
val double: (Int) -> Int = { it * 2 }
val isEven: (Int) -> Boolean = { it % 2 == 0 }
```

---

## 4. Higher-order функции

Функция, которая **принимает функцию как параметр** или **возвращает функцию**.

```kotlin
fun applyTwice(n: Int, f: (Int) -> Int): Int {
    return f(f(n))
}

println(applyTwice(3, { it * 2 })) // 12
```

### Trailing lambda синтаксис

Если лямбда — **последний параметр**, можно вынести её за скобки:

```kotlin
applyTwice(3) { it * 2 }  // то же самое
```

Если лямбда — **единственный параметр**, скобки вообще убираются:

```kotlin
fun repeat3(action: () -> Unit) {
    action(); action(); action()
}

repeat3 { println("!") }
```

---

## 5. Возврат функции из функции

```kotlin
fun multiplier(factor: Int): (Int) -> Int {
    return { it * factor }
}

val triple = multiplier(3)
println(triple(5)) // 15
```

---

## 6. Функции стандартной библиотеки (используют лямбды)

Ты уже их знаешь из коллекций:

```kotlin
listOf(1, 2, 3, 4).filter { it > 2 }       // [3, 4]
listOf(1, 2, 3).map { it * 10 }             // [10, 20, 30]
listOf(1, 2, 3).forEach { println(it) }
listOf(1, 2, 3).any { it > 2 }             // true
listOf(1, 2, 3).all { it > 0 }             // true
listOf(1, 2, 3).none { it > 5 }            // true
listOf(1, 2, 3).count { it % 2 == 1 }      // 2
listOf(1, 2, 3, 4).fold(0) { acc, x -> acc + x }  // 10
```

---

## 7. `fold` и `reduce`

```kotlin
// fold — с начальным значением
listOf(1, 2, 3, 4).fold(0) { acc, x -> acc + x }   // 10
listOf(1, 2, 3, 4).fold(1) { acc, x -> acc * x }   // 24

// reduce — без начального значения (первый элемент = начало)
listOf(1, 2, 3, 4).reduce { acc, x -> acc + x }    // 10
```

`fold` безопаснее — работает с пустыми списками, `reduce` бросает исключение.

---

## 8. Функция как переменная

```kotlin
fun isEven(n: Int): Boolean = n % 2 == 0

val check: (Int) -> Boolean = ::isEven  // ссылка на функцию

listOf(1, 2, 3, 4).filter(::isEven)  // [2, 4]
```

`::functionName` — ссылка на уже объявленную функцию.

---

## 9. Nullable лямбды

```kotlin
fun maybeRun(action: (() -> Unit)?) {
    action?.invoke()
}

maybeRun(null)        // ничего не произойдёт
maybeRun { println("ran") }
```

`invoke()` — явный вызов лямбды (то же что `action()`).

---

## 10. Типичные паттерны в Android

```kotlin
// Callback
button.setOnClickListener { view ->
    // обработка нажатия
}

// Трансформация данных
val names = users.map { it.name }
val adults = users.filter { it.age >= 18 }

// Сортировка
users.sortedBy { it.name }
users.sortedWith(compareBy({ it.age }, { it.name }))

// Поиск
val found = users.firstOrNull { it.id == targetId }

// Действие с nullable
user?.let { showProfile(it) }
```

---

## Шпаргалка

| Синтаксис | Что значит |
|-----------|------------|
| `{ x -> x * 2 }` | лямбда с параметром x |
| `{ it * 2 }` | лямбда с неявным параметром |
| `(Int) -> Int` | тип функции |
| `::functionName` | ссылка на функцию |
| `action?.invoke()` | вызов nullable лямбды |
| trailing lambda | лямбда вынесена за скобки |
