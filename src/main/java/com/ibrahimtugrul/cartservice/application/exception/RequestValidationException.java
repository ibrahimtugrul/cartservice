package com.ibrahimtugrul.cartservice.application.exception;

public class RequestValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final Object[] arguments;

    public RequestValidationException(final String message) {
        super(message);
        arguments = new Object[0];
    }

    public Object[] getArguments() {
        return this.arguments;
    }
}