package com.neonflame.projectQ.repository;

import com.neonflame.projectQ.model.Place;
import com.neonflame.projectQ.model.Queue;
import com.neonflame.projectQ.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepo extends JpaRepository <Place, Long>{
    public Place findByQueueAndUser (Queue queue, User user);
}
