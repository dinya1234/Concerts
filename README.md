# Концерты + поиск по городу + покупка билета

Android-приложение для просмотра афиши концертов. Демонстрирует архитектуру MVVM, работу с Room (кэш), Retrofit (сеть) и Repository (связка).

##  Функциональность

- Просмотр списка концертов (исполнитель, место, дата, цена)
- Детальная информация о концерте
- Кэширование данных в локальной базе Room
- Работа с публичным API (npoint.io)
- Обработка ошибок сети с кнопкой Retry
- Отображение кэша при отсутствии интернета

##  Архитектура

**MVVM + Repository + Room + Retrofit**

| Слой | Компоненты | Описание |
|------|-----------|----------|
| **UI** | Compose экраны | Отображение данных, обработка действий пользователя |
| **ViewModel** | ConcertListViewModel, ConcertDetailViewModel | Бизнес-логика, управление состоянием (StateFlow, UiState/UiEvent) |
| **Repository** | ConcertRepository | Единая точка доступа к данным, связывает Room и Retrofit |
| **Data** | Room (Entity, Dao, Database), Retrofit (Api, Dto) | Источники данных: локальная БД и сеть |

### Поток данных:

API (Retrofit) → Repository → Room (кэш) → Flow → ViewModel (UiState) → UI (Compose)


1. ViewModel вызывает `repository.refreshConcerts()`
2. Repository загружает данные из API (Dispatchers.IO)
3. Данные сохраняются в Room (Single Source of Truth)
4. UI получает данные через Flow из Room → `collectAsState()`
5. При ошибке сети показывается кэш из Room + кнопка Retry

##  Технологии

- **Язык:** Kotlin
- **UI:** Jetpack Compose + Material Design 3
- **Архитектура:** MVVM (ViewModel + StateFlow)
- **Управление состоянием:** UiState / UiEvent (sealed interface)
- **Локальная БД:** Room (Flow, suspend)
- **Сеть:** Retrofit
- **Навигация:** Navigation Compose (2 экрана, передача id)
- **Асинхронность:** Kotlin Coroutines (viewModelScope, Dispatchers.IO)
- **DI:** Ручное внедрение (AppModule)

##  Структура проекта

```
com.example.kt_5/
├── data/
│ ├── local/
│ │ ├── entity/ConcertEntity.kt ← Таблица Room
│ │ ├── dao/ConcertDao.kt ← SQL-запросы
│ │ └── AppDatabase.kt ← База данных
│ ├── remote/
│ │ ├── dto/ConcertDto.kt ← DTO + Mapper
│ │ └── ConcertApi.kt ← Интерфейс Retrofit
│ └── repository/
│ └── ConcertRepository.kt ← Связь Room + API
├── di/
│ └── AppModule.kt ← Создание зависимостей
├── ui/
│ ├── list/
│ │ ├── ConcertListContract.kt ← UiState + UiEvent
│ │ ├── ConcertListViewModel.kt ← Логика списка
│ │ └── ConcertListScreen.kt ← UI списка
│ └── detail/
│ ├── ConcertDetailContract.kt ← UiState + UiEvent
│ ├── ConcertDetailViewModel.kt ← Логика деталей
│ └── ConcertDetailScreen.kt ← UI деталей
├── navigation/
│ └── AppNavigation.kt ← Граф навигации
├── ConcertApp.kt ← Application
└── MainActivity.kt ← Точка входа
```


##  API

Публичный API размещён на npoint.io: https://api.npoint.io/6b21056c3371505aafba


##  Скриншоты

<div align="center">
  <img src="screenshots/list.jpg" width="300" alt="Список концертов"/>
  <img src="screenshots/detail.jpg" width="300" alt="Детали концерта"/>
</div>

##  Состояния UI

| Состояние | Когда показывается |
|-----------|-------------------|
| **Loading** | При первой загрузке данных |
| **Success** | Данные загружены, показывается список |
| **Error** | Ошибка сети, кнопка Retry |
| **Empty** | Список пуст |

## Запуск

1. Клонировать репозиторий
2. Открыть в Android Studio
3. Синхронизировать Gradle
4. Запустить на эмуляторе или устройстве (minSdk 24)

