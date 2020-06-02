package com.ibrahimtugrul.cartservice.application.controller;

import com.ibrahimtugrul.cartservice.application.model.request.CartCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;

public interface CartController {
    IdResponse create(final CartCreateRequest cartCreateRequest);
}