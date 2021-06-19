package com.neonflame.projectQ.service;

import com.neonflame.projectQ.model.Queue;
import com.neonflame.projectQ.model.User;
import com.neonflame.projectQ.repository.QueueRepo;
import org.springframework.stereotype.Service;

@Service
public class QueueService {

    private final QueueRepo queueRepo;

    public QueueService(QueueRepo queueRepo) {
        this.queueRepo = queueRepo;
    }

    public Queue create(User user, int size) {
        if (size < 5 || size > 128)
            throw new IllegalArgumentException("Bad queue size value");

        Queue queue = new Queue();
        queue.setCreator(user);
        queue.setSize(size);

        return queueRepo.save(queue);
    }

    public void takePlace(User user, int index) {

    }
}
