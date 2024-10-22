package com.election.backendjava.repositories.form;

import com.election.backendjava.models.form.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    public Topic findByName(String name);
}
