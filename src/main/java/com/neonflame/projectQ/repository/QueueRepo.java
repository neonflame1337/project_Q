package com.neonflame.projectQ.repository;

import com.neonflame.projectQ.model.Queue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueRepo extends JpaRepository<Queue, Long> {
}
