# Extension функции в Kotlin

---

## 1. Что такое extension функция

Позволяет добавить метод к **существующему классу** без его изменения и без наследования.

```kotlin
// Добавляем метод isEmail() к классу String
fun String.isEmail(): Boolean {
    return contains('@') && contains('.')
}

val email = "user@mail.ru"
println(email.isEmail())  // true
```

Синтаксис: `fun ТипПолучателя.имяФункции(): ТипВозврата`

---

## 2. Extension на стандартных типах

```kotlin
fun Int.isEven(): Boolean = this % 2 == 0

fun String.firstWord(): String = trim().split(" ").first()

fun List<Int>.average(): Double = sum().toDouble() / size

println(4.isEven())             // true
println("  Hello World".firstWord())  // Hello
println(listOf(1, 2, 3).average())    // 2.0
```

`this` внутри extension функции — это объект на котором вызывается функция.

---

## 3. Extension свойства

```kotlin
val String.wordCount: Int
    get() = trim().split(" ").size

val String.isPalindrome: Boolean
    get() = this == this.reversed()

println("hello world".wordCount)  // 2
println("racecar".isPalindrome)   // true
```

У extension свойств **нет backing field** — только getter (и setter для var).

---

## 4. Nullable receiver

```kotlin
fun String?.orEmpty(): String = this ?: ""

fun String?.isNullOrBlankCustom(): Boolean = this == null || this.isBlank()

val name: String? = null
println(name.orEmpty())            // ""
println(name.isNullOrBlankCustom()) // true
```

Если receiver nullable — функцию можно вызывать на null без NPE.

---

## 5. Extension функция vs метод класса

```kotlin
class User(val name: String, val age: Int)

// Extension — снаружи класса
fun User.isAdult(): Boolean = age >= 18

// Метод класса — внутри
class User(val name: String, val age: Int) {
    fun isAdult(): Boolean = age >= 18
}
```

**Когда extension, а когда метод:**
- Метод — если это **ключевая логика** класса
- Extension — если это **дополнительная/утилитарная** логика, или класс чужой (String, Int, List)

---

## 6. Extension на generic типах

```kotlin
fun <T> List<T>.secondOrNull(): T? = if (size >= 2) this[1] else null

fun <T> MutableList<T>.swap(i: Int, j: Int) {
    val temp = this[i]
    this[i] = this[j]
    this[j] = temp
}

println(listOf(1, 2, 3).secondOrNull())  // 2
println(listOf<Int>().secondOrNull())    // null
```

---

## 7. Android контекст

Extension функции активно используются в Android:

```kotlin
// Скрыть/показать View
fun View.show() { visibility = View.VISIBLE }
fun View.hide() { visibility = View.GONE }

// Toast одной строкой
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

// Работа с SharedPreferences
fun SharedPreferences.Editor.putBoolean(key: String, value: Boolean): SharedPreferences.Editor {
    putBoolean(key, value)
    return this
}

// Использование:
myView.hide()
context.toast("Привет!")
```

---

## Шпаргалка

```
fun Тип.функция()     — extension функция
val Тип.свойство      — extension свойство (только getter)
var Тип.свойство      — extension свойство (getter + setter)
fun Тип?.функция()    — extension на nullable типе
this                  — объект на котором вызвана функция
```
