package com.election.backendjava.models;

import com.election.backendjava.models.form.Comment;
import com.election.backendjava.models.user.User;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserConstructor() {
        User user = new User("testuser", "test@example.com", "Test User", "Region1", "securePassword123");

        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getName());
        assertEquals("Region1", user.getRegion());
        assertEquals("securePassword123", user.getPassword());
        assertFalse(user.getBanned(), "Default banned status should be false");
        assertEquals(0, user.getBanCount(), "Default ban count should be 0");
        assertNotNull(user.getComments(), "Comments list should not be null");
        assertTrue(user.getComments().isEmpty(), "Comments list should be empty by default");
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();
        user.setUsername("newuser");
        user.setEmail("newuser@example.com");
        user.setName("New User");
        user.setRegion("NewRegion");
        user.setPassword("newSecurePassword");
        user.setBanned(true);
        user.setBanCount(2);
        user.setBanExpiration(LocalDateTime.now().plusDays(1));

        assertEquals("newuser", user.getUsername());
        assertEquals("newuser@example.com", user.getEmail());
        assertEquals("New User", user.getName());
        assertEquals("NewRegion", user.getRegion());
        assertEquals("newSecurePassword", user.getPassword());
        assertTrue(user.getBanned(), "Banned status should be true");
        assertEquals(2, user.getBanCount(), "Ban count should be 2");
        assertNotNull(user.getBanExpiration(), "Ban expiration should not be null");
    }

    @Test
    void testAddComment() {
        User user = new User("testuser", "test@example.com", "Test User", "Region1", "securePassword123");
        Comment comment = new Comment();
        comment.setCommentId(1);
        comment.setCommentText("This is a test comment");
        comment.setCommentTitle("Test Title");
        comment.setCreatedAt(LocalDateTime.now());

        user.getComments().add(comment);

        assertFalse(user.getComments().isEmpty(), "Comments list should not be empty after adding a comment");
        assertEquals(1, user.getComments().size(), "Comments list size should be 1 after adding a comment");
        assertEquals("This is a test comment", user.getComments().get(0).getCommentText());
        assertEquals("Test Title", user.getComments().get(0).getCommentTitle());
        assertNotNull(user.getComments().get(0).getCreatedAt(), "CreatedAt should not be null");
    }

    @Test
    void testDefaultValues() {
        User user = new User();

        assertFalse(user.getBanned(), "Default banned status should be false");
        assertEquals(0, user.getBanCount(), "Default ban count should be 0");
        assertNull(user.getBanExpiration(), "Default ban expiration should be null");
        assertNotNull(user.getComments(), "Default comments list should not be null");
        assertTrue(user.getComments().isEmpty(), "Default comments list should be empty");
    }
}


