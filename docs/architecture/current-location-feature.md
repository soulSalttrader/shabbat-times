# Current Location Feature

Detects the user's current location via GPS, reverse geocodes it to a named location, and displays it alongside manually saved locations with dynamic distance labels.

## High-Level Flow

The following sequence describes the process of detecting and displaying the user's current location.

1. User taps placeholder card → `GpsEvent.GpsLocationRequested` dispatched to `GpsViewModel`
2. Permission flow handled by `PermissionViewModel` → `LocationPermission.Granted`
3. `ResolveGpsLocationUseCase` - permission-gated GPS + reverse geocode → `ResolvedLocation`
4. `GpsViewModel` writes to two repositories via `onEach`:
  - `UpdateCurrentLocationUseCase` → `CurrentLocationRepository` (for display)
  - `SaveLocationUseCase` → `SavedLocationsRepository` (for times fetching)
5. `ShabbatViewModel` reacts to both repository emissions automatically:
  - `currentLocationRepository.location` → distance/status computation
  - `savedLocationsRepository.locations` → `halachicTimesFlow` refetches
6. Times fetched per `Coordinates` - GPS location treated same as saved locations
7. `LocationStatus` computed dynamically per entry from permission + GPS distance

## ViewModels

The logic for the current location feature is distributed across three primary ViewModels.

| ViewModel | Responsibility |
|-----------|----------------|
| `PermissionViewModel` | Permission UI flow (rationale, education, settings) |
| `GpsViewModel` | GPS resolution + writes to `CurrentLocationRepository` |
| `ShabbatViewModel` | Pure observer - combines locations + times + permission → `ShabbatUiState` |

## Key Components

Several specialized components coordinate to ensure location data is accurately resolved and shared.

| Component | Responsibility |
|-----------|----------------|
| `GpsLocationRepository` | Live device GPS, emits `Flow<Location?>` |
| `CurrentLocationRepository` | Shared GPS state bridge - `GpsViewModel` writes, `ShabbatViewModel` reads |
| `PermissionRepository` | Permission state, gates GPS stream |
| `ResolveGpsLocationUseCase` | Combines permission + GPS + reverse geocode → `Flow<GpsResultState>` |
| `UpdateCurrentLocationUseCase` | Converts `ResolvedLocation?` → `SavedLocation?`, updates `CurrentLocationRepository` |
| `SaveLocationUseCase` | Converts `ResolvedLocation` → `SavedLocation`, persists to `SavedLocationsRepository` |
| `RemoveLocationUseCase` | Removes saved location from `SavedLocationsRepository` |
| `ShabbatCalendar` | Provides upcoming Friday/Saturday dates, injectable and testable |

## Saved Location

The `SavedLocation` model represents a location that has been persisted or is being used for calculation.

```kotlin
@Immutable
@Serializable
data class SavedLocation(
    val id: String,
    val name: String,
    val coordinates: Coordinates,
    @Serializable(with = ZoneIdAsStringSerializer::class)
    val timeZoneId: ZoneId,
) {
    companion object {
        const val EMPTY_NAME = "Tap to use current location"
        const val GPS_ID = "gps"
        const val EMPTY_ID = "empty"
        const val LOCATION_ID = "location"

        fun empty() = SavedLocation(
            id = EMPTY_ID,
            name = EMPTY_NAME,
            coordinates = Coordinates.EMPTY,
            timeZoneId = ZoneId.systemDefault(),
        )

        fun isEmpty(location: SavedLocation) = location.id == EMPTY_ID
    }
}
```

## Location Status

Computed dynamically in `LocationWithTimesLoaded` reducer - never stored:

| Status               | Condition                                      | Label                         |
|----------------------|------------------------------------------------|-------------------------------|
| `Current`            | GPS coordinates match saved location (< 0.1km) | "Your current location"       |
| `LastKnownLocation`  | GPS available, CurrentLocationState.Idle       | "Last known location"         |
| `Nearby(distanceKm)` | GPS available, distance ≥ 0.1km                | "123.4 km away"               |
| `Locating`           | `LocationPermission.Requesting`                | "Getting your location..."    |
| `NoPermission`       | `LocationPermission.Denied/DeniedPermanently`  | "Tap to use current location" |
| `Unknown`            | No GPS available                               | "Unknown distance"            |

## Design Decisions

The following design choices prioritize reactivity and a clear separation of concerns.

- **GPS resolution owned by `GpsViewModel`** - owns the lifecycle of the GPS resolution process
- **`CurrentLocationRepository` as shared state bridge** - same pattern as `PermissionRepository`
- **`ShabbatViewModel` is a pure observer** - never resolves GPS, never writes to repositories
- **GPS location saved with fixed `GPS_ID`** - upserted on every update, never duplicates
- **Times fetched per `Coordinates`** - GPS and saved locations treated identically by times domain
- **`LocationStatus` computed from permission + distance** - never stored, always derived

## Persistence

The app uses **Room** for local persistence:

- **`saved_locations`** table - stores user-saved locations including GPS entry
  - `sortOrder` column preserves drag-to-reorder position across restarts
  - GPS location (`SavedLocation.GPS_ID`) upserted on each resolve - position preserved
- **`current_location`** table - single-row table. 
  - Although Room is available via `@Persisted`, the app currently uses the `@InMemory` implementation in `ShabbatViewModel` and `UpdateCurrentLocationUseCase` by design, so GPS always re-fetches a fresh location on restart.

## Repository Strategy

The app employs different repository implementations depending on the required storage behavior.

| Qualifier | Implementation | Use case |
|-----------|----------------|----------|
| `@InMemory` | In-memory `StateFlow` | Testing, debugging |
| `@Persisted` | Room-backed | Production |

Switch between them via qualifier at injection site - no other code changes needed.

## Known Limitations

The following limitations are known and targeted for future improvements.

- `fallbackToDestructiveMigration(true)` active - schema changes wipe data
- Real migrations needed before production release
