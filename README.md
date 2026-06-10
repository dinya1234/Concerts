# ConcertApp — Афиша концертов

Android-приложение для просмотра афиши концертов, добавления в избранное, покупки билетов и управления ими в личном кабинете. Демонстрирует архитектуру MVVM, offline-first подход, работу с Room (кэш), Retrofit (сеть), Dagger Hilt и Navigation Compose.

##  Функциональность

- **Список концертов** — просмотр всех концертов с фильтрацией по городу
- **Детальная страница** — полная информация о концерте, добавление/удаление из избранного
- **Покупка билета** — форма с валидацией email, подтверждение покупки
- **Личный кабинет** — отдельный экран с двумя списками:
  - Избранные концерты (кликабельны для перехода к деталям)
  - Купленные билеты (с указанием email получателя)
- **Offline-first** — при отсутствии интернета показываются кэшированные данные с понятным сообщением
- **Тёмная тема** — автоматически следует за системными настройками (Material 3 Dynamic Color)

##  Архитектура

**Clean Architecture + MVVM + Repository Pattern**

| Слой | Компоненты | Описание |
|------|-----------|----------|
| **UI** | Compose экраны (4 шт) | Отображение данных, обработка действий пользователя |
| **ViewModel** | ConcertListViewModel, ConcertDetailViewModel, PurchaseViewModel, ProfileViewModel | Бизнес-логика, управление UiState/UiEvent через StateFlow |
| **Repository** | ConcertRepository | Единый источник данных, связывает Room и Retrofit |
| **Data** | Room (2 Entity), Retrofit | Локальная БД (кэш + избранное + билеты) и сетевой API |
| **DI** | Dagger Hilt | Внедрение зависимостей (DatabaseModule, NetworkModule, RepositoryModule) |

### Поток данных (offline-first):

API (Retrofit) → Repository → Room (кэш) → Flow → ViewModel (UiState) → UI (Compose)



1. При старте ViewModel вызывает `repository.refreshConcerts()`
2. Repository пытается загрузить данные из API
3. При успехе — данные сохраняются в Room
4. При ошибке сети — проверяется наличие кэша
5. Если кэш есть → показываются данные + сообщение "Нет сети. Показаны сохранённые концерты"
6. Если кэша нет → показывается экран ошибки с кнопкой Retry
7. UI получает данные через Flow из Room → `collectAsState()`

##  Технологии

- **Язык:** Kotlin
- **UI:** Jetpack Compose + Material Design 3
- **Архитектура:** MVVM (ViewModel + StateFlow)
- **Управление состоянием:** Sealed interface (UiState / UiEvent)
- **Локальная БД:** Room (Flow, suspend, UPDATE, WHERE)
- **Сеть:** Retrofit + Gson
- **Навигация:** Navigation Compose (4 экрана, передача аргументов)
- **Асинхронность:** Kotlin Coroutines (viewModelScope, Dispatchers.IO)
- **Внедрение зависимостей:** Dagger Hilt
- **Сущности:**
  - `ConcertEntity` — концерты (поля: id, artist, venue, date, city, description, price, isFavorite)
  - `TicketEntity` — купленные билеты (поля: id, concertId, artist, venue, date, city, price, email)

##  Структура проекта

