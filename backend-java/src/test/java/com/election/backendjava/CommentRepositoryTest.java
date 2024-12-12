package com.election.backendjava;

import com.election.backendjava.models.form.Comment;
import com.election.backendjava.repositories.form.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void testSaveAndFindComment() {
        Comment comment = new Comment();
        comment.setCommentTitle("Test Title");
        comment.setCommentText("Test Text");
        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        Optional<Comment> foundComment = commentRepository.findById(savedComment.getCommentId());
        assertTrue(foundComment.isPresent());
        assertEquals("Test Title", foundComment.get().getCommentTitle());
    }
}