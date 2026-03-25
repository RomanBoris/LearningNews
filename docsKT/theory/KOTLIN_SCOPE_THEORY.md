# Теория: Scope функции (let, apply, run, also, with)

## Что это такое

Scope функции — это функции стандартной библиотеки, которые выполняют блок кода **в контексте объекта**. Внутри блока можно обращаться к объекту без его имени.

```kotlin
// без scope функции
val user = User()
user.name = "Alex"
user.age = 25
println(user.name)

// с apply
val user = User().apply {
    name = "Alex"
    age = 25
}
```

---

## Главная таблица

| Функция | Объект внутри | Возвращает | Когда использовать |
|---------|--------------|------------|-------------------|
| `let`   | `it`         | результат лямбды | null-check, трансформация |
| `apply` | `this`       | сам объект | инициализация объекта |
| `run`   | `this`       | результат лямбды | инициализация + вычисление |
| `also`  | `it`         | сам объект | побочные действия (логирование) |
| `with`  | `this`       | результат лямбды | группировка операций над объектом |

---

## let

**Объект**: `it` | **Возвращает**: результат лямбды

```kotlin
val name: String? = "Alex"

// основное применение — null check
val length = name?.let {
    println("Name is $it")
    it.length  // возвращается из let
}
// length = 6, если name != null; null если name == null
```

```kotlin
// трансформация
val result = "  hello  "
    .trim()
    .let { it.uppercase() }  // "HELLO"
```

```kotlin
// цепочка с переименованием (чтобы не путаться с вложенными it)
user?.let { u ->
    u.email?.let { email ->
        sendEmail(email)
    }
}
```

**Правило**: используй `let` когда хочешь **что-то сделать с объектом и получить другой результат**, особенно с nullable.

---

## apply

**Объект**: `this` (неявный) | **Возвращает**: сам объект

```kotlin
val user = User().apply {
    name = "Alex"      // this.name = "Alex"
    age = 25           // this.age = 25
    email = "a@b.com"
}
```

```kotlin
// Android — типичный пример
val intent = Intent(context, MainActivity::class.java).apply {
    putExtra("userId", 123)
    putExtra("screen", "profile")
    flags = Intent.FLAG_ACTIVITY_NEW_TASK
}
startActivity(intent)
```

```kotlin
// Builder-паттерн
val dialog = AlertDialog.Builder(context).apply {
    setTitle("Внимание")
    setMessage("Удалить?")
    setPositiveButton("Да") { _, _ -> delete() }
    setNegativeButton("Нет", null)
}.create()
```

**Правило**: используй `apply` когда **настраиваешь объект** (устанавливаешь свойства) и хочешь вернуть его же.

---

## run

**Объект**: `this` (неявный) | **Возвращает**: результат лямбды

```kotlin
val result = user.run {
    // this = user
    "$name is ${if (age >= 18) "adult" else "minor"}"
}
// result = "Alex is adult"
```

```kotlin
// run без объекта — просто блок кода
val config = run {
    val host = System.getenv("HOST") ?: "localhost"
    val port = System.getenv("PORT")?.toInt() ?: 8080
    Config(host, port)
}
```

```kotlin
// null check + вычисление (альтернатива let)
val displayName = user?.run {
    "$name ($email)"
} ?: "Guest"
```

**Правило**: используй `run` когда хочешь **обратиться к объекту как `this`** и **вернуть вычисленное значение** (не сам объект).

---

## also

**Объект**: `it` | **Возвращает**: сам объект

```kotlin
val user = User("Alex", 25)
    .also { println("Created user: ${it.name}") }
    .also { logger.log("New user: $it") }
// also не меняет объект, просто даёт на него посмотреть
```

```kotlin
// в цепочке — добавить логирование не ломая цепочку
val numbers = mutableListOf(1, 2, 3)
    .also { println("Before: $it") }
    .apply { add(4) }
    .also { println("After: $it") }
```

```kotlin
// сохранение и использование
fun getUser(): User {
    return repository.findUser()
        .also { cache.put(it.id, it) }
}
```

**Правило**: используй `also` для **побочных действий** (логирование, отладка, кэширование) не меняя поток данных.

---

## with

**Объект**: `this` (неявный) | **Возвращает**: результат лямбды

```kotlin
val user = User("Alex", 25, "alex@test.com")

with(user) {
    println(name)   // this.name
    println(age)
    println(email)
}
```

```kotlin
// вычисление на основе объекта
val description = with(user) {
    buildString {
        append("Name: $name\n")
        append("Age: $age\n")
        append("Email: $email")
    }
}
```

**Отличие от run**: `with(obj) { }` vs `obj.run { }` — результат одинаковый, но `with` не работает с nullable объектами через `?.`.

```kotlin
user?.run { ... }   // OK для nullable
with(user) { ... }  // не работает если user nullable
```

**Правило**: используй `with` когда объект **точно не null** и нужно **несколько раз обратиться к нему** без повторения имени.

---

## Сравнение let vs run, apply vs also

```
             | возвращает себя | возвращает результат
-------------|-----------------|---------------------
this (неявный)| apply           | run
it           | also            | let
```

```
Нужно null-check?          → let
Настраиваю объект?         → apply
Вычисляю на основе объекта? → run
Логирую/отлаживаю?         → also
Несколько операций над объектом (non-null)? → with
```

---

## Цепочки scope функций

```kotlin
val user = User()
    .apply { name = "Alex"; age = 25 }     // настройка
    .also { logger.log("Created: $it") }   // логирование
    .let { userRepository.save(it) }       // трансформация (возвращает SavedUser)
```

---

## Типичные паттерны в Android

```kotlin
// 1. Безопасная работа с nullable
user?.let {
    binding.tvName.text = it.name
    binding.tvEmail.text = it.email
}

// 2. Инициализация View
binding.btnSubmit.apply {
    text = "Submit"
    isEnabled = false
    setOnClickListener { submit() }
}

// 3. Создание Intent
val intent = Intent(this, DetailActivity::class.java).apply {
    putExtra("id", item.id)
}
startActivity(intent)

// 4. Логирование в цепочке
viewModel.getUsers()
    .also { Log.d("TAG", "Fetched ${it.size} users") }
    .filter { it.isActive }

// 5. Инициализация объекта с вычислением результата
val isValid = form.run {
    nameField.isNotEmpty() && emailField.contains("@")
}
```

---

## Частые ошибки

### 1. apply когда нужен let
```kotlin
// ❌ apply возвращает объект, а не строку
val name: String = user.apply { trim() }  // ошибка типа

// ✅ let возвращает результат
val name: String = user.let { it.trim() }
```

### 2. Вложенные it
```kotlin
// ❌ непонятно какой it
user?.let {
    it.email?.let {
        println(it)  // это email или user?
    }
}

// ✅ явные имена
user?.let { u ->
    u.email?.let { email ->
        println(email)
    }
}
```

### 3. with с nullable
```kotlin
val user: User? = getUser()

// ❌ не компилируется если user nullable
with(user) { println(name) }

// ✅
user?.run { println(name) }
```
