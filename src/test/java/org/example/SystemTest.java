package org.example;

import org.example.exception.DuplicateEntryException;
import org.example.exception.InvalidInputException;
import org.example.service.ZipReelSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SystemTest {

    private ZipReelSystem system;

    @BeforeEach
    public void setUp() {
        system = new ZipReelSystem();
    }

    @Test
    public void testSuccessfulAddition() {
        assertDoesNotThrow(() -> {
            system.addMovie("1", "Inception", "Sci-Fi", 2010, 9.5);
            system.addUser("101", "John", "Action");
        });
    }

    @Test
    public void testDuplicateMovieThrowsException() {
        system.addMovie("1", "Inception", "Sci-Fi", 2010, 9.5);
        assertThrows(DuplicateEntryException.class, () ->
                system.addMovie("1", "Duplicate Title", "Action", 2022, 5.0)
        );
    }

    @Test
    public void testSearchWithInvalidUserThrowsException() {
        assertThrows(InvalidInputException.class, () ->
                system.search("NON_EXISTENT_USER", "GENRE", "Sci-Fi")
        );
    }

    @Test
    public void testSearchWithInvalidTypeThrowsException() {
        system.addMovie("1", "Inception", "Sci-Fi", 2010, 9.5);
        system.addUser("101", "John", "Action");

        // Assuming your code throws InvalidInputException for unsupported types
        assertThrows(InvalidInputException.class, () ->
                system.search("101", "INVALID_TYPE", "Value")
        );
    }
}