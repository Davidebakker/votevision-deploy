package com.election.backendjava.repositories.form;

import com.election.backendjava.models.form.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
