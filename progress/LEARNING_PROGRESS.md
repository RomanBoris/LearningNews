# Прогресс изучения Kotlin

## Информация о студенте
- **Опыт**: Android разработчик, повторение основ Kotlin
- **Формат**: Задания + проверка
- **Начало**: 2026-01-24

### Философия обучения
> "Я хочу стать хорошим разработчиком-инженером. Нейросеть не моя замена, а мой помощник."

**Подход**: Теория → базовые задания → проверка → коммит → практика в реальном проекте

### План обучения
1. **Этап 1**: База Kotlin (текущий проект) — синтаксис, коллекции, классы, лямбды
2. **Этап 2**: Android проект — применение изученного в реальных условиях
   - В Android проекте будет свой `LEARNING_PROGRESS.md`
   - Задания будут привязаны к реальным задачам Android разработки

### Git-практика (с сессии 2026-02-17)
> **С этого момента работаем через Git как положено:**
> - **Ветки**: для каждой темы/задачи создаём отдельную ветку, после завершения мержим в master
> - **Коммиты**: осмысленные коммиты после каждого завершённого задания/блока
> - **Формат**: студент выполняет все Git-команды сам, Claude подсказывает что делать, когда и как правильно
> - **Справка**: `GIT_GUIDE.md` — конспект по Git
>
> **Текущая ветка**: `topic-6-classes-finish` (создание — следующий шаг)
> ```
> git checkout -b topic-6-classes-finish
> ```

**Слабые места для усиления**:
- ~~Nullable типы и null safety~~ (базово изучено в теме 3)
- ~~When expressions (расширенные возможности)~~ (изучено в теме 3)
- ~~Коллекции~~ ✅ ИЗУЧЕНО! (List, Set, Map, трансформации)

---

## План тем

1. ✅ **Переменные и типы данных** (val, var, типы, nullable)
2. ✅ **Функции** (объявление, параметры, возвращаемые значения, single-expression)
3. ✅ **Условные операторы** (if, when, elvis operator)
4. ✅ **Циклы** (for, while, ranges)
5. ✅ **Коллекции** (List, Set, Map, трансформации) — ПРИОРИТЕТ ЗАКРЫТ!
6. ✅ **Классы и объекты** (конструкторы, свойства, методы, generics, data class)
7. ✅ **Data классы и sealed классы**
8. ✅ **Наследование и интерфейсы**
9. ✅ **Extension функции**
10. ✅ **Lambda и Higher-order функции**
11. ✅ **Scope функции** (let, apply, run, also, with)
12. ⏳ **Null safety** (?, !!, ?:, safe calls) — ОТЛОЖЕНА (2/12, сложно воспринимается)
13. ✅ **Корутины** (suspend, launch, async, Dispatchers, withContext, exception handling)
14. ⏸️ **JVM Concurrency** (Thread, synchronized, volatile, happens-before — собеседовательный уровень, изучаем параллельно с Android проектом)

---

## Пройденные темы

### ✅ Тема 1: Переменные и типы данных
**Файл**: `src/Main.kt`
**Дата**: 2026-01-24
**Статус**: Завершено

**Результаты**:
- Попытка 1: 7/10 - основы выполнены, детали упущены
- Попытка 2: 9/10 - добавлены все типы
- Финальная: 10/10 ✓

**Что изучено**:
- ✅ Неизменяемые переменные (`val`)
- ✅ Изменяемые переменные (`var`) с переприсвоением
- ✅ Nullable типы (`Double?`)
- ✅ Константы уровня файла (`const val`)
- ✅ Базовые типы: Int, Long, Float, Double, Boolean, Char, String
- ✅ Строковая интерполяция (`$variable`)

**Комментарии**: Быстро исправлял замечания, понимание материала хорошее.

---

### ✅ Тема 2: Функции
**Файл**: `src/Funk.kt`
**Дата**: 2026-01-24
**Статус**: Завершено

**Результаты**:
- Попытка 1: 8.5/10 - почти всё правильно
- Финальная: 10/10 ✓

**Что изучено**:
- ✅ Функция без параметров и возвращаемого значения (`fnTask0()`)
- ✅ Функция с параметрами и возвращаемым значением (`fnTask2()`)
- ✅ Функция с параметрами по умолчанию (`fnTask3()`)
- ✅ Single-expression функция (`fnTask4()`)
- ✅ Функция с vararg параметром (`fnTask5()`)
- ✅ Именованные аргументы при вызове

**Дополнительно изучено**:
- 📚 Подробный разбор `vararg` в Android:
  - Permissions (ActivityCompat.requestPermissions)
  - Room DAO (@Insert, @Delete)
  - Navigation (bundleOf)
  - String.format
  - Spread operator (*)

**Комментарии**: Самостоятельно создал отдельный файл. Запросил подробности про vararg - хороший подход к обучению.

---

### ✅ Тема 3: Условные операторы
**Файлы**: `src/Condition.kt`, `src/User.kt`
**Дата**: 2026-01-24
**Статус**: Завершено (основное задание)

**Результаты**:
- Попытка 1: 5/10 - базовые примеры
- Попытка 2: 8.5/10 - добавлены ключевые концепции ✓

**Что изучено** ✅:
- ✅ If-else как выражение (`conditionIf1()`)
- ✅ When с конкретными значениями (`condWhen()`)
- ✅ When с диапазонами (`condWhen2()`)
- ✅ **Elvis operator** (`validEmail()`) - добавлено во 2-й попытке
- ✅ **When как выражение** (`nullProg()`) - добавлено во 2-й попытке
- ✅ **When с проверкой типа (is)** (`nullProg()`) - добавлено во 2-й попытке
- ✅ Комбинация условий (&&, ||)
- ✅ Создал класс User для работы с nullable

**Опционально (не обязательно, но полезно для углубления)**:
- ⏸️ Safe call chain (цепочка `user?.address?.city`)
- ⏸️ Let с nullable (`email?.let { ... }`)
- ⏸️ When без аргумента (валидация нескольких условий)

**Теория предоставлена**:
- 📚 Подробный разбор Nullable типов (Safe call, Elvis, !!, Let, Smart cast, паттерны для Android)
- 📚 Подробный разбор When expression (как выражение, диапазоны, проверка типов, без аргумента, exhaustive when)

**Замечания**:
- Опечатка в User.kt: `adress` → должно быть `address`
- `conditionIf()` можно улучшить (убрать лишние return)

**Комментарии**: Хорошо усвоил материал после изучения теории. Elvis и When с типами реализованы правильно. Создал дополнительный класс User самостоятельно.

---

### ✅ Тема 4: Циклы
**Файлы**: `src/Cycles.kt`, `src/While.kt`
**Дата**: 2026-01-25
**Статус**: Завершено

**Результаты**: 10/10 ✓

**Что изучено**:
- ✅ `for` с range (`1..n`, `downTo`)
- ✅ `for` с коллекциями и `withIndex()`
- ✅ `while` + `break` (`guessNumber()`)
- ✅ `repeat` (`repeatNum()`, `repeatNum2()`)
- ✅ `continue` для пропуска итераций (`positiveNum()`)
- ✅ `return` для выхода из функции (`indexNum()`)
- ✅ Вложенные циклы (`multiplicationTable()`)

**Важные уроки**:
- Фигурные скобки в `if` — без них выполняется только одна строка
- Инициализация переменных: `list.first()` вместо `0` для поиска max/min
- `return` vs `break`: return выходит из функции, break — только из цикла

**Комментарии**: Все 12 заданий выполнены. Хорошее понимание циклов.

---

### ✅ Тема 5: Коллекции — ЗАВЕРШЕНО
**Файлы**: `src/Collection.kt`, `src/SetCollection.kt`, `src/MapCollection.kt`, `src/Transforms.kt`
**Дата**: 2026-01-25 — 2026-02-01
**Статус**: Завершено
**Результат**: 27/27 заданий ✓

**Цель достигнута**: Коллекции из слабой стороны превратились в сильную!

---

#### ✅ List (5/5 заданий)
- Создание: `listOf()`, `mutableListOf()`, `emptyList()`, `List(n) { }`
- Доступ: `[]`, `get()`, `getOrNull()`, `getOrElse()`, `first()`, `last()`
- Изменение: `add()`, `remove()`, `removeAt()`, `clear()`
- Проверки: `size`, `isEmpty()`, `contains()`, `in`, `indexOf()`

#### ✅ Set (5/5 заданий)
- Уникальные элементы, автоудаление дубликатов
- Операции множеств: `union`, `intersect`, `subtract`
- Преобразование: `list.toSet()`

#### ✅ Map (5/5 заданий)
- Создание: `mapOf()`, `mutableMapOf()`
- Доступ: `[]`, `get()`, `getOrDefault()`
- Изменение: `put()`, `[]`, `remove()`
- Перебор: `keys`, `values`, `entries`, деструктуризация `(k, v)`

#### ✅ Трансформации (12/12 заданий)

**Уровень 1 — Базовые**:
| # | Функция | Концепция | Статус |
|---|---------|-----------|--------|
| 1 | `filterAdults()` | filter | ✅ |
| 2 | `toUpperNames()` | map | ✅ |
| 3 | `findFirst()` | firstOrNull | ✅ |

**Уровень 2 — Комбинации**:
| # | Функция | Концепция | Статус |
|---|---------|-----------|--------|
| 4 | `adultNames()` | filter → sortedBy → map | ✅ |
| 5 | `sumOfEven()` | filter → sum | ✅ |
| 6 | `validEmails()` | filter (count) → map | ✅ |

**Уровень 3 — Подумать**:
| # | Функция | Концепция | Статус |
|---|---------|-----------|--------|
| 7 | `wordLengths()` | split → associate | ✅ |
| 8 | `groupByFirstLetter()` | groupBy | ✅ |
| 9 | `removeDuplicatesKeepOrder()` | toSet / distinct | ✅ |

**Уровень 4 — Собеседование**:
| # | Функция | Концепция | Статус |
|---|---------|-----------|--------|
| 10 | `mostFrequent()` | groupingBy → eachCount → maxBy | ✅ |
| 11 | `secondMax()` | distinct → sortedDescending → getOrNull | ✅ |
| 12 | `flatten()` | flatten → sortedDescending | ✅ |

**Важные уроки**:
- Оптимизация: сначала `filter`, потом `sorted` (меньше элементов сортировать)
- `groupBy` vs `groupingBy`: первый возвращает Map сразу, второй требует терминальную операцию
- `associate { it to value }` для создания Map из List

**Дополнительно создано**:
- 📚 `KOTLIN_COLLECTIONS_THEORY.md` — полная шпаргалка по коллекциям (~400 строк)

---

## Текущая тема

### ✅ Тема 6: Классы и объекты
**Файлы**: `src/objects/Classes.kt`, `src/objects/ClassesLeveTwo.kt`, `src/objects/ClassesLevelFour.kt`
**Дата**: 2026-02-05 — 2026-02-17
**Статус**: Завершено (все обязательные задания)

**Результаты**:

#### ✅ Уровень 1: Базовые классы (5/5) — ЗАВЕРШЁН!
| # | Задание | Концепция | Статус |
|---|---------|-----------|--------|
| 1.1 | Book | Primary конструктор, методы | ✅ 10/10 |
| 1.2 | Product | Параметры по умолчанию | ✅ 10/10 |
| 1.3 | Rectangle | Вычисляемые свойства (custom getter) | ✅ 10/10 |
| 1.4 | Temperature | Custom getter/setter, backing field | ✅ 10/10 |
| 1.5 | Person | Custom setter с валидацией | ✅ 10/10 |

