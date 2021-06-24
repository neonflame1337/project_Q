package com.neonflame.projectQ.exceptions.queue;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Access denied to user")
public class QueueAccessDeniedException extends RuntimeException{
    public QueueAccessDeniedException(String message) {
        super(message);
    }
}
