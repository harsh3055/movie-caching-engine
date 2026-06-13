package org.example.service;

import org.example.cache.LFUCache;
import org.example.cache.LRUCache;
import org.example.exception.DuplicateEntryException;
import org.example.exception.InvalidInputException;
import org.example.model.Movie;
import org.example.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ZipReelSystem {
    private final Map<String, Movie> movies = new HashMap<>();
    private final Map<String, User> users = new HashMap<>();

    private final Map<String, LRUCache<String, List<Movie>>> l1Caches = new HashMap<>();
    private final LFUCache<String, List<Movie>> l2Cache = new LFUCache<>(20);

    private int l1Hits = 0, l2Hits = 0, primaryHits = 0, totalSearches = 0;

    public void addMovie(String id, String title, String genre, int year, double rating) {
        if (movies.containsKey(id)) {
            throw new DuplicateEntryException("Movie ID already exists.");
        }
        movies.put(id, new Movie(id, title, genre, year, rating));
        System.out.println("Movie '" + title + "' added successfully");
    }

    public void addUser(String id, String name, String preferredGenre) {
        if (users.containsKey(id)) {
            throw new DuplicateEntryException("User ID already exists.");
        }
        users.put(id, new User(id, name, preferredGenre));
        l1Caches.put(id, new LRUCache<>(5));
        System.out.println("User '" + name + "' added successfully");
    }

    public void search(String userId, String searchType, String searchValue) {
        if (!users.containsKey(userId)) throw new InvalidInputException("Invalid User ID");
        String cacheKey = searchType.toUpperCase() + ":" + searchValue.toLowerCase();
        executeSearch(userId, cacheKey, movie -> matchesSearch(movie, searchType, searchValue));
    }

    public void searchMulti(String userId, String genre, int year, double minRating) {
        if (!users.containsKey(userId)) throw new InvalidInputException("Invalid User ID");
        String cacheKey = "MULTI:" + genre.toLowerCase() + ":" + year + ":" + minRating;
        executeSearch(userId, cacheKey, movie ->
                movie.getGenre().equalsIgnoreCase(genre) &&
                        movie.getYear() == year &&
                        movie.getRating() >= minRating
        );
    }

    private boolean matchesSearch(Movie movie, String type, String value) {
        switch (type.toUpperCase()) {
            case "TITLE": return movie.getTitle().equalsIgnoreCase(value);
            case "GENRE": return movie.getGenre().equalsIgnoreCase(value);
            case "YEAR": return String.valueOf(movie.getYear()).equals(value);
            default: throw new InvalidInputException("Invalid search type");
        }
    }

    private void executeSearch(String userId, String cacheKey, Predicate<Movie> filter) {
        totalSearches++;
        LRUCache<String, List<Movie>> userCache = l1Caches.get(userId);

        // 1. Check L1 Cache
        if (userCache.containsKey(cacheKey)) {
            l1Hits++;
            printResults(userCache.get(cacheKey), "L1");
            return;
        }

        // 2. Check L2 Cache
        List<Movie> l2Result = l2Cache.get(cacheKey);
        if (l2Result != null) {
            l2Hits++;
            userCache.put(cacheKey, l2Result); // Promote back to L1
            printResults(l2Result, "L2");
            return;
        }

        // 3. Search Primary Store
        primaryHits++;
        List<Movie> results = new ArrayList<>();
        for (Movie movie : movies.values()) {
            if (filter.test(movie)) {
                results.add(movie);
            }
        }

        // 4. Update Caches
        userCache.put(cacheKey, results);
        l2Cache.put(cacheKey, results);

        printResults(results, "Primary Store");
    }

    private void printResults(List<Movie> results, String cacheLevel) {
        if (results.isEmpty()) {
            System.out.println("No movies found. (Searched in " + cacheLevel + ")");
            return;
        }
        for (Movie m : results) {
            System.out.println(m.getTitle() + " (Found in " + cacheLevel + ")");
        }
    }

    public void viewCacheStats() {
        System.out.println("L1 Cache Hits: " + l1Hits);
        System.out.println("L2 Cache Hits: " + l2Hits);
        System.out.println("Primary Store Hits: " + primaryHits);
        System.out.println("Total Searches: " + totalSearches);
    }

    public void clearCache(String level) {
        if ("L1".equalsIgnoreCase(level)) {
            for (LRUCache<String, List<Movie>> cache : l1Caches.values()) {
                cache.clear();
            }
            System.out.println("L1 cache cleared successfully");
        } else if ("L2".equalsIgnoreCase(level)) {
            l2Cache.clear();
            System.out.println("L2 cache cleared successfully");
        } else {
            throw new InvalidInputException("Invalid cache level provided.");
        }
    }
}