package org.example;

import org.example.service.ZipReelSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        ZipReelSystem system = new ZipReelSystem();
        Scanner scanner = new Scanner(System.in);

        System.out.println("ZipReel System Started. Awaiting commands (Type EXIT to quit)...");

        // Read from STDIN continuously
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            if (line.equalsIgnoreCase("EXIT")) break;

            try {
                processCommand(system, line);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void processCommand(ZipReelSystem system, String line) {
        // Regex to split by spaces, but group items inside double quotes together
        List<String> tokens = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(line);
        while (m.find()) {
            // Remove the quotes from the parsed tokens before passing to the system
            tokens.add(m.group(1).replace("\"", ""));
        }

        if (tokens.isEmpty()) return;

        String command = tokens.get(0).toUpperCase();

        switch (command) {
            case "ADD_MOVIE":
                // Format: ADD_MOVIE <id> <title> <genre> <year> <rating>
                if (tokens.size() < 6) throw new IllegalArgumentException("Invalid ADD_MOVIE arguments");
                system.addMovie(
                        tokens.get(1),
                        tokens.get(2),
                        tokens.get(3),
                        Integer.parseInt(tokens.get(4)),
                        Double.parseDouble(tokens.get(5))
                );
                break;

            case "ADD_USER":
                // Format: ADD_USER <id> <name> <preferred_genre>
                if (tokens.size() < 4) throw new IllegalArgumentException("Invalid ADD_USER arguments");
                system.addUser(tokens.get(1), tokens.get(2), tokens.get(3));
                break;

            case "SEARCH":
                // Format: SEARCH <user_id> <search_type> <search_value>
                if (tokens.size() < 4) throw new IllegalArgumentException("Invalid SEARCH arguments");
                system.search(tokens.get(1), tokens.get(2), tokens.get(3));
                break;

            case "SEARCH_MULTI":
                // Format: SEARCH_MULTI <user_id> <genre> <year> <min_rating>
                if (tokens.size() < 5) throw new IllegalArgumentException("Invalid SEARCH_MULTI arguments");
                system.searchMulti(
                        tokens.get(1),
                        tokens.get(2),
                        Integer.parseInt(tokens.get(3)),
                        Double.parseDouble(tokens.get(4))
                );
                break;

            case "VIEW_CACHE_STATS":
                // Format: VIEW_CACHE_STATS
                system.viewCacheStats();
                break;

            case "CLEAR_CACHE":
                // Format: CLEAR_CACHE <cache_level>
                if (tokens.size() < 2) throw new IllegalArgumentException("Invalid CLEAR_CACHE arguments");
                system.clearCache(tokens.get(1));
                break;

            default:
                System.out.println("Unknown command: " + command);
        }
    }
}