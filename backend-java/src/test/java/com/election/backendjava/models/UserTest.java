package com.election.backendjava.models;

import com.election.backendjava.models.form.Comment;
import com.election.backendjava.models.user.User;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserInitializationAndDefaults() {
        User user = new User("testuser", "test@example.com", "Test User", "Region1", "securePassword123");

        assertAll(
                () -> assertEquals("testuser", user.getUsername()),
                () -> assertEquals("test@example.com", user.getEmail()),
                () -> assertEquals("Test User", user.getName()),
                () -> assertEquals("Region1", user.getRegion()),
                () -> assertEquals("securePassword123", user.getPassword()),
                () -> assertFalse(user.getBanned(), "Default banned status should be false"),
                () -> assertEquals(0, user.getBanCount(), "Default ban count should be 0"),
                () -> assertNotNull(user.getComments(), "Comments list should not be null"),
                () -> assertTrue(user.getComments().isEmpty(), "Comments list should be empty by default")
        );
    }

    @Test
    void testAddingComment() {
        User user = new User("testuser", "test@example.com", "Test User", "Region1", "securePassword123");
        Comment comment = new Comment(1, "Test Comment", "Test Title", null, user, LocalDateTime.now());

        user.getComments().add(comment);

        assertAll(
                () -> assertEquals(1, user.getComments().size()),
                () -> assertEquals("Test Comment", user.getComments().get(0).getCommentText()),
                () -> assertEquals("Test Title", user.getComments().get(0).getCommentTitle()),
                () -> assertEquals(user, user.getComments().get(0).getUser())
        );
    }
}
