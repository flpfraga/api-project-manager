package com.fraga.projectManager.exception;

public class HttpClientException extends RuntimeException {
    public HttpClientException(String message) {
        super(message);
    }

    public HttpClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
