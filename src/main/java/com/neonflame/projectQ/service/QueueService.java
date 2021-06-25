package com.neonflame.projectQ.service;

import com.neonflame.projectQ.exceptions.queue.QueueAccessDeniedException;
import com.neonflame.projectQ.exceptions.queue.QueueIndexErrorException;
import com.neonflame.projectQ.exceptions.queue.QueueIsTakenException;
import com.neonflame.projectQ.exceptions.queue.QueueNotFoundException;
import com.neonflame.projectQ.exceptions.user.UserNotFoundException;
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
    private final UserService userService;

    public QueueService(QueueRepo queueRepo, UserService userService) {
        this.queueRepo = queueRepo;
        this.userService = userService;
    }

    /**
     * Finds queue by id;
     * @param id queue id
     * @throws QueueNotFoundException if queue with id was not found
     * @return queue
     */
    public Queue findQueue(Long id) {
        Queue queue = queueRepo.findById(id).orElse(null);
        if (queue == null)
            throw new QueueNotFoundException(id);
        return queue;
    }

    /**
     * Finds all queues
     * @return all queues
     */
    public List<Queue> findAllQueues() {
        List<Queue> queues = queueRepo.findAll();
        if (queues.isEmpty())
            throw new QueueNotFoundException("No any queues");
        return queueRepo.findAll();
    }

    /**
     * Queue create
     * @param username user's username
     * @param size queue size should be in the range from 5 to 128
     * @return created Queue
     */
    public Queue create(String username, int size) {
        User user = userService.findUser(username);

        if (size < 5 || size > 128)
            throw new QueueIndexErrorException("Bad queue size value");

        Queue queue = new Queue();
        queue.setCreator(user);
        queue.setSize(size);

        return queueRepo.save(queue);
    }

    /**
     * Queue delete
     * @param queueId
     * @param username
     * @throws QueueNotFoundException if queue with queueId was not found
     * @throws QueueAccessDeniedException if user do not have permission to delete the queue
     */
    public void delete(Long queueId, String username) {
        Queue queue = findQueue(queueId);
        if (!queue.getCreator().getUsername().equals(username))
            throw new QueueAccessDeniedException("User + " + username + "can not delete the queue");

        queueRepo.delete(queue);
    }

    /**
     * Queue activate or deactivate if queue has been activated
     * @param queueId
     * @param username
     * @throws QueueNotFoundException if queue with queueId was not found
     * @throws QueueAccessDeniedException if user do not have permission to delete the queue
     * @return true if queue was activated
     * @return false if queue was deactivated
     */
    public boolean activate(Long queueId, String username) {
        Queue queue = findQueue(queueId);
        if (!queue.getCreator().getUsername().equals(username))
            throw new QueueAccessDeniedException("User + " + username + "can not control queue active status");

        boolean result;
        if (queue.isActive()) {
            queue.setActive(false);
            result = false;
        }
        else {
            queue.setActive(true);
            result = true;
        }
        queueRepo.save(queue);
        return result;
    }

    /**
     * Takes place in the queue. If user takes the same place then method vacates the place
     * @param username user's username String value
     * @param queueId queue id
     * @param index place in the queue
     * @throws QueueNotFoundException if queue with queueId was not found
     * @throws QueueIsTakenException if place in the queue has been taken by another user
     * @return true if place was taken successful
     * @return false if place was vacated
     */
    public boolean takePlace(String username, Long queueId, int index) {
        index--;

        Queue queue = findQueue(queueId);

        User user = userService.findUser(username);

        if (index < 0 || index >= queue.getSize())
            throw new QueueIsTakenException("Bad index");

        if (!queue.isFree(index) && queue.findPlaceByUser(user).getIndex() != index)
            throw new QueueIsTakenException(index);

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
