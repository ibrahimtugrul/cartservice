package com.ibrahimtugrul.cartservice.domain.converter;

import com.ibrahimtugrul.cartservice.domain.entity.Product;
import com.ibrahimtugrul.cartservice.domain.vo.ProductVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductToVoConverterTest {
    private ProductToVoConverter productToVoConverter;

    @BeforeEach
    public void setup() {
        this.productToVoConverter = new ProductToVoConverter();
    }

    @Test
    public void should_convert() {
        // given
        final Product product = Product.builder()
                .price(15.0)
                .categoryId(1L)
                .title("product")
                .id(1L)
                .build();

        // when
        final ProductVo productVo = productToVoConverter.convert(product);

        // then
        assertThat(productVo).isNotNull();
        assertThat(productVo.getCategoryId()).isEqualTo(product.getCategoryId());
        assertThat(productVo.getId()).isEqualTo(product.getId());
        assertThat(productVo.getTitle()).isEqualTo(product.getTitle());
        assertThat(productVo.getPrice()).isEqualTo(product.getPrice());
    }
}