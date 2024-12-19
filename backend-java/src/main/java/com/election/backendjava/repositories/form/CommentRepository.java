package com.election.backendjava.repositories.form;

import com.election.backendjava.models.form.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
