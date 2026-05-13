package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    List<Feedback> findAllByOrderByCreatedAtDesc();
}
