# Задания: Clean Architecture

> Все задания выполняются в проекте LearningNews.
> Теорию читать в `docs/theory/01_clean_architecture.md`.
> **Примеры из теории не копировать** — там другой домен. Здесь работаем с новостями.

---

## Уровень 1 — Создать структуру и Domain слой

### Задание 1.1 — Структура папок
Создай структуру пакетов в `com.pobezhkin.learningnews`:
```
data/
  remote/dto/
  repository/
  mapper/
domain/
  model/
  repository/
  usecase/
presentation/
  screen/home/
```
Создай пустые `.gitkeep` файлы или сразу начинай с реальных классов.

**Цель**: понять где что будет жить до того как начнёшь писать код.

---

### Задание 1.2 — Domain модель
В `domain/model/` создай data class `Article` с полями:
- `id: String`
- `title: String`
- `description: String?` (может быть null)
- `url: String`
- `imageUrl: String?`
- `publishedAt: String`
- `sourceName: String`

**Правило**: никаких аннотаций Room, никаких `@SerializedName`. Чистый Kotlin.

---

### Задание 1.3 — Repository интерфейс
В `domain/repository/` создай интерфейс `NewsRepository` с одним методом:
- получить список топовых новостей (suspend функция, возвращает `List<Article>`)

**Вопрос для себя**: почему это интерфейс, а не класс?

---

### Задание 1.4 — UseCase
В `domain/usecase/` создай класс `GetTopHeadlinesUseCase`.
- Принимает `NewsRepository` в конструкторе
- Имеет `invoke` оператор (suspend)
- Делегирует вызов в репозиторий

---

## Уровень 2 — Data слой (заглушка)

### Задание 2.1 — DTO
В `data/remote/dto/` создай `ArticleDto` — модель как она придёт из сети:
- `title: String?`
- `description: String?`
- `url: String?`
- `urlToImage: String?`
- `publishedAt: String?`
- `source: SourceDto?`

И `SourceDto`:
- `id: String?`
- `name: String?`

> Все поля nullable — сеть ненадёжна, любое поле может не прийти.

---

### Задание 2.2 — Mapper
В `data/mapper/` создай функцию-расширение `ArticleDto.toDomain(): Article`.

Обрати внимание:
- `title` в DTO nullable, в Domain — нет. Как обработаешь?
- `id` в DTO нет совсем. Придумай как сгенерировать (подсказка: `url` уникален)
- `sourceName` — откуда возьмёшь?

---

### Задание 2.3 — RepositoryImpl (заглушка)
В `data/repository/` создай `NewsRepositoryImpl` которая реализует `NewsRepository`.

Пока без настоящей сети — возвращай список из 2–3 захардкоженных `Article` объектов.

**Цель**: убедиться что цепочка Domain → Data работает без сети.

---

## Уровень 3 — Связать всё

### Задание 3.1 — ViewModel
В `presentation/screen/home/` создай `HomeViewModel`:
- Принимает `GetTopHeadlinesUseCase` в конструкторе
- Содержит `StateFlow<HomeUiState>`
- При создании (или по вызову метода) — загружает новости

`HomeUiState` — sealed class с состояниями: Loading, Success (список статей), Error (сообщение).

---

### Задание 3.2 — Проверка
Запусти проект. Добавь в `MainActivity` временный вывод в лог:
```
Log.d("TEST", "Articles count: ...")
```
Создай вручную `NewsRepositoryImpl` → `GetTopHeadlinesUseCase` → вызови и выведи количество статей.

**Цель**: убедиться что вся цепочка работает до подключения Hilt.

---

## Итоговый чеклист

- [ ] Структура пакетов создана
- [ ] `Article` — чистый domain объект без Android аннотаций
- [ ] `NewsRepository` — интерфейс в domain
- [ ] `GetTopHeadlinesUseCase` — делегирует в репозиторий через `invoke`
- [ ] `ArticleDto` — все поля nullable
- [ ] Mapper корректно обрабатывает null поля
- [ ] `NewsRepositoryImpl` реализует интерфейс
- [ ] `HomeUiState` — sealed class с 3 состояниями
- [ ] `HomeViewModel` — работает через UseCase
- [ ] В логах видно что цепочка работает
