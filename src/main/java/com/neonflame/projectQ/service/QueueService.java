package com.neonflame.projectQ.service;

import com.neonflame.projectQ.model.Place;
import com.neonflame.projectQ.model.Queue;
import com.neonflame.projectQ.model.User;
import com.neonflame.projectQ.repository.PlaceRepo;
import com.neonflame.projectQ.repository.QueueRepo;
import com.neonflame.projectQ.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueueService {

    private final QueueRepo queueRepo;
    private final UserRepo userRepo;
    private final PlaceRepo placeRepo;

    public QueueService(QueueRepo queueRepo, UserRepo userRepo, PlaceRepo placeRepo) {
        this.queueRepo = queueRepo;
        this.userRepo = userRepo;
        this.placeRepo = placeRepo;
    }

    public Queue findQueue(Long id) {
        return queueRepo.findById(id).get();
    }

    public List<Queue> findAllQueues() {
        return queueRepo.findAll();
    }

    public Queue create(String username, int size) {
        User user = userRepo.findByUsername(username);
        if (user == null)
            throw new IllegalStateException("User was not found");

        if (size < 5 || size > 128)
            throw new IllegalArgumentException("Bad queue size value");

        Queue queue = new Queue();
        queue.setCreator(user);
        queue.setSize(size);

        return queueRepo.save(queue);
    }

    // return false if place was vacated
    // TODO documentation
    public boolean takePlace(String username, Long queueId, int index) {
        User user = userRepo.findByUsername(username);
        if (user == null)
            throw new IllegalStateException("User was not found");

        Queue queue = queueRepo.findById(queueId).orElse(null);
        if (queue == null)
            throw new IllegalStateException("Queue was not found");

        if (index < 0 || index > queue.getSize())
            throw new IllegalArgumentException("Bad index");

        Place place = placeRepo.findByQueueAndUser(queue, user);
        if (place != null) {
            queue.getPlaces().remove(place);
            // Free place if user takes(clicks) the same place
            if (place.getIndex() != index)
                return false;
        }

        queue.getPlaces().add(
                new Place(user, queue, index)
                //placeRepo.save(new Place(user, queue, index))
        );
        queueRepo.save(queue);
        return true;
    }
}
