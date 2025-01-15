package com.election.backendjava.repositories.form;

import com.election.backendjava.models.form.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByTopicTopicIdOrderByCreatedAtDesc(Long topicId, Pageable pageable);
}
