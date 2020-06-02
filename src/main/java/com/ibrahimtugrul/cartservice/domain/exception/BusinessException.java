package com.ibrahimtugrul.cartservice.domain.exception;

public abstract class BusinessException extends RuntimeException {

    private final Object[] arguments;

    public BusinessException(final String message, Object... arguments) {
        super(message);
        this.arguments = arguments;
    }

    public Object[] getArguments() {
        return arguments;
    }
}