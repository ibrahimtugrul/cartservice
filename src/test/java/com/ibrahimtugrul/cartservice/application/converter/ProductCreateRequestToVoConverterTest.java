package com.ibrahimtugrul.cartservice.application.converter;

import com.ibrahimtugrul.cartservice.application.model.request.ProductCreateRequest;
import com.ibrahimtugrul.cartservice.domain.vo.ProductCreateVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductCreateRequestToVoConverterTest {

    private ProductCreateRequestToVoConverter productCreateRequestToVoConverter;

    @BeforeEach
    public void setup() {
        this.productCreateRequestToVoConverter = new ProductCreateRequestToVoConverter();
    }

    @Test
    public void should_convert() {
        // given
        final ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
                .price("15.0")
                .title("product")
                .categoryId(1L)
                .build();

        // when
        final ProductCreateVo productCreateVo = productCreateRequestToVoConverter.convert(productCreateRequest);

        // then
        assertThat(productCreateVo).isNotNull();
        assertThat(productCreateVo.getTitle()).isEqualTo(productCreateRequest.getTitle());
        assertThat(productCreateVo.getCategoryId()).isEqualTo(productCreateRequest.getCategoryId());
        assertThat(productCreateVo.getPrice()).isEqualTo(Double.valueOf(productCreateRequest.getPrice().toString()));
    }
}