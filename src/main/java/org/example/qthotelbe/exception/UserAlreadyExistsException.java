package org.example.qthotelbe.exception;

import org.example.qthotelbe.model.User;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
