package com.neonflame.projectQ.service;

import com.neonflame.projectQ.excrptions.queue.QueueIsTakenException;
import com.neonflame.projectQ.excrptions.queue.QueueNotFoundException;
import com.neonflame.projectQ.excrptions.user.UserNotFoundException;
import com.neonflame.projectQ.model.Place;
import com.neonflame.projectQ.model.Queue;
import com.neonflame.projectQ.model.User;
import com.neonflame.projectQ.repository.QueueRepo;
import com.neonflame.projectQ.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueueService {

    private final QueueRepo queueRepo;
    private final UserRepo userRepo;

    public QueueService(QueueRepo queueRepo, UserRepo userRepo) {
        this.queueRepo = queueRepo;
        this.userRepo = userRepo;
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
            throw new UserNotFoundException("User was not found");

        Queue queue = queueRepo.findById(queueId).orElse(null);
        if (queue == null)
            throw new QueueNotFoundException("Queue with id " + queueId + " not found");

        if (index < 0 || index > queue.getSize())
            throw new QueueIsTakenException("Bad index");

        if (!queue.isFree(index) && queue.findPlaceByUser(user).getIndex() != index)
            throw new QueueIsTakenException("Place with index " + index + " is already taken");

        boolean result = true;
        Place place = queue.findPlaceByUser(user);
        if (place != null) {
            queue.getPlaces().remove(place);
            // User click another place     --> retake place
            // User click the same place    --> vacate place
            if (place.getIndex() != index)
                place.setIndex(index);
            else
                result = false;
        }
        else
            place = new Place(user, index);

        if (result)
            queue.getPlaces().add(place);

        queueRepo.save(queue);

        return result;
    }
}
