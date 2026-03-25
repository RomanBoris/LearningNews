# Git — Подробный конспект с логикой

---

## Что такое Git и зачем?

Git — программа которая **следит за изменениями в файлах** и хранит всю историю.
Представь что каждый коммит — это снимок всего проекта в момент времени.

```
Время ────────────────────────────────────────────>

📸 v1        📸 v2        📸 v3        📸 v4
"Init"    "Add login"  "Fix crash"  "Add UI"
```

Можно вернуться к любому снимку. Можно работать параллельно в нескольких версиях (ветки).

**GitHub** — это просто хостинг для этих снимков. Git живёт локально на твоём компе, GitHub — в интернете.

---

## Три зоны Git

Это самое важное для понимания. В Git есть три места где живёт код:

```
┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐
│  Рабочая папка  │──add──▶    Staging       │─commit─▶  Репозиторий  │
│  (твои файлы)   │       │  (индекс)        │       │  (история)      │
│                 │◀─restore│  "корзина"      │       │                 │
└─────────────────┘       └─────────────────┘       └─────────────────┘
   Ты редактируешь           Готово к коммиту          Уже сохранено
```

- **Рабочая папка** — файлы на диске. Ты их редактируешь как обычно.
- **Staging (индекс)** — промежуточная зона. Ты сам решаешь что туда положить перед коммитом. Смысл: можно изменить 5 файлов, но закоммитить только 2 — те что относятся к одной задаче.
- **Репозиторий** — вся история. Каждый коммит здесь навсегда (почти).

Поэтому стандартный цикл:
```bash
# 1. Посмотреть что изменилось
git status

# 2. Добавить нужные файлы в staging
git add DataSealed.kt

# 3. Сохранить снимок
git commit -m "Add MathResult sealed class"
```

---

## Разбор флагов по логике

### `git commit -m "сообщение"`

**Что делает `-m`?**

`-m` = `--message`. Флаг для передачи текста сообщения прямо в команде.

```bash
git commit -m "Add login screen"
#           └── "дай сообщение прямо здесь"
```

**А что если НЕ писать `-m`?**

Git откроет текстовый редактор (vim/nano/notepad) и будет ждать пока ты напишешь сообщение там:

```bash
git commit
# ↑ откроется редактор:
# |
# | # Please enter the commit message for your changes.
# | # Lines starting with '#' will be ignored.
# | # ...
```

Это удобно для длинных сообщений с описанием. Но в 99% случаев хватает `-m`.

**Другие варианты:**
```bash
git commit -m "Заголовок" -m "Подробное описание здесь"
# Первый -m = заголовок, второй -m = тело сообщения (два абзаца)
```

---

### `git push -u origin main`

Это самая "магическая" команда, разберём каждый кусок:

```
git push  -u        origin       main
  │        │           │           │
  │        │           │           └── Какую ветку пушить (локальную)
  │        │           └────────────── Куда пушить (имя удалённого репозитория)
  │        └────────────────────────── Привязать (запомнить связь)
  └─────────────────────────────────── Отправить коммиты
```

**`origin` — что это?**

Когда ты связываешь локальный проект с GitHub, удалённому репозиторию даётся имя. По умолчанию оно `origin`. Это просто псевдоним для длинного URL.

```bash
# Посмотреть что скрыто за словом origin:
git remote -v

# Вывод:
# origin  https://github.com/username/repo.git (fetch)
# origin  https://github.com/username/repo.git (push)
```

Можно назвать иначе — но `origin` это индустриальный стандарт, не придумывай другие имена.

```bash
# Технически можно, но не надо:
git remote add github https://github.com/username/repo.git
git push github main

# Так — правильно:
git remote add origin https://github.com/username/repo.git
git push origin main
```

**`-u` — что это?**

`-u` = `--set-upstream`. Устанавливает связь между локальной веткой и удалённой.

```
До -u:                          После -u:
┌──────────────┐                ┌──────────────┐
│ LOCAL        │                │ LOCAL        │
│  main        │    нет связи   │  main ───────┼──────┐
│              │                │              │      │
└──────────────┘                └──────────────┘      │ привязка
                                                       │
┌──────────────┐                ┌──────────────┐      │
│ GITHUB       │                │ GITHUB       │      │
│  main        │                │  main ◀──────┼──────┘
└──────────────┘                └──────────────┘
```

Благодаря этой привязке, **все следующие команды работают без аргументов**:

```bash
git push         # знает: слать в origin/main
git pull         # знает: тянуть из origin/main
```

**Что если НЕ использовать `-u`?**

```bash
git push origin main    # работает, но без привязки
git push                # ОШИБКА: "fatal: no upstream configured"
```

Без `-u` каждый раз нужно писать полностью: `git push origin main`

**Нужно ли `-u` каждый раз?**

Нет. Только **один раз** при первом пуше новой ветки. Дальше просто `git push`.

```bash
git checkout -b topic-8-interfaces
git push -u origin topic-8-interfaces   # ← первый раз с -u
# ... коммиты ...
git push                                 # ← все последующие без -u
```

---

### `git checkout -b название-ветки`

