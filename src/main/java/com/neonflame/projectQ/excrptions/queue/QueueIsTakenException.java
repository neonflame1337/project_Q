package com.neonflame.projectQ.excrptions.queue;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Place is already taken")
public class QueueIsTakenException extends RuntimeException{
     public QueueIsTakenException(String message) {
        super(message);
    }
}
