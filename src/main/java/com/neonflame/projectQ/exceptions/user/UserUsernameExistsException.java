package com.neonflame.projectQ.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User with this username already exists")
public class UserUsernameExistsException extends RuntimeException {
    public UserUsernameExistsException(String message) {
        super(message);
    }
}