```bash
git checkout -b feature-login
#              └── "и сразу создай ветку"
```

**Без `-b`** — просто переключиться на существующую ветку:
```bash
git checkout master        # переключиться на master (ветка должна существовать)
git checkout -b new-branch # создать NEW-BRANCH и переключиться на неё
```

Это короткая запись двух команд:
```bash
git branch feature-login      # создать
git checkout feature-login    # переключиться
# ↑ то же самое что git checkout -b feature-login
```

Современный синоним (с Git 2.23):
```bash
git switch -c feature-login    # -c = create, аналог checkout -b
```

---

### `git clone <url>` vs `git init + git remote add origin`

Два способа начать работу с репозиторием:

**Вариант 1 — клонировать существующий:**
```bash
git clone https://github.com/username/repo.git
# ↑ автоматически:
#   - создаёт папку repo/
#   - скачивает весь код
#   - настраивает origin → этот url
#   - ставит -u для веток
```

**Вариант 2 — создать новый и связать с GitHub:**
```bash
git init                                          # создать .git/ в текущей папке
git remote add origin https://github.com/...git   # сказать "origin это вот этот url"
git push -u origin master                         # первый пуш
```

В нашем проекте мы делали вариант 2 — проект уже был, потом подключили GitHub.

---

## Ветки — логика и картинки

### Что такое ветка

Ветка — это просто **указатель на коммит**. Когда ты делаешь коммит, указатель двигается вперёд.

```
master: A ── B ── C
                  ↑
               HEAD (ты здесь)
```

`HEAD` — это "ты сейчас здесь". Всегда указывает на текущий коммит.

### Создаём новую ветку

```bash
git checkout -b topic-8-interfaces
```

```
             master
               ↓
A ── B ── C ── D
               ↑
    topic-8-interfaces (новая, пока то же самое)
               ↑
             HEAD
```

### Делаем коммиты в ветке

```
             master
               ↓
A ── B ── C ── D ── E ── F
                         ↑
              topic-8-interfaces
                         ↑
                       HEAD
```

master остался на D. topic-8 пошла вперёд.

### Мержим обратно в master

```bash
git checkout master
git merge topic-8-interfaces
```

```
A ── B ── C ── D ── E ── F
               ↑          ↑
             (было)      master = topic-8-interfaces
                          ↑
                        HEAD
```

Fast-forward merge: если master не двигался пока мы работали в ветке, Git просто перемещает указатель master вперёд. Никаких конфликтов.

### Если master тоже двигался (работа в команде)

```
                   master
                     ↓
A ── B ── C ── D ── G ── H
               \
                E ── F
                     ↑
              topic-8-interfaces
```

Тогда Git создаёт **merge commit**:

```bash
git checkout master
git merge topic-8-interfaces
```

```
A ── B ── C ── D ── G ── H ── M (merge commit)
               \              ↑
                E ── F ───────┘
                              ↑
                     master = HEAD
```

---

## Полный цикл работы (наш формат)

```
┌─────────────────────────────────────────────────────────────────┐
│                    ОДИН ЦИКЛ ТЕМЫ                               │
│                                                                 │
│  1. Создаём ветку                                               │
│     git checkout -b topic-8-interfaces                          │
│     git push -u origin topic-8-interfaces                       │
│                                                                 │
│  2. Пишем код, коммитим после каждого задания                   │
│     git add src/interfaces/Interfaces.kt                        │
│     git commit -m "Add task 1.1 - Printable interface"          │
│                                                                 │
│  3. Пушим (можно после каждого коммита или в конце)             │
│     git push                                                    │
│                                                                 │
│  4. Тема завершена → мержим в master                            │
│     git checkout master                                         │
│     git merge topic-8-interfaces                                │
│     git push                                                    │
└─────────────────────────────────────────────────────────────────┘
```

---

## Удалённый и локальный репозиторий

```
┌─────────────────────────┐            ┌──────────────────────────┐
│       ЛОКАЛЬНО          │            │         GITHUB           │
│      (твой комп)        │            │        (облако)          │
│                         │            │                          │
│  master    ─────────────┼───push────▶│  origin/master           │
│  topic-8   ─────────────┼───push────▶│  origin/topic-8          │
│                         │            │                          │
│                         │◀──pull─────┼─  origin/master          │
│                         │◀──fetch────┼─  origin/master          │
└─────────────────────────┘            └──────────────────────────┘
```

**`git fetch` vs `git pull`:**

```bash
git fetch    # скачать изменения с GitHub → но НЕ применять к твоим файлам
             # можно посмотреть что изменилось: git diff origin/master

git pull     # скачать + сразу применить (merge)
             # = git fetch + git merge
```

`fetch` нужен чтобы посмотреть чужие изменения прежде чем применять. В одиночной работе `pull` всегда.

---

## Команды — полный список

### Просмотр состояния

```bash
git status                   # что изменено / в staging / не отслеживается
git log                      # полная история коммитов
git log --oneline            # коротко: хеш + сообщение
git log --oneline -5         # последние 5 коммитов
git log --oneline --graph    # с визуализацией веток
git diff                     # что изменено (НЕ в staging)
git diff --staged            # что в staging (войдёт в следующий коммит)
git diff master..topic-8     # разница между двумя ветками
```

