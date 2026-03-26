# Теория: MVVM + UiState + StateFlow

## Рекомендуется к прочтению
- developer.android.com/topic/architecture/ui-layer
- developer.android.com/kotlin/flow/stateflow-and-sharedflow

---

## Зачем нужен MVVM?

Ты уже написал `HomeViewModel`. Но зачем он вообще существует?

Представь без него: Activity сама вызывает UseCase, сама хранит список новостей, сама обрабатывает ошибки. Что происходит когда пользователь поворачивает телефон?

Activity **пересоздаётся**. Все данные — потеряны. Снова запрос в сеть. Снова загрузка.

ViewModel живёт **дольше** чем Activity. Это её главный смысл.

---

## Три части MVVM

```
View (Composable)
    ↕ наблюдает за state
ViewModel
    ↕ вызывает
Model (UseCase → Repository → Data)
```

- **View** — только отображает. Никакой логики. Видит состояние — рисует его.
- **ViewModel** — хранит состояние UI, вызывает бизнес-логику, обрабатывает события от View.
- **Model** — данные и бизнес-логика (UseCase, Repository — это уже есть).

**Правило:** View никогда не знает откуда пришли данные. ViewModel никогда не знает как они отображаются.

---

## UiState — единственная правда об экране

Плохой подход — хранить много отдельных переменных:
```kotlin
var isLoading: Boolean
var newsList: List<News>
var errorMessage: String?
```

Проблема: можно оказаться в невозможном состоянии — `isLoading = true` и `newsList` не пустой одновременно.

Хороший подход — **одно sealed class состояние**:

```kotlin
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val news: List<News>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
```

Экран всегда в **одном** из трёх состояний. Невозможных комбинаций нет.

---

## StateFlow — поток состояний

`StateFlow` — это поток который:
1. Всегда имеет **текущее значение** (в отличие от обычного Flow)
2. Отправляет новое значение только если оно **изменилось**
3. Новый подписчик сразу получает **последнее** значение

```kotlin
// В ViewModel — приватный, изменяемый
private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)

// Наружу — только для чтения
val state: StateFlow<HomeUiState> = _state.asStateFlow()
```

Зачем два свойства? Это паттерн **инкапсуляции**:
- Только ViewModel знает как менять состояние
- View может только наблюдать

---

## Как View наблюдает за StateFlow

В Compose это делается через `collectAsState()`:

```kotlin
val state by viewModel.state.collectAsState()

when (state) {
    is HomeUiState.Loading -> { /* показать прогресс */ }
    is HomeUiState.Success -> { /* показать список */ }
    is HomeUiState.Error -> { /* показать ошибку */ }
}
```

`when` по sealed class — компилятор заставит обработать **все** варианты. Забыл один — ошибка компиляции. Это и есть сила sealed class.

---

## Жизненный цикл ViewModel

```
Activity создаётся    → ViewModel создаётся (если нет)
Activity поворот      → Activity пересоздаётся, ViewModel ЖИВЁТ
Activity нажал Back   → Activity уничтожается, ViewModel уничтожается
```

`viewModelScope` — корутин-скоуп привязанный к ViewModel. Когда ViewModel уничтожается — все корутины отменяются автоматически.

---

## На собеседовании спросят

- *"Чем StateFlow отличается от LiveData?"*
  StateFlow — часть Kotlin Coroutines, не зависит от Android, имеет начальное значение, лучше тестируется.

- *"Зачем sealed class для UiState, а не просто data class?"*
  sealed class гарантирует конечный набор состояний — компилятор проверяет полноту `when`.

- *"Что такое `asStateFlow()`?"*
  Возвращает read-only обёртку над MutableStateFlow — инкапсуляция изменяемого состояния.

---

## Что уже есть в проекте

Ты уже написал `HomeViewModel` и `HomeUiState` — структурно всё правильно. В заданиях доработаем детали и добавим View.
