package com.ibrahimtugrul.cartservice.application.mapper;

import com.ibrahimtugrul.cartservice.application.model.response.ProductResponse;
import com.ibrahimtugrul.cartservice.domain.vo.ProductVo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.function.Function;

@Component
public class ProductVoToProductResponseMapper implements Function<ProductVo, ProductResponse> {
    @Override
    public ProductResponse apply(final ProductVo productVo) {
        return ProductResponse.builder()
                .title(productVo.getTitle())
                .price(String.valueOf(productVo.getPrice()))
                .id(String.valueOf(productVo.getId()))
                .categoryId(String.valueOf(productVo.getCategoryId()))
                .build();
    }
}