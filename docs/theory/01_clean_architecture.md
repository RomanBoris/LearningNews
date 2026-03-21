# Теория: Clean Architecture

## Рекомендуется к прочтению
- **"Clean Architecture"** — Robert C. Martin, главы 1–5, 22
- developer.android.com/topic/architecture

---

## Зачем вообще нужна архитектура?

Представь проект без архитектуры: всё в одном Activity/Fragment — сеть, логика, UI, база данных. Что происходит через 3 месяца?
- Изменение одной кнопки ломает логику расчётов
- Невозможно написать тест
- Новый разработчик открывает файл и закрывает ноутбук

**Архитектура** — это правила о том, *что знает о чём*. Не о том как красиво назвать папки.

---

## Главный принцип: Dependency Rule

```
Presentation → Domain ← Data
```

Стрелка = "знает о" / "зависит от".

- **Presentation** знает о Domain
- **Data** знает о Domain
- **Domain** не знает ни о ком

Domain — это сердце. Оно не должно знать, откуда данные (сеть? база? заглушка?) и как они отображаются (Compose? XML? тест?).

Это называется **инверсия зависимостей** (Dependency Inversion Principle — буква D из SOLID).

---

## Три слоя

### 1. Domain (бизнес-логика)
Самый важный слой. Чистый Kotlin — никаких Android зависимостей.

**Что здесь живёт:**
- **Entity** — бизнес-объект (что такое "статья" с точки зрения бизнеса)
- **Repository interface** — *контракт* (что мы умеем делать с данными)
- **UseCase** — одна бизнес-операция

```kotlin
// Entity — только бизнес-поля, никаких @Entity Room, никаких @SerializedName
data class Article(
    val id: String,
    val title: String,
    val description: String?,
    val publishedAt: String
)

// Repository interface — контракт, не реализация
interface ArticleRepository {
    suspend fun getTopArticles(): List<Article>
}

// UseCase — одна операция, одна ответственность
class GetTopArticlesUseCase(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(): List<Article> {
        return repository.getTopArticles()
    }
}
```

> **Заметь**: `ArticleRepository` — это интерфейс. Domain не знает, откуда придут данные.

---

### 2. Data (данные)
Здесь живут реализации: сеть, база, кэш.

**Что здесь живёт:**
- **RepositoryImpl** — реализует интерфейс из Domain
- **RemoteDataSource** — работа с API (Retrofit)
- **LocalDataSource** — работа с базой (Room)
- **DTO** (Data Transfer Object) — модели для сети/базы
- **Mapper** — конвертация DTO → Entity

```kotlin
// DTO — модель как приходит из сети (с аннотациями Retrofit/Gson)
data class ArticleDto(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?
)

// Mapper — конвертация, изолированная в одном месте
fun ArticleDto.toDomain(): Article = Article(
    id = title.hashCode().toString(), // упрощённо
    title = title,
    description = description,
    publishedAt = ""
)

// RepositoryImpl — реализует контракт Domain
class ArticleRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : ArticleRepository {
    override suspend fun getTopArticles(): List<Article> {
        return remoteDataSource.fetchTopArticles().map { it.toDomain() }
    }
}
```

---

### 3. Presentation (UI)
ViewModel + Compose. Знает о Domain (UseCase), не знает о Data.

```kotlin
class ArticleViewModel(
    private val getTopArticles: GetTopArticlesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun loadArticles() {
        viewModelScope.launch {
            _uiState.value = UiState.Success(getTopArticles())
        }
    }
}

sealed class UiState {
    object Loading : UiState()
    data class Success(val articles: List<Article>) : UiState()
    data class Error(val message: String) : UiState()
}
```

---

## Структура папок в Android проекте

```
com.example.app/
├── data/
│   ├── remote/
│   │   ├── dto/
│   │   └── ApiService.kt
│   ├── local/
│   │   └── dao/
│   ├── repository/
│   │   └── ArticleRepositoryImpl.kt
│   └── mapper/
│       └── ArticleMapper.kt
├── domain/
│   ├── model/
│   │   └── Article.kt
│   ├── repository/
│   │   └── ArticleRepository.kt       ← интерфейс
│   └── usecase/
│       └── GetTopArticlesUseCase.kt
└── presentation/
    ├── screen/
    │   └── home/
    │       ├── HomeViewModel.kt
    │       └── HomeScreen.kt
    └── navigation/
```

---

## Ключевые понятия для собеседования

| Термин | Коротко |
|--------|---------|
| **Separation of Concerns** | Каждый слой отвечает за своё |
| **Dependency Inversion** | Зависишь от абстракции, не от реализации |
| **Single Responsibility** | Один класс — одна причина для изменения |
| **UseCase** | Одна бизнес-операция, изолированная от UI и Data |
| **DTO vs Entity** | DTO — модель сети/БД, Entity — модель бизнеса |
| **Mapper** | Конвертация между слоями, изолирована |

---

## Частые вопросы на собеседовании

**"Зачем UseCase если можно вызвать Repository напрямую из ViewModel?"**
> UseCase изолирует бизнес-логику от UI. Если логика усложняется (фильтрация, комбинирование источников) — ViewModel не раздувается. Также UseCase легко тестировать отдельно.

**"Почему Domain не должен знать об Android?"**
> Domain — это чистая бизнес-логика. Если она зависит от Android, её нельзя протестировать JUnit тестами (нужен эмулятор). Чистый Kotlin — быстрые тесты.

**"Чем отличается Repository от DataSource?"**
> Repository — единая точка входа для Domain, решает *откуда взять данные* (кэш или сеть). DataSource — конкретный источник (только сеть или только БД).
