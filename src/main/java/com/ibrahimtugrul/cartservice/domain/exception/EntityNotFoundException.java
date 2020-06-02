package com.ibrahimtugrul.cartservice.domain.exception;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(final String entity) {
        super("domain.entity.notFound", entity);
    }
}