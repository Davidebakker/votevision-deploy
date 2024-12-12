package com.election.backendjava;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddComment() throws Exception {
        String commentRequestJson = """
            {
                "title": "Test Title",
                "commentText": "This is a test comment."
            }
        """;

        mockMvc.perform(post("/api/chat/topic/1/comment/post")
                        .contentType(MediaType.APPLICATION_JSON) // Zorg ervoor dat MediaType correct is
                        .content(commentRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentTitle").value("Test Title"))
                .andExpect(jsonPath("$.commentText").value("This is a test comment."));
    }
}