#### ✅ Дополнительные задания на Getter/Setter (3/3)
| # | Задание | Концепция | Статус |
|---|---------|-----------|--------|
| G1 | Distance | Конвертация метры/км, getter/setter | ✅ 10/10 |
| G2 | UserName | Setter с trim() и валидацией | ✅ 10/10 |
| G3 | BankAccount (доп) | Методы deposit/withdraw | ✅ 10/10 |

#### ✅ Уровень 2: Конструкторы и свойства (5/5) — ЗАВЕРШЁН!
| # | Задание | Концепция | Статус |
|---|---------|-----------|--------|
| 2.1 | BankAccount | Init блок с валидацией | ✅ 10/10 |
| 2.2 | User | Secondary конструкторы | ✅ 10/10 |
| 2.3 | Point | Методы, вычисления | ✅ 10/10 |
| 2.4 | Circle | Private свойства, setter с проверкой | ✅ 10/10 |
| 2.5 | Password | Хеширование, приватные данные | ✅ 8/10 |

#### ✅ Уровень 3: Companion object и модификаторы (5/5) — ЗАВЕРШЁН!
| # | Задание | Концепция | Статус |
|---|---------|-----------|--------|
| 3.1 | MathUtils | companion object, константы | ✅ 9/10 |
| 3.2 | Counter | companion object, подсчёт экземпляров | ✅ 10/10 |
| 3.3 | Email | приватный конструктор, фабричный метод | ✅ (подсмотрел решение, разобрал) |
| 3.4 | Database | object (singleton) | ✅ 9/10 |
| 3.5 | Logger | уровни логирования | ✅ 10/10 (подсмотрел log(), разобрал) |

#### ✅ Уровень 4: Продвинутое (2/2) — ЗАВЕРШЁН!
| # | Задание | Концепция | Статус |
|---|---------|-----------|--------|
| 4.4 | Stack\<T\> | Generics, MutableList, push/pop/peek | ✅ 9/10 |
| 4.5 | Data class vs class | equals, toString, ==, === | ✅ 10/10 |

#### 📎 Ознакомительные (после всех основных тем)
- 4.1 Vector — operator overloading
- 4.2 Outer — nested/inner классы
- 4.3 Money — infix функции
> Редко используются в Android разработке. Будут представлены для ознакомления после прохождения тем 6–12.

**Важные уроки**:
- Разница между getter/setter и обычными методами
- `field` для backing field в setter
- `require()` для валидации в init блоке и setter
- Floating point precision (999.9999 вместо 1000.0)
- `trim()` возвращает новую строку, нужно сохранять результат
- `private constructor` + factory метод — паттерн для валидации при создании объекта
- `companion object` имеет доступ к приватному конструктору своего класса
- Явное указание типов помогает в обучении (например `split()` → `List<String>`)

**Теория**:
- 📚 `KOTLIN_CLASSES_THEORY.md` — полная шпаргалка по классам (~1000 строк)

---

## Заметки

### Вопрос студента (24.01.2026):
> "я все писал практически сходу, понимаю что задания простые, но нормально делаю?"

**Ответ**: Да, прогресс хороший. Для опытного Android разработчика базовые темы действительно простые. Рассматриваем варианты:
- Быстрый темп (несколько тем сразу)
- Практический подход (фокус на специфичные Kotlin фичи)
- Продолжить по плану

**Решение**: Продолжаем по плану - последовательное изучение всех тем с практикой

---

## Статистика
- Темы завершены: 11/12
- Всего заданий выполнено: 73+ (50 из предыдущих тем + 23 по классам)
- Средняя оценка: 10/10
- Файлов создано: 13 (Main.kt, Funk.kt, Condition.kt, User.kt, Cycles.kt, While.kt, Collection.kt, SetCollection.kt, MapCollection.kt, Transforms.kt, Classes.kt, ClassesLeveTwo.kt, ClassesLevelThree.kt)
- Дополнительные материалы:
  - KOTLIN_COLLECTIONS_THEORY.md (~400 строк)
  - KOTLIN_CLASSES_THEORY.md (~1000 строк)
- Формат: теория → задания → проверка

---

## Инструкции для следующей сессии

При следующем запуске Claude Code:
1. Прочитать этот файл (`LEARNING_PROGRESS.md`)
2. Понять текущий прогресс
3. Продолжить с темы, отмеченной как ⏳ (следующая)
4. Обновлять этот файл после каждой завершенной темы

**Текущая позиция**:
- ✅ Тема 6 (Классы и объекты) — ЗАВЕРШЕНА
- ⏳ **Тема 7: Data классы и sealed классы** — В ПРОЦЕССЕ (12/15)
- ✅ **Тема 7 завершена** (12/15 — 3 задания уровня 4 перенесены в Android проект)
- 🤖 **Перенести в Android проект**:
  - 4.1 StateManager (повтор) — ViewModel + StateFlow + MVI
  - 4.2 Парсер JSON-like — sealed для парсинга ответов API
  - 4.3 Pipeline — Flow операторы

**Полный список заданий**:
- См. файл `CLASSES_TASKS.md` (все 20 заданий зафиксированы)

---

## История сессий

### Сессия 2026-03-21 (Тема 13 завершена — Корутины)
- ✅ Тема 13 (Корутины) — все задания выполнены
  - task 1.1 - launch basics ✅
  - task 1.2 - suspend function ✅
  - task 2.1 - async parallel ✅
  - task 2.2 - job control ✅
  - task 3.1 - withContext ✅
  - task 3.2 - structured concurrency ✅
  - task 4.1 - try/catch ✅
  - task 4.2 - exception handler ✅ (`CoroutineExceptionHandler` + `CoroutineScope`)
- 💡 Null safety (тема 12) отложена — сложно воспринимается
- 🚀 **Следующий этап**: Android проект

### Сессия 2026-03-17 (Тема 12 начата + настройка Gradle + теория корутин)
- ⏳ Тема 12 (Null safety) — 2/12 заданий
  - task 1.1 safe call + elvis: formatName() с getUserName()
  - task 1.2 smart cast: describeValue() через when + is
- 🔧 Проект переведён на Gradle (добавлены `build.gradle.kts`, `settings.gradle.kts`)
  - Подключены корутины: `kotlinx-coroutines-core:1.7.3`
  - sourceSets настроен на существующую структуру `src/`
- 📚 Создан `GRADLE_THEORY.md` — теория по Gradle
- 📚 Создан `KOTLIN_COROUTINES_THEORY.md` — полная теория по корутинам
- 💡 Решение: JVM Concurrency (тема 14) изучаем параллельно с Android проектом
- **Следующее**: задания по null safety (осталось 10/12) + задания по корутинам

### Сессия 2026-03-09 (Тема 11 завершена)
- ✅ Тема 11 (Scope функции) — 10/10 заданий
  - task 1.1 apply: Person + Address через apply
  - task 1.2 let: null safety с ?.let + ?:
  - task 1.3 also: цепочка логирования
  - task 2.1 run: вычисления внутри блока
  - task 2.2 with: вывод Config
  - task 2.3 choosing scope: все 4 функции в одной
  - task 3.1 chain transformations: цепочка let → let → also
  - task 3.2 builder pattern: HttpRequest.() -> Unit
  - task 3.3 viewmodel: UiState + copy + also + run + let
  - task 3.4 run without object: initApp() + System.getenv()
- **Следующее**: Тема 12 — Null safety

### Сессия 2026-03-05 (Темы 9 и 10 завершены, тема 11 начата)
- 📚 Создан `KOTLIN_SCOPE_THEORY.md` — теория по scope функциям
- 📝 Создан `SCOPE_TASKS.md` — 10 заданий (3 уровня)
- 🗂️ Реорганизация: все .md файлы разложены по `docs/theory/` и `docs/tasks/`
- **Следующее**: начать выполнение заданий по теме 11 (Scope функции)

