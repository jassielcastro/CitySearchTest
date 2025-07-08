# 📦 storage Module

## 🧩 Module Type

**Data Module** – Dedicated to handling local storage using Room.

## 🎯 Purpose

This module was created to **isolate the storage logic** from the rest of the application, promoting
a clean architecture and separation of concerns. It focuses solely on managing local database
operations using **Room** and is not responsible for networking or business logic.

## 🧠 Why It Exists

- To **decouple** the database layer from other features or modules.
- To manage **Room database** configurations and interactions in one centralized place.
- To contain **local-only data models** that represent database tables.
- To provide a single source of truth for **persisting cities information** downloaded from external
  sources.

## 🔑 Key Considerations

- All data classes (e.g., Room Entities, DAOs) in this module are just for Database handling.
- The main data being persisted is the list of **Cities**, originally retrieved from:
  > [https://gist.githubusercontent.com/hernan-uala/](https://gist.githubusercontent.com/hernan-uala/dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/cities.json)
- This module is **not responsible for mapping** models to domain or UI layers — that should be done
  externally.
- It is implemented as an **Android module** (not a plain Kotlin module) to allow access to the **Android `Context`**, which is required for Room database initialization and testing.

## ✅ Usage

To use this module:

1. Add it as a project implementation in your `build.gradle`.
2. Access DAOs via the `CitiesDAO` injected instance by Koin.
3. Interact only through provided interfaces or Koin bindings if using DI.
4. Ensure that any data transformations or mappings are handled in the domain or presentation
   layers, not here.

## 📄 Queries

### 🔍 Get All Cities (Paged)

This module provides a query to retrieve **all cities** stored in the local database using **Room's Paging support**:

- ✅ Uses `PagingSource` to load results in a paginated way.
- 📉 Improves performance by **limiting memory usage** and **reducing load times**.
- 🔄 Enables efficient scrolling and lazy-loading behavior in UI.

## 🧪 Testing Strategy

Testing for this module is provided under **`androidTest/`**, not `test/`, due to the need for:
- **Android Context** to initialize and validate the Room database.
- Realistic integration testing for **DAO queries** and **schema validations**.

Using Android Instrumented Tests ensures that the Room database is correctly created, queried, and maintained.

```bash
    ./gradlew :storage:connectedDebugAndroidTest
```