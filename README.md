# movie-caching-engine

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
├── main/java/com/example/
│   ├── Main.java                        # Driver class (STDIN command processing)
│   ├── cache/
│   │   ├── LFUCache.java                # O(1) LFU Cache implementation
│   │   └── LRUCache.java                # LRU Cache implementation
│   ├── exception/
│   │   ├── DuplicateEntryException.java # Exception for duplicate IDs
│   │   └── InvalidInputException.java   # Exception for bad queries
│   ├── model/
│   │   ├── Movie.java                   # Movie data model
│   │   └── User.java                    # User data model
│   └── service/
│       └── ZipReelSystem.java           # Core orchestration and search logic
└── test/java/com/zipreel/
    └── service/
    └── ZipReelSystemTest.java       # Unit tests for critical components

```

## 💻 Terminal Commands

Once the application is running, you can interact with it using the following commands:

| Command | Format | Example |
| :--- | :--- | :--- |
| **Add Movie** | `ADD_MOVIE <id> <title> <genre> <year> <rating>` | `ADD_MOVIE 1 "Inception" "Sci-Fi" 2010 9.5` |
| **Add User** | `ADD_USER <id> <name> <preferred_genre>` | `ADD_USER 1 "John" "Action"` |
| **Search** | `SEARCH <user_id> <search_type> <search_value>` | `SEARCH 1 GENRE "Action"` |
| **Multi-Search** | `SEARCH_MULTI <user_id> <genre> <year> <min_rating>` | `SEARCH_MULTI 1 "Action" 2020 8.0` |
| **View Stats** | `VIEW_CACHE_STATS` | `VIEW_CACHE_STATS` |
| **Clear Cache** | `CLEAR_CACHE <cache_level>` (L1 or L2) | `CLEAR_CACHE L1` |

## 🧪 Testing

The project includes unit tests located in `src/test/java/com.example/SystemTest.java`. 

### How to Run Tests
1. Ensure your `pom.xml` includes the JUnit 5 dependency.
2. In IntelliJ, navigate to the test file.
3. Click the green play button (▶️) next to the class name or individual test methods to execute them.

### Key Test Cases Implemented:
* `testSuccessfulAddition`: Verifies correct registration of movies and users.
* `testDuplicateMovieThrowsException`: Ensures the system handles duplicate IDs.
* `testSearchWithInvalidUserThrowsException`: Validates that searches for non-existent users trigger `InvalidInputException`.

## 🏃 How to Run

1. **Compile:** `mvn clean compile`
2. **Run:** `mvn exec:java -Dexec.mainClass="com.example.Main"`
3. **Interact:** Type the commands listed above directly into your terminal.