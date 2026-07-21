# Architecture Documentation

This document serves as an index for the technical architecture of the Shabbat Times app. 
The project follows modern Android practices with a focus on MVI, Clean Architecture, and type-safe components.

## Chapters

- [Core Architecture](architecture/core-architecture.md) — UI patterns, DI, Data/Domain layers, and Use Cases.
- [Current Location & Persistence](architecture/current-location-feature.md) — GPS detection logic, Room database structure, and repository strategies.
- [Date & Time Handling](architecture/date-time-handling.md) — Parsers, formatters, and utility extensions for temporal accuracy.
- [Ephemeris & Solar Position Engine](architecture/ephemeris-solar-position.md) — Detailed walkthrough of the astronomical calculation logic.
- [Halachic Times](architecture/halachic-times.md) — Domain models and display logic for Shabbat-specific calculations.
- [MVI Architecture](architecture/mvi-architecture.md) — Deep dive into the unidirectional data flow, state reduction, and effect handling.
- [Navigation](architecture/navigation.md) — Type-safe navigation system using Compose Navigation and Hilt.
- [Permissions Management](architecture/permissions-management.md) — Robust handling of location permissions and lifecycle synchronization.
- [Project Status](architecture/project-status.md) — Historical context, recently resolved issues, and roadmap.
- [Reorderable Cards](architecture/reorderable-cards.md) — Generic implementation of drag-to-reorder and swipe-to-delete.
- [Search Architecture](architecture/search-architecture.md) — Reactive search implementation for both autocomplete and GPS resolution.
- [Solar Times API](architecture/solar-times-api.md) — Integration with external solar data providers and DTO mapping.