```
com.example.kt_5/
├── data/
│ ├── local/
│ │ ├── entity/
│ │ │ ├── ConcertEntity.kt ← Концерты (с полем isFavorite)
│ │ │ └── TicketEntity.kt ← Билеты (с email покупателя)
│ │ ├── dao/
│ │ │ ├── ConcertDao.kt ← SQL: getAll, getByCity, getFavorites, updateFavorite
│ │ │ └── TicketDao.kt ← SQL: getAll, insert
│ │ └── AppDatabase.kt ← База данных (version 2)
│ ├── remote/
│ │ ├── dto/
│ │ │ └── ConcertDto.kt ← DTO + toEntity()
│ │ └── ConcertApi.kt ← Retrofit интерфейс
│ └── repository/
│ └── ConcertRepository.kt ← Связь Room + API (offline-first логика)
├── di/
│ ├── DatabaseModule.kt ← Room + Dao
│ ├── NetworkModule.kt ← Retrofit + ConcertApi
│ └── RepositoryModule.kt ← ConcertRepository
├── ui/
│ ├── list/ ← Экран списка (с фильтром по городу)
│ │ ├── ConcertListUiState.kt
│ │ ├── ConcertListUiEvent.kt
│ │ ├── ConcertListViewModel.kt
│ │ └── ConcertListScreen.kt
│ ├── detail/ ← Детали концерта + избранное
│ │ ├── ConcertDetailUiState.kt
│ │ ├── ConcertDetailUiEvent.kt
│ │ ├── ConcertDetailViewModel.kt
│ │ └── ConcertDetailScreen.kt
│ ├── purchase/ ← Покупка билета (валидация email)
│ │ ├── PurchaseUiState.kt
│ │ ├── PurchaseUiEvent.kt
│ │ ├── PurchaseViewModel.kt
│ │ └── PurchaseScreen.kt
│ ├── profile/ ← Личный кабинет (избранное + билеты)
│ │ ├── ProfileUiState.kt
│ │ ├── ProfileUiEvent.kt
│ │ ├── ProfileViewModel.kt
│ │ └── ProfileScreen.kt
│ └── theme/ ← Тёмная/светлая тема
│ ├── Color.kt
│ ├── Theme.kt
│ └── Type.kt
├── navigation/
│ └── AppNavigation.kt ← NavHost (4 экрана + аргументы)
├── ConcertApp.kt ← @HiltAndroidApp
└── MainActivity.kt ← @AndroidEntryPoint, setContent
```



##  API

**Публичный API (npoint.io):**

- **Базовый URL:** `https://api.npoint.io/`
- **Эндпоинт:** `7f92b42e55fc57670ab4/concerts`
- **Метод:** GET
- **Формат ответа:** JSON (массив объектов ConcertDto)
- **Ограничения:** публичный, не требует API ключа

**Пример ответа:**
```json
[
  {
    "id": 1,
    "artist": "Imagine Dragons",
    "venue": "СКК Арена",
    "date": "15.06.2026",
    "city": "Санкт-Петербург",
    "description": "Грандиозное шоу...",
    "price": 3500.0
  }
]
```

##  Состояния UI

| Состояние | Экран | Когда показывается |
|-----------|-------|-------------------|
| **Loading** | Все экраны | При первой загрузке данных |
| **Success** | Список, Детали, Профиль | Данные загружены успешно |
| **Error** | Список | Ошибка сети и нет кэша + кнопка Retry |
| **Empty** | Список, Профиль | Нет концертов / избранного / билетов |
| **Ready** | Покупка | Концерт загружен, форма готова |
| **Confirmed** | Покупка | Билет успешно куплен |
| **Offline message** | Список | Нет интернета, но данные из кэша отображаются |

##  Навигация (4 экрана)

| Маршрут | Параметры | Описание |
|---------|-----------|----------|
| `list` | — | Список концертов с фильтром по городу |
| `detail/{concertId}` | Int | Детальная информация + избранное |
| `purchase/{concertId}` | Int | Форма покупки билета (email) |
| `profile` | — | Личный кабинет (избранное + билеты) |

##  Запуск проекта

### Требования
- Android Studio Hedgehog | 2023.1.1 или новее
- minSdk: 24 (Android 7.0)
- targetSdk: 34 (Android 14)

### Инструкция

1. **Клонировать репозиторий**
   ```bash
   git clone <url-репозитория>
   cd kt_5
2. Открыть в Android Studio
   ```bash
      File → Open → выбрать папку проекта

3. Синхронизировать Gradle
   ```bash
      File → Sync Project with Gradle Files

4. Запустить приложение
   ```bash
      Выбрать эмулятор или физическое устройство

      Нажать Run ▶ (Shift + F10)

## Автор

Волохова Д.А
