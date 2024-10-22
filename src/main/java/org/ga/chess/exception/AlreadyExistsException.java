package org.ga.chess.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "already exists")
public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String message) {
        super(String.format("%s already exists in the database",message));
    }
}
