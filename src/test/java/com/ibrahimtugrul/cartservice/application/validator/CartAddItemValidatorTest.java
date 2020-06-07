package com.ibrahimtugrul.cartservice.application.validator;

import com.ibrahimtugrul.cartservice.application.exception.RequestValidationException;
import com.ibrahimtugrul.cartservice.application.model.request.CartAddItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class CartAddItemValidatorTest {

    private CartAddItemValidator cartAddItemValidator;

    private static final String ERR_INVALID_PRODUCT_ID = "cart.validation.required.productId.numeric";
    private static final String ERR_INVALID_PRODUCT_ID_BELOW_ONE = "cart.validation.required.productId.numeric.valid";
    private static final String ERR_INVALID_QUANTITY = "cart.validation.required.quantity.numeric";
    private static final String ERR_INVALID_QUANTITY_BELOW_ONE = "cart.validation.required.quantity.numeric";

    @BeforeEach
    public void setup() {
        this.cartAddItemValidator = new CartAddItemValidator();
    }

    @Test
    public void should_throw_exception_when_product_id_is_not_numeric() {
        // given
        final CartAddItemRequest cartAddItemRequest = CartAddItemRequest.builder()
                .productId("A")
                .quantity("1")
                .build();

        // when
        final Throwable throwable = catchThrowable(()->{cartAddItemValidator.validate(cartAddItemRequest);});

        // then
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(RequestValidationException.class);
        final RequestValidationException requestValidationException = (RequestValidationException) throwable;
        assertThat(requestValidationException.getLocalizedMessage().contains(ERR_INVALID_PRODUCT_ID)).isTrue();
    }

    @Test
    public void should_throw_exception_when_product_id_is_numeric_with_zero() {
        // given
        final CartAddItemRequest cartAddItemRequest = CartAddItemRequest.builder()
                .productId("0")
                .quantity("1")
                .build();

        // when
        final Throwable throwable = catchThrowable(()->{cartAddItemValidator.validate(cartAddItemRequest);});

        // then
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(RequestValidationException.class);
        final RequestValidationException requestValidationException = (RequestValidationException) throwable;
        assertThat(requestValidationException.getLocalizedMessage().contains(ERR_INVALID_PRODUCT_ID_BELOW_ONE)).isTrue();
    }

    @Test
    public void should_throw_exception_when_quantity_is_not_numeric() {
        // given
        final CartAddItemRequest cartAddItemRequest = CartAddItemRequest.builder()
                .productId("1")
                .quantity("A")
                .build();

        // when
        final Throwable throwable = catchThrowable(()->{cartAddItemValidator.validate(cartAddItemRequest);});

        // then
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(RequestValidationException.class);
        final RequestValidationException requestValidationException = (RequestValidationException) throwable;
        assertThat(requestValidationException.getLocalizedMessage().contains(ERR_INVALID_QUANTITY)).isTrue();
    }

    @Test
    public void should_throw_exception_when_quantity_is_numeric_with_zero() {
        // given
        final CartAddItemRequest cartAddItemRequest = CartAddItemRequest.builder()
                .productId("1")
                .quantity("0")
                .build();

        // when
        final Throwable throwable = catchThrowable(()->{cartAddItemValidator.validate(cartAddItemRequest);});

        // then
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(RequestValidationException.class);
        final RequestValidationException requestValidationException = (RequestValidationException) throwable;
        assertThat(requestValidationException.getLocalizedMessage().contains(ERR_INVALID_QUANTITY_BELOW_ONE)).isTrue();
    }
}