package com.fraga.projectManager.exception;

public class IlegalArgumentException extends RuntimeException {
    public IlegalArgumentException(String message) {
        super(message);
    }

    public IlegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
