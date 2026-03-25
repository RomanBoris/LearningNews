# Задания: Extension функции (10 заданий)

**Проект**: KotlinRepetition
**Файлы**: `src/extensions/`
**Теория**: `KOTLIN_EXTENSION_THEORY.md`

---

## 🟢 УРОВЕНЬ 1: Основы (4 задания)

### **1.1 String extensions**
Напиши extension функции для `String`:
- `fun String.isPalindrome(): Boolean` — true если строка читается одинаково в обе стороны (`"racecar"` → true)
- `fun String.wordCount(): Int` — количество слов в строке
- `fun String.capitalize2(): String` — первая буква заглавная, остальные строчные (не использовать встроенный `capitalize()`)

В main протестируй каждую на нескольких строках.

---

### **1.2 Int и Double extensions**
Напиши:
- `fun Int.isEven(): Boolean` — чётное ли число
- `fun Int.isPrime(): Boolean` — простое ли число
- `fun Double.roundTo(decimals: Int): Double` — округление до N знаков после запятой

В main протестируй.

---

### **1.3 List extensions**
Напиши extension функции для `List<Int>`:
- `fun List<Int>.second(): Int?` — второй элемент или null
- `fun List<Int>.sumOfEven(): Int` — сумма чётных элементов
- `fun List<Int>.isAscending(): Boolean` — отсортирован ли список по возрастанию

В main протестируй.

---

### **1.4 Nullable extensions**
Напиши:
- `fun String?.isNullOrBlank2(): Boolean` — true если null или пустая/пробелы (без встроенного isNullOrBlank)
- `fun Int?.orZero(): Int` — возвращает значение или 0 если null
- `fun <T> List<T>?.orEmpty2(): List<T>` — возвращает список или пустой список если null

В main протестируй с null и не-null значениями.

---

## 🟡 УРОВЕНЬ 2: Extension на своих классах (3 задания)

### **2.1 User extensions**
Создай `data class User(val name: String, val age: Int, val email: String)`.

Напиши extension функции:
- `fun User.isAdult(): Boolean` — возраст >= 18
- `fun User.hasValidEmail(): Boolean` — email содержит @ и .
- `fun User.greeting(): String` — `"Привет, $name! Тебе $age лет."`

В main создай несколько пользователей и протестируй.

---

### **2.2 List<User> extensions**
Используя `User` из 2.1, напиши extension функции для `List<User>`:
- `fun List<User>.adults(): List<User>` — только совершеннолетние
- `fun List<User>.averageAge(): Double` — средний возраст
- `fun List<User>.findByName(name: String): User?` — найти пользователя по имени

---

### **2.3 MutableList extensions**
Напиши extension функции для `MutableList<T>`:
- `fun <T> MutableList<T>.swap(i: Int, j: Int)` — поменять элементы местами
- `fun <T> MutableList<T>.removeFirst2(): T?` — удалить и вернуть первый элемент (null если пустой)

---

## 🔴 УРОВЕНЬ 3: Комбинации (3 задания)

### **3.1 String builder extensions**
Напиши extension функции для `String`:
- `fun String.toTitleCase(): String` — каждое слово с заглавной буквы (`"hello world"` → `"Hello World"`)
- `fun String.truncate(maxLength: Int): String` — обрезать до maxLength символов, добавить `"..."` если обрезано
- `fun String.countOccurrences(char: Char): Int` — количество вхождений символа

---

### **3.2 Collection pipeline**
Напиши extension функции:
- `fun List<String>.longestWord(): String?` — самое длинное слово
- `fun List<Int>.median(): Double` — медиана списка
- `fun <T> List<T>.chunked2(size: Int): List<List<T>>` — разбить список на подсписки по size элементов (без встроенного chunked)

---

### **3.3 Android-style extensions**
Имитируй типичные Android extension функции:
- `fun String.toIntSafe(): Int?` — конвертация String → Int без исключения (null если не число)
- `fun String.toDoubleSafe(): Double?` — аналогично для Double
- `fun List<*>.isNotEmpty2(): Boolean` — не пустой ли список
- `fun <T> T?.isNull(): Boolean` — является ли объект null

В main покажи как это используется вместо try-catch.

---

## Прогресс

| Уровень | Задания | Статус |
|---------|---------|--------|
| 1. Основы | 0/4 | ⏸️ |
| 2. Свои классы | 0/3 | ⏸️ |
| 3. Комбинации | 0/3 | ⏸️ |
| **Итого** | **0/10** | |
