package com.ibrahimtugrul.cartservice.domain.converter;

import com.ibrahimtugrul.cartservice.domain.entity.Product;
import com.ibrahimtugrul.cartservice.domain.vo.ProductVo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ProductToVoConverter implements Function<Product, ProductVo>, Converter<Product, ProductVo> {
    @Override
    public ProductVo apply(final Product product) {
        return ProductVo.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .categoryId(product.getCategoryId())
                .build();
    }

    @Override
    public ProductVo convert(final Product product) {
        return apply(product);
    }
}