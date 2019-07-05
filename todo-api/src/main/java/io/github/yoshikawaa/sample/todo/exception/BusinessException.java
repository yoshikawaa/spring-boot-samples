package io.github.yoshikawaa.sample.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BusinessException extends ResponseStatusException {

    private static final long serialVersionUID = 1L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
	
    public BusinessException(String reason, Throwable cause) {
        super(HTTP_STATUS, reason, cause);
    }

    public BusinessException(String reason) {
        super(HTTP_STATUS, reason);
    }

    public BusinessException() {
        super(HTTP_STATUS);
    }

}
