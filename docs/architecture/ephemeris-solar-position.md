# Ephemeris & Solar Position Engine

## Overview

This document describes the astronomical calculation engine used to determine halachic times (zmanim). 
The system replaces hardcoded community presets with a precise mathematical model that calculates solar events based on Earth's orbital elements.

## Package Structure

The engine is organized into two primary packages, separating core astronomical calculations from observer-relative coordinate translations.

### 1. `settings.ephemeris`

This package contains the "Core Astronomical" logic based on Jean Meeus' *Astronomical Algorithms* and NOAA's solar calculations.

| Component | Responsibility |
|-----------|----------------|
| `MeanOrbitalElements` | Calculates Earth's orbital eccentricity, mean longitude, and mean anomaly. |
| `SunPositionCalculator` | Computes the Sun's true and apparent longitude. |
| `EquationOfTimeCalculator` | Handles the correction between apparent solar time and mean solar time. |
| `NoaaSolarEphemerisCalculator` | The primary entry point that assembles these elements into a `SolarEphemeris`. |

### 2. `settings.solarPosition`

Translates ephemeris data into observer-relative coordinates.

| Component | Responsibility |
|-----------|----------------|
| `EphemerisBasedSolarPositionCalculator` | Converts an `Instant` and `Coordinates` into a solar altitude in degrees. |
| `BinarySolarAltitudeSearch` | Uses a binary search algorithm to find the exact `Instant` when the Sun crosses a specific altitude. |
| `AstronomicalSolarDepressionCalculator` | Coordinates the search to find evening times for specific depression angles. |

## Data Flow in Settings
1. **User Selection**: The user selects a `HavdalahCriterion` (e.g., `Solar(8.5°)`) or `CandleLightingOffset` in `SettingsScreen`.
2. **Preference Persistence**: `UserPreferencesRepository` saves these to DataStore.
3. **Reactive Fetching**: `GetHalachicTimesUseCase` observes these preferences.
4. **Calculation**: The use case passes the target depression angle to the `SolarDepressionTimeCalculator`, which uses the Ephemeris engine to find the precise crossing time for the location's coordinates.

## Edge Cases: High Latitudes

In extreme geographic locations, certain solar events may not occur, necessitating specialized handling.
In regions where the Sun does not reach a required depression angle (e.g., "White Nights"), the `BinarySolarAltitudeSearch` returns `null`. The UI handles this via the `TimeState.Unavailable` model, displaying `NA_TIME` to the user.