### Staging

```bash
git add файл.kt              # конкретный файл
git add src/interfaces/      # папка целиком
git add .                    # всё изменённое в текущей папке (осторожно)
git restore --staged файл.kt # убрать из staging (файл остаётся изменённым)
```

### Коммиты

```bash
git commit -m "сообщение"              # коммит с сообщением
git commit -m "заголовок" -m "тело"   # коммит с заголовком и телом
git commit                             # открыть редактор для длинного сообщения
```

### Ветки

```bash
git branch                        # список веток (* = текущая)
git branch feature-x              # создать ветку (без переключения)
git checkout feature-x            # переключиться на ветку
git checkout -b feature-x         # создать + переключиться
git switch feature-x              # современный аналог checkout
git switch -c feature-x           # современный аналог checkout -b
git branch -d feature-x           # удалить ветку (только если смержена)
git branch -D feature-x           # удалить ветку принудительно
```

### Удалённый репозиторий

```bash
git remote -v                             # показать все привязанные remote
git remote add origin <url>              # привязать remote с именем origin
git push -u origin ветка                 # первый пуш ветки (с привязкой)
git push                                  # пуш (после того как привязка есть)
git push origin ветка                    # пуш без -u (без привязки)
git pull                                  # стянуть + применить
git fetch                                 # стянуть без применения
```

### Отмена изменений

```bash
git restore файл.kt          # вернуть файл к состоянию последнего коммита
git restore --staged файл.kt # убрать из staging
git reset --soft HEAD~1      # отменить последний коммит (изменения останутся в staging)
git reset --mixed HEAD~1     # отменить коммит + убрать из staging (изменения в файлах)
git stash                    # спрятать все незакоммиченные изменения
git stash pop                # достать спрятанное
git stash list               # список всего спрятанного
```

---

## Как называть коммиты

**Формат**: `Глагол + что сделано`. Коротко, по-английски.

```bash
# Плохо:
git commit -m "fix"
git commit -m "changes"
git commit -m "asdfgh"

# Хорошо:
git commit -m "Add Printable interface with basic implementation"
git commit -m "Fix divide by zero in Calculator"
git commit -m "Refactor WeatherSensor to use sealed class"
git commit -m "Remove unused imports"
```

Частые глаголы:
- `Add` — новая функциональность, новый файл
- `Fix` — исправление бага
- `Update` — изменение существующего
- `Remove` — удаление
- `Refactor` — переработка без изменения поведения
- `Rename` — переименование

---

## .gitignore

Файл `.gitignore` — список того что Git **не должен трогать**.

```gitignore
# Папки IDE — у каждого своя конфигурация, не нужна другим
../../.idea/
*.iml

# Скомпилированные файлы — генерируются из исходников
*.class
build/
out/

# Системные файлы Windows
Thumbs.db
desktop.ini

# Секреты — НИКОГДА не коммить!
.env
*.key
credentials.json
google-services.json
```

**Правило**: в git только исходный код. Всё генерируемое, временное, секретное — в .gitignore.

Важно: `.gitignore` работает только для **неотслеживаемых** файлов. Если файл уже закоммичен — `.gitignore` его не скроет.

---

## Конфликты слияния

Когда два человека изменили **один и тот же участок** одного файла:

```
<<<<<<< HEAD
val name = "Alice"         ← твоя версия (текущая ветка)
=======
val name = "Bob"           ← чужая версия (та что мержим)
>>>>>>> feature-branch
```

Решение:
1. Открой файл, найди маркеры `<<<<`, `====`, `>>>>`
2. Оставь правильный вариант (или объедини оба)
3. Удали все маркеры
4. `git add файл.kt`
5. `git commit`

В IDE (IntelliJ/Android Studio) есть визуальный merge tool — намного удобнее.

---

## Типичные ошибки

| Ошибка | Почему плохо | Правильно |
|--------|-------------|-----------|
| `git add .` везде | Можно случайно добавить .env или бинарники | `git add конкретный_файл.kt` |
| Коммит с паролями или .env | Секреты в истории навсегда (даже если потом удалишь) | Добавь в .gitignore ДО первого коммита |
| Один огромный коммит в конце | Невозможно понять что где менялось, откатить только часть | Коммить логическими кусками |
| Работа в master напрямую | В команде — конфликты и хаос, нет code review | Всегда через ветки |
| `git push --force` | Перезаписывает историю на сервере, можно уничтожить чужую работу | Почти никогда. Только `--force-with-lease` если очень надо |
| Не писать `.gitignore` сразу | .idea/ и build/ замусоривают историю | Создавай `.gitignore` при `git init` |

---

## Шпаргалка — типичный день

```bash
# Утро: получить свежий код
git pull

# Начать задачу
git checkout -b topic-8-interfaces
git push -u origin topic-8-interfaces

# В процессе: после каждого выполненного задания
git status
git add src/interfaces/Interfaces.kt
git commit -m "Add task 1.1 - Printable interface"
git push

# Тема закончена: влить в master
git checkout master
git merge topic-8-interfaces
git push
```
