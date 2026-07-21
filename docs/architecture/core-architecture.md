# Core Architecture

The app is built using modern Android architecture principles and libraries.

## UI

The app uses Jetpack Compose as its declarative UI toolkit. 
It follows the Unidirectional Data Flow (UDF) pattern, where ViewModels expose an immutable UiState via StateFlow and the UI observes it using collectAsStateWithLifecycle to ensure lifecycle-aware updates.

```kotlin
@Composable
fun ShabbatScreen(snackbarHostState: SnackbarHostState) {
    val shabbatViewModel: ShabbatViewModel = hiltViewModel()
    val shabbatState by shabbatViewModel.state.collectAsStateWithLifecycle()

    val gpsViewMode: GpsViewModel = hiltViewModel()
    val gpsUiState by gpsViewMode.state.collectAsStateWithLifecycle()

    val searchViewModel: SearchViewModel = hiltViewModel()
    val searchUiState by searchViewModel.state.collectAsStateWithLifecycle()

    val permissionViewModel: PermissionViewModel = hiltViewModel()
    val permissionUiState by permissionViewModel.state.collectAsStateWithLifecycle()

    var returnedFromSettings by rememberSaveable { mutableStateOf(false) }

    HandlePermissions(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ),
        returnedFromSettings = returnedFromSettings,
        onSettingsHandled = { returnedFromSettings = false },
        permissionState = permissionUiState,
        dispatch = permissionViewModel::dispatch,
    )

    PermissionDialogs(
        permissionState = permissionUiState,
        onOpenSettings = { returnedFromSettings = true },
        dispatch = permissionViewModel::dispatch,
    )

    val context = LocalContext.current

    val onCardClick = {
        when (permissionUiState.permission.dispatchCardAction()) {
            CardAction.OpenGpsSearch         -> gpsViewMode.dispatch(GpsEvent.GpsLocationRequested)
            CardAction.ShowDeniedPermanently -> permissionViewModel.dispatch(PermissionEvent.TappedCardDeniedPermanently)
            CardAction.ShowDeniedRationale   -> permissionViewModel.dispatch(PermissionEvent.TappedCardDenied)
            CardAction.PermissionRequested   -> permissionViewModel.dispatch(PermissionEvent.PermissionRequested)
            CardAction.None                  -> Unit
        }
    }

    val searchConfig = SearchConfig(
        state = searchUiState.default(),
        action = searchViewModel.default(),
    )

    when (val entries = shabbatState.shabbat) {
        is ShabbatResultState.Idle    -> LoadingScreen()

        is ShabbatResultState.Loading -> LoadingScreen()

        is ShabbatResultState.Empty   -> {
            ShabbatContent(
                items = listOf(
                    ShabbatEntry(
                        location = SavedLocation.empty(),
                        times = null,
                        status = gpsUiState.gpsResult.toLocationStatus(permissionUiState.permission),
                    ),
                ).toImmutableList(),
                isDraggable = false,
                searchConfig = searchConfig,
                onClick = onCardClick,
            )
        }

        is ShabbatResultState.Ready   -> {
            ShabbatContent(
                items = entries.entries,
                swipeConfig = SwipeConfig(toLeft = SwipeState.Delete) { item ->
                    shabbatViewModel.dispatch(
                        ShabbatEvent.LocationDeleted(
                            savedLocation = item.location,
                            isCurrent = item.status == LocationStatus.Current,
                        )
                    )
                },
                searchConfig = searchConfig,
                onClick = onCardClick,
                onReorder = { from, to ->
                    shabbatViewModel.dispatch(
                        ShabbatEvent.ReorderLocations(from = from, to = to)
                    )
                },
            )
        }

        is ShabbatResultState.Failure -> FailureScreen(
            cause = entries.cause,
            onRetry = { shabbatViewModel.dispatch(ShabbatEvent.RetryLoadShabbatEntry) },
        )
    }

    LaunchedEffect(Unit) {
        merge(
            shabbatViewModel.effects,
            searchViewModel.effects,
            permissionViewModel.effects,
        ).collect { effect -> handleUiEffect(effect, context, snackbarHostState) }
    }
}
```

## DI

Dagger Hilt is used for Dependency Injection, managing the lifecycle of ViewModels, Repositories, and Use Cases. 
Custom qualifiers like @InMemory and @Persisted are employed to switch between transient and persistent data storage implementations seamlessly.

## @InMemory and @Persisted

Dagger qualifiers used to distinguish between repository implementations.
@InMemory uses transient StateFlow storage (ideal for testing), while @Persisted is backed by Room to ensure data survives app restarts.

### Qualifiers

Qualifiers are used to differentiate between different implementations of the same interface.

```kotlin
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InMemory

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Persisted
```

### Modules

Hilt modules provide the necessary bindings for the repository implementations.

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    @InMemory
    abstract fun bindSavedLocationsRepository(impl: SavedLocationsRepositoryInMemory): SavedLocationsRepository

    @Binds
    @Singleton
    @InMemory
    abstract fun bindCurrentLocationRepository(impl: CurrentLocationRepositoryImpl): CurrentLocationRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class PersistenceModule {

    @Binds
    @Singleton
    @Persisted
    abstract fun bindSavedLocationsRepo(impl: SavedLocationsRepositoryRoom): SavedLocationsRepository

    @Binds
    @Singleton
    @Persisted
    abstract fun bindCurrentLocationRepo(impl: CurrentLocationRepositoryRoom): CurrentLocationRepository
}
```

### Usage

Qualifiers are applied at the injection site to specify the desired repository implementation.

```kotlin
class SaveLocationUseCase @Inject constructor(
    @param:Persisted private val savedLocationsRepository: SavedLocationsRepository,
) {
    //...
}
```

## ViewModel 

The primary ViewModel for the main screen handles location updates and Shabbat time calculations.

```kotlin
@HiltViewModel
class ShabbatViewModel @Inject constructor(
    @param:InMemory private val currentLocationRepository: CurrentLocationRepository,
    @param:Persisted private val savedLocationsRepository: SavedLocationsRepository,
    private val reorderLocationsUseCase: ReorderLocationsUseCase,
    private val getHalachicTimesUseCase: GetHalachicTimesUseCase,
    private val removeLocationUseCase: RemoveSavedLocationUseCase,
    observeGpsLocationUseCase: ObserveGpsLocationUseCase,
    userPreferencesRepository: UserPreferencesRepository,
    permissionRepository: PermissionRepository,
    networkConnectivityObserver: NetworkObserver,
    oneTimeMessageTracker: OneTimeMessageTracker,
) : BaseViewModel(oneTimeMessageTracker) {
    private val reloadTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    init {
        viewModelScope.launch {
            networkConnectivityObserver.isConnected
                .distinctUntilChanged()
                .drop(1)
                .collect { connected ->
                    when (connected) {
                        true -> emitEffect(UiEffect.ShowToast(UiText.Resource(R.string.restored_internet)))
                        else -> emitEffect(UiEffect.ShowToast(UiText.Resource(R.string.error_no_internet)))
                    }

                    if (connected) { reloadTrigger.tryEmit(Unit) }
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val halachicTimesFlow: StateFlow<List<HalachicTimes>> = combine(
        currentLocationRepository.location,
        savedLocationsRepository.locations,
        userPreferencesRepository.shabbatPreferences,
        reloadTrigger.onStart { emit(Unit) },
    ) { gpsLocation, savedLocations, preferences, _ ->
        val locations = buildList {
            gpsLocation?.let { add(it) }
            addAll(savedLocations)
        }

        locations to preferences
    }.flatMapLatest { (savedLocations, preferences) ->
        flow {
            val results = getHalachicTimesUseCase(savedLocations, preferences)
            val successes = results.filterIsInstance<NetworkResult.Success<HalachicTimes>>()
                .map { it.data }

            successes.toUnavailabilityWarning()?.let { message ->
                emitOnce(OneTimeMessage.UNAVAILABLE_TIME_WARNING, UiEffect.ShowSnackBar(message))
            }
            emitBatchOutcome(results.toBatchOutcome())

            emit(successes)
        }
    }
        .catch { cause ->
            dispatch(ShabbatEvent.ShabbatEntryLoadFailed(cause))
            emitEffect(UiEffect.ShowToast(cause.userMessage()))
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    private val _state: MutableStateFlow<ShabbatUiState> = MutableStateFlow(value = ShabbatUiState())

    val state: StateFlow<ShabbatUiState> = combine(
        _state,
        halachicTimesFlow,
        observeGpsLocationUseCase(),
        savedLocationsRepository.locations,
        permissionRepository.permissionState,
    ) { state, halachicTimes, currentLocationState, savedLocations, permission ->
        ShabbatEvent.ShabbatEntryLoaded(savedLocations, currentLocationState, halachicTimes, permission).reducer reduce state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ShabbatUiState(),
    )

    fun dispatch(event: UiEvent) {
        _state.updateAndGet { current ->
            when (event) {
                is ShabbatEvent -> event.reducer reduce current
                else            -> current
            }
        }

        when (event) {
            is ShabbatEvent.LocationDeleted  -> handleDeleteLocation(event)
            is ShabbatEvent.ReorderLocations -> handleReorderLocations(event)
            else                             -> Unit
        }
    }

    private fun handleReorderLocations(event: ShabbatEvent.ReorderLocations) {
        viewModelScope.launch {
            val entries = (state.value.shabbat as? ShabbatResultState.Ready)?.entries ?: return@launch
            reorderLocationsUseCase(entries, event.from, event.to)
        }
    }

    private fun handleDeleteLocation(event: ShabbatEvent.LocationDeleted) {
        viewModelScope.launch {
            removeLocationUseCase(event.savedLocation, event.isCurrent)
        }
    }
}
```

## Networking

Networking is handled by Retrofit, which provides a type-safe HTTP client interface.
- OkHttp is used as the underlying engine, configured with interceptors for logging and timeouts. 
- Responses are parsed using Kotlinx Serialization.

### Why two `@Provides` functions for the same type?

Both `SolarTimesRetrofit` and `GeoapifyRetrofit` return `Retrofit`, but each is
configured with a different base URL for a different external service. Since
Hilt resolves dependencies by type, two unqualified `Retrofit` providers would
be ambiguous — Hilt wouldn't know which one to inject where.

The `@SolarTimesRetrofit` / `@GeoapifyRetrofit` qualifier annotations make each
binding distinct in the dependency graph, so consumers request the specific
instance they need:

```kotlin
class SolarTimesApi @Inject constructor(
    @SolarTimesRetrofit private val retrofit: Retrofit
)
```

```kotlin
@Provides
@Singleton
@SolarTimesRetrofit
fun provideSolarTimesRetrofit(): Retrofit {
    val client = OkHttpClientFactory.create(Debug.enabled)
    val contentType = "application/json".toMediaType()

    return Retrofit.Builder()
        .baseUrl(BASE_SUNRISE_SUNSET)
        .client(client)
        .addConverterFactory(JsonConfig.json.asConverterFactory(contentType))
        .build()
}
```

```kotlin
@Provides
@Singleton
@GeoapifyRetrofit
fun provideGeoapifyRetrofit(): Retrofit {
    val client = OkHttpClientFactory.create(Debug.enabled)
    val contentType = "application/json".toMediaType()

    return Retrofit.Builder()
        .baseUrl(BASE_GEOAPIFY)
        .client(client)
        .addConverterFactory(JsonConfig.json.asConverterFactory(contentType))
        .build()
}
```

## Key Components

The data layer follows clean architecture principles with repository abstractions.

| Repository | Responsibility |
|------------|----------------|
| `GpsLocationRepository` | Live device GPS, emits raw `Flow<Location?>` — never persisted                                                                      |
| `CurrentLocationRepository` | Shared GPS-resolved location between ViewModels. `GpsViewModel` writes via `UpdateCurrentLocationUseCase`, `ShabbatViewModel` reads |
| `SavedLocationsRepository` | User-saved locations CRUD (Room)                                                                                                    |
| `GeocodingRepository` | Reverse geocode + autocomplete via Geoapify                                                                                         |
| `SolarTimesRepository` | Fetches solar times per `SolarTimesRequest` via sunrisesunset.io API                                                                          |

## KotlinX Serialization

Kotlinx Serialization is the primary JSON library. 
It is chosen for its Kotlin-first design, compile-time safety, and Multiplatform readiness, offering better integration with Kotlin features like default values compared to Java-centric alternatives like Moshi or GSON.

## Domain Layer

Core domain models represent the business logic and data structures of the application.

| Model | Description                                                                                                          |
|-------|----------------------------------------------------------------------------------------------------------------------|
| `SavedLocation`     | User-saved location with coordinates and timezone. Persisted via Room.                                               |
| `ResolvedLocation`  | Geocoding API result. Never persisted — converted to `SavedLocation` on user save                                    |
| `SolarTimesRequest` | Entry point for times domain. Contains coordinates, timezone and date only — no city concept                         |
| `HalachicTimes`     | Raw Shabbat times (TimeState) — candle lighting and havdalah                                                         |
| `ShabbatEntry`      | UI model combining `SavedLocation` with `HalachicTimesDisplay` and `LocationStatus`                                  |
| `Coordinates`       | Latitude/longitude pair, always normalized to 4 decimal places for reliable matching                                 |
| `TimeState`         | Represents the status of a specific halachic time (e.g., Calculated, Missing, or Unavailable), wrapping a LocalTime. |
| `LocationStatus`         | Dynamic state of a location entry relative to the user (e.g., Current, Nearby, or Locating)                          |
| `LocationPermission`         | Domain representation of the system GPS permission state (Granted, Denied, Rationale needed)                         |
| `ShabbatCalendar`         | Utility for calculating the relevant dates for upcoming Shabbat events (Friday/Saturday)                             |

## Use Cases

Use cases encapsulate specific business logic and coordinate data flow between repositories.

| Use Case | Responsibility |
|----------|----------------|
| `GetHalachicTimesUseCase` | Computes candle lighting and havdalah times from solar times                                |
| `GetLocationSuggestionsUseCase` | Fetches address suggestions from the geocoding service based on partial user input |
| `ObserveGpsLocationUseCase` | Observes GPS permission state, emits raw `Location?` when granted                           |
| `RemoveSavedLocationUseCase` | Removes saved location                                                                      |
| `ReorderLocationsUseCase` | Updates the persistent sort order of locations in the database after a drag-and-drop interaction                                                                      |
| `ResolveGpsLocationUseCase` | Combines GPS permission + location + reverse geocoding into single `Flow<ResolvedLocation?>` |
| `SaveLocationUseCase` | Converts `ResolvedLocation` → `SavedLocation` and persists                                  |
| `SaveShabbatPreferenceUseCase` | Persists user-defined offsets for candle lighting and havdalah                                                                        |
| `ShabbatPreferencesUseCase` | Provides a reactive stream of the user's current halachic calculation settings                                                                       |
| `UpdateCurrentLocationUseCase` | Converts `ResolvedLocation?` → `SavedLocation?` and updates `CurrentLocationRepository`     |

```kotlin
class ResolveGpsLocationUseCase @Inject constructor(
    private val geocodingRepository: GeocodingRepository,
    private val observeGpsLocation: ObserveGpsLocationUseCase,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<GpsResultState> =
        observeGpsLocation()
          .flatMapLatest { state ->
              when (state) {
                is CurrentLocationState.Idle -> flowOf(GpsResultState.Idle)
                is CurrentLocationState.Fetching -> flowOf(GpsResultState.Loading)
                is CurrentLocationState.Available -> resolveLocation(state.coordinates)
              }
          }
    
    private fun resolveLocation(coordinates: Coordinates): Flow<GpsResultState> = flow {
        emit(GpsResultState.Loading)
        val result = geocodingRepository.reverseGeocode(coordinates)
        emit(
            result.fold(
              onSuccess = { GpsResultState.Resolved(it) },
              onFailure = { GpsResultState.Failure(it) }
            )
        )
    }
}
```
