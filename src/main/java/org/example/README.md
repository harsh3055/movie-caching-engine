# ZipReel - Movie Content Management System

A Java-based in-memory movie content management system featuring a multi-level caching mechanism to optimize search operations and provide fast retrieval times.

## 🚀 Features

* **Multi-Level Caching System:**
    * **L1 Cache:** User-specific cache using LRU (Least Recently Used) policy. Maximum 5 entries per user.
    * **L2 Cache:** Global popular searches cache using LFU (Least Frequently Used) policy with O(1) eviction time complexity. Maximum 20 entries.
    * **Primary Store:** Complete movie and user database kept entirely in-memory using hash maps.
* **Movie & User Management:** Add new movies with attributes like genre, release year, and rating, and register new users with genre preferences.
* **Advanced Search Capabilities:** * Search by specific attributes (Title, Genre, Year).
    * Multi-filter search (Genre + Year + Min Rating combination).
    * Fast cache checks in the order: `L1 -> L2 -> Primary Store`.
* **Cache Management:** View cache hits statistics and clear specific cache levels.
* **Robust Error Handling:** Custom exceptions for duplicate entries and invalid inputs.

## 📂 Project Structure

```text
src/
└── com/
    └── zipreel/
        ├── Main.java                        # Driver class with demo operations
        ├── cache/
        │   ├── LFUCache.java                # O(1) LFU Cache implementation
        │   └── LRUCache.java                # LRU Cache implementation
        ├── exception/
        │   ├── DuplicateEntryException.java # Exception for duplicate IDs
        │   └── InvalidInputException.java   # Exception for bad queries
        ├── model/
        │   ├── Movie.java                   # Movie data model
        │   └── User.java                    # User data model
        └── service/
            └── ZipReelSystem.java           # Core orchestration and search logic