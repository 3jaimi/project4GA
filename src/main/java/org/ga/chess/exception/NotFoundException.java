package org.ga.chess.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not found")
    public class NotFoundException extends RuntimeException{
        public NotFoundException(String message) {
            super(String.format("Couldn't find a %s",message));
        }
    }

