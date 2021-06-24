package com.neonflame.projectQ.exceptions.queue;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Queue was not found")
public class QueueNotFoundException extends RuntimeException {
    public QueueNotFoundException(String message) {
        super(message);
    }

    public QueueNotFoundException(Long id) {
        super("Queue with id " + id + " not found");
    }
}
