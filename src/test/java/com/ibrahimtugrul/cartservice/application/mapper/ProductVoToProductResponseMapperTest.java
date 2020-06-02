package com.ibrahimtugrul.cartservice.application.mapper;

import com.ibrahimtugrul.cartservice.application.model.response.ProductResponse;
import com.ibrahimtugrul.cartservice.domain.vo.ProductVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductVoToProductResponseMapperTest {

    private ProductVoToProductResponseMapper productVoToProductResponseMapper;

    @BeforeEach
    public void setup() {
        this.productVoToProductResponseMapper = new ProductVoToProductResponseMapper();
    }

    @Test
    public void should_map() {
        // given
        final ProductVo productVo = ProductVo.builder()
                .categoryId(1L)
                .id(2L)
                .price(15.0)
                .title("product")
                .build();

        // when
        final ProductResponse productResponse = productVoToProductResponseMapper.apply(productVo);

        // then
        assertThat(productResponse).isNotNull();
        assertThat(productResponse.getCategoryId()).isEqualTo(productVo.getCategoryId());
        assertThat(productResponse.getId()).isEqualTo(productVo.getId());
        assertThat(productResponse.getPrice().doubleValue()).isEqualTo(productVo.getPrice());
        assertThat(productResponse.getTitle()).isEqualTo(productVo.getTitle());
    }
}