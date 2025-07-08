# 🌐 data-source-manager Module

## 🧩 Module Type
**Domain - Data Source Module** – Bridges local storage and remote network layers while exposing a unified data access layer to the rest of the app.

## 🎯 Purpose

The `data-source-manager` module is responsible for:
- Managing data retrieval from both **network and local storage**.
- **Persisting API results** into the local Room database.
- Returning **domain-friendly models** that can be mapped to UI models.
- Abstracting the data source origin (local or remote) using the **Repository Pattern**.

## 🧠 Why It Exists

This module was created to:
- **Separate responsibilities** between storage and networking concerns.
- Act as the **single source of truth** for fetching, storing, and exposing data.
- Provide clean, testable data access logic without leaking implementation details (such as Retrofit or Room) to consumers.

## 🔗 Dependencies

- **Implements** the `storage` module to persist data.
- Uses **Retrofit** for networking.
- Exposes results through **Repository classes**, keeping consumers decoupled from data origins.

## 🌍 External APIs Used

- **[Ualá Gist](https://gist.githubusercontent.com/hernan-uala/dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/cities.json)**  
  → Source of the full cities list to populate the DB.

- **[OpenWeatherMap](https://api.openweathermap.org/)**  
  → Used to fetch detailed weather data for cities by the Coordinates.
  → Requires an API key for access.

- **[Picsum Photos](https://picsum.photos/)**  
  → Provides random images.
  → The images are not stored locally, but fetched on demand to avoid unnecessary storage usage.
  → The images are used to enhance the UI experience by providing random photos for cities.

## 🏗 Architecture

- Fetches data from remote services.
- Saves remote data into the local database using `storage` (just the Cities information).
- Returns domain-level models to be mapped into UI-friendly models.
- Consumers do **not need to know** whether data came from a network call or local cache.

## 📐 Design Principles

- **Repository Pattern**: Exposes classes for data access.
- **Single Responsibility**: This module handles data orchestration, not presentation or UI logic.
- **Modularization**: Keeps networking and storage encapsulated.

## ✅ Usage

1. Include this module as a dependency in the feature or UI layer.
2. Use it just tru Koin Injection
   1. Inject the `CitiesRepository` class.
   2. Inject the `CityWeatherRepository` class.
3. Receive ready-to-map domain models from one single entry point.