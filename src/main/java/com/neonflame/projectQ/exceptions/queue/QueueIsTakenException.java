package com.neonflame.projectQ.exceptions.queue;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Place is already taken")
public class QueueIsTakenException extends RuntimeException{
     public QueueIsTakenException(String message) {
        super(message);
    }

    public QueueIsTakenException(int index) {
         super("Place with index " + index + " is already taken");
    }
}
