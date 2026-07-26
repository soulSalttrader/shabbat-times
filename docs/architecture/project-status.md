# Project Status

## Planned

- **Localization** - add and integrate Hebrew and Czech locale support using localization files.
- **Room migrations** - add real migration paths before removing fallbackToDestructiveMigration
- **Modularization** - split the single module into feature/data/domain modules
- **Kotlin-first libraries** - migrate from current stack toward Ktor, Koin, etc.
- **Kotlin Multiplatform readiness** - restructure shared logic to be KMP-compatible
- **Proxy backend** - introduce a backend proxy layer

## History

- ~~Permission state always Idle on restart~~ → synced on app start, GPS auto-refreshes if granted
- ~~Drag order lost on app restart~~ → persisted via Room with `sortOrder` column
- ~~Drag order lost during session~~ → preserved via `LaunchedEffect(items.size)` on recomposition
- ~~Hard-coded Jerusalem location~~ → dynamic GPS location + user-saved locations
- ~~Hard-coded 12/24h preference~~ → API always requests 24h format, display formatting in UI layer
- ~~City-coupled times domain~~ → times domain decoupled, uses `Coordinates` + `ZoneId` only
- ~~Adopt UiText for all hardcoded strings~~ → all user-facing strings use `UiText` / `strings.xml`
- ~~User-customizable candle lighting and havdalah offsets~~ → replaced with community tradition presets