### Сессия 2026-03-05 (Темы 9 и 10 завершены)
- ✅ Тема 9 (Extensions) — 10/10 заданий
  - String, Int, Double, Nullable, List, User, MutableList extensions
  - Все уровни включая Android-style extensions
- ✅ Тема 10 (Lambda) — 10/10 заданий
  - Лямбды как переменные, higher-order функции, возврат функций
  - fold/reduce, мемоизация, compose, EventBus
- 🗂️ Реорганизация файлов: `docs/theory/` и `docs/tasks/`
- **Следующее**: Тема 11 — Scope функции

### Сессия 2026-03-01 (Тема 8 завершение + начало темы 9)
- ✅ 2.4 Discountable + Product — abstract + interface вместе (10/10)
- ✅ 3.1 Duck/Fish/Eagle — несколько интерфейсов, List\<Swimmable\> (10/10)
- ✅ 3.2 Sensor — is/as, смарт-каст, describeSensor() (10/10)
- ✅ 3.3 Logger — интерфейс как параметр, DI паттерн (разобрали подробно)
- ✅ 4.1 Repository — InMemoryUserRepository, CRUD (10/10)
- ✅ 4.2 EventBus — Observer паттерн, EventListener\<T\>, sealed AppEvent (10/10)
- ✅ 4.3 Strategy — SortStrategy\<T\>, Sorter\<T\>, 3 стратегии (написал сам после объяснения)
- **Тема 8 полностью завершена! 14/14 заданий ✅**
- 📚 Создан `KOTLIN_EXTENSION_THEORY.md` + `EXTENSION_TASKS.md` (10 заданий)
- ⏳ Тема 9 начата — 1.1 isPalindrome в процессе
- **Следующее**: продолжить с 1.1 String extensions

### Сессия 2026-02-28 (Тема 8 — Наследование и интерфейсы) 08:00 — ...
- 🔧 Переписан `GIT_GUIDE.md` — подробно с флагами, логикой, ASCII схемами
- 📝 Создан `INHERITANCE_TASKS.md` — 14 заданий (4 уровня)
- ✅ 1.1 Animal — open class, override, List\<Animal\> (10/10)
- ✅ 1.2 Shape — override val, open fun area() (10/10)
- ✅ 1.3 Vehicle — super.describe(), цепочка конструкторов (9/10)
- ✅ 1.4 Person/Employee/Manager — цепочка наследования 3 уровня (10/10)
- ✅ 2.1 Notification — abstract class, deliver(), preview() (10/10)
- ✅ 2.2 Printable — первый interface, List\<Printable\> (10/10)
- ✅ 2.3 Validatable — interface + default метод, регулярки (10/10)

### Сессия 2026-02-25 (Тема 7 — завершение + начало темы 8)
- ✅ 4.1 StateManager — 9/10 ⚠️ слабое место (MVI паттерн плохо понят)
- 📝 4.2, 4.3 — перенесены в Android проект с комментариями
- 📚 Создан `KOTLIN_INHERITANCE_THEORY.md` — теория по наследованию и интерфейсам
- 💡 Идея: список лёгких задач на повторение для вечера после работы (реализовать позже)
- 📝 **Следующее**: Тема 8 — задания по наследованию и интерфейсам (на выходных)

### Сессия 2026-02-21 (Тема 7 — уровни 2 и 3, ~5 часов)
- ✅ 2.3 OrderStatus — статусы заказа (8/10, исправил trackingNumber)
- ✅ 2.4 MathResult — divide + squareRoot + showResult (9/10, ArithmeticException)
- ✅ 3.1 Response\<T\> — generic sealed class (10/10)
- ✅ 3.2 Inventory — data class + коллекции: filter, groupBy, maxByOrNull, average (10/10)
- ✅ 3.3 Messenger — sealed + data class вместе (10/10)
- ✅ 3.4 Calculator — execute + describe, самостоятельно без подсказок (9/10)
- Git: коммиты после каждого задания, ветка `topic-7-sealed-classes`
- 📝 **Следующее**: 4.1 StateManager (уровень 4)

