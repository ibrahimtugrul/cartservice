package com.ibrahimtugrul.cartservice.application.validator;

import com.ibrahimtugrul.cartservice.application.exception.RequestValidationException;
import com.ibrahimtugrul.cartservice.application.model.request.CartAddItemRequest;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Component;

@Component
public class CartAddItemValidator {

    public void validate(final CartAddItemRequest cartAddItemRequest) {

        if(!NumberUtils.isNumber(cartAddItemRequest.getProductId())) {
            throw new RequestValidationException("cart.validation.required.productId.numeric");
        }
        final Long productId = Long.valueOf(cartAddItemRequest.getProductId());
        if(productId < 1) {
            throw new RequestValidationException("cart.validation.required.productId.numeric.valid");
        }

        if(!NumberUtils.isNumber(cartAddItemRequest.getQuantity())) {
            throw new RequestValidationException("cart.validation.required.quantity.numeric");
        }
        final Long quantity = Long.valueOf(cartAddItemRequest.getQuantity());
        if(quantity < 1) {
            throw new RequestValidationException("cart.validation.required.quantity.numeric.valid");
        }
    }
}