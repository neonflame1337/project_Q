package com.neonflame.projectQ.excrptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "")
public class QueueIndexErrorException extends RuntimeException{

    public QueueIndexErrorException(String message) {
        super(message);
    }

}