### Сессия 2026-02-18 (Тема 7 — продолжение)
- ✅ 1.4 Coordinate — свойство вне конструктора (10/10), объяснил почему не в equals/toString
- ✅ 2.1 Weather — первый sealed class (9/10)
- ✅ 2.2 Shape — площадь, сам перешёл на data class для вариантов (10/10)
- Git: ветка `topic-7-sealed-classes`, коммиты после каждого задания

### Сессия 2026-02-17 (Завершение темы 6 + начало темы 7)
- 🔧 Переход на полноценный Git-workflow: ветки, коммиты, мержи
- ✅ **Тема 6 завершена!** Stack\<T\> (9/10) + Data class vs class (10/10)
- Git: ветка `topic-6-classes-finish` → мерж в master → push
- 📚 Создан `KOTLIN_DATA_SEALED_THEORY.md` — конспект по data/sealed классам
- 📚 Создан `DATA_SEALED_TASKS.md` — 15 заданий (4 уровня)
- ⏳ **Тема 7 начата**: задания 1.1-1.3 выполнены (Contact, Student, Settings)
- Git: ветка `topic-7-sealed-classes`, push на GitHub

### Сессия 2026-02-13 (Классы — завершение уровня 3 + Git)
- ✅ Уровень 3: завершён (2 задания: Database, Logger)
  - Database — object singleton (9/10, замечание: `connection != null` вместо `if/else`)
  - Logger — уровни логирования (10/10, подсмотрел логику log())
- 🔧 Настроен .gitignore (убраны .idea/, .iml, добавлены .claude/, Windows-файлы)
- 📚 Создан GIT_GUIDE.md — практический конспект по Git
- 🚀 Первый коммит + подключение GitHub (private repo)
- **Решение**: студент делает коммиты самостоятельно для практики Git
- **Следующее**: Задание 4.4 (Stack<T> — generics)

### Сессия 2026-02-12 (Классы — продолжение)
- ✅ Уровень 2: завершён (4 задания: User, Point, Circle, Password)
- ✅ Уровень 3: начат (3/5)
  - MathUtils — companion object, константы, isPrime
  - Counter — companion object + init, подсчёт экземпляров
  - Email — private constructor + factory метод (подсмотрел, разобрал код)
- 📚 Разобрана теория: private constructor + factory pattern
- **Следующее**: Задание 3.4 (Database — object singleton)

### Сессия 2026-02-05 (Классы и объекты)
- ✅ Уровень 1: Базовые классы (5/5 заданий)
  - Book, Product, Rectangle, Temperature, Person
- ✅ Дополнительные задания на getter/setter (3/3)
  - Distance, UserName, BankAccount
  - Подробный разбор getter/setter с примерами
  - Исследование floating point precision
- ✅ Уровень 2: начат (1/5)
  - BankAccount с init блоком
- 📚 Изучена теория: `KOTLIN_CLASSES_THEORY.md` (~1000 строк)
- **Следующее**: Задание 2.2 (User с secondary конструкторами)

### Сессия 2026-02-01 (9:15 — 13:45, ~4.5 часа)
- ✅ Map: теория + 5 заданий
- ✅ Трансформации: 12 заданий (4 уровня сложности)
- 📚 Создана шпаргалка: `KOTLIN_COLLECTIONS_THEORY.md`
- **Тема 5 (Коллекции) полностью завершена!**
- Следующее: Тема 6 — Классы и объекты

### Сессия 2026-01-28 (2.5 часа)
- ✅ List: теория + 5 заданий
- ✅ Set: теория + 5 заданий
- 📚 Дополнительно: forEach vs for, forEachIndexed
- Следующее: Map
