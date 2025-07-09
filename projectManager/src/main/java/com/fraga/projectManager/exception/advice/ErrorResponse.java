package com.fraga.projectManager.exception.advice;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private Integer statusCode;

}
