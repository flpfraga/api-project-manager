package com.fraga.projectManager.controller.defaultController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface DefaultController {

    default <T> ResponseEntity<DefaultResponse<T>> retornarResponse(final HttpStatus httpStatus,
                                                                    final T response) {
        return ResponseEntity.status(httpStatus.value()).body(new DefaultResponse<>(httpStatus.value(), response));
    }

    /*
     * HTTP 200
     */
    default <T> ResponseEntity<DefaultResponse<T>> success(final T response) {
        return retornarResponse(HttpStatus.OK, response);
    }
}
