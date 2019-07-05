package io.github.yoshikawaa.sample.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceNotFoundException extends ResponseStatusException {

	private static final long serialVersionUID = 1L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    public ResourceNotFoundException(String reason, Throwable cause) {
        super(HTTP_STATUS, reason, cause);
    }

    public ResourceNotFoundException(String reason) {
        super(HTTP_STATUS, reason);
    }

    public ResourceNotFoundException() {
        super(HTTP_STATUS);
    }

}
