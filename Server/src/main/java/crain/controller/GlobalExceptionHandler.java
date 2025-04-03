package crain.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected String handleIllegalArgumentException(IllegalArgumentException e) {
        log.debug(e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected String handleUnmatchedExceptions(Exception e) {
        log.debug("Unexpected Failure: {}", e.getMessage(), e);
        return "An unexpected Exception has occurred on the Server. Contact Crain if this persists";
    }
}
