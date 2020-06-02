package com.ibrahimtugrul.cartservice.application.converter;

import com.ibrahimtugrul.cartservice.application.model.request.ProductCreateRequest;
import com.ibrahimtugrul.cartservice.domain.vo.ProductCreateVo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ProductCreateRequestToVoConverter implements Function<ProductCreateRequest, ProductCreateVo>, Converter<ProductCreateRequest, ProductCreateVo> {
    @Override
    public ProductCreateVo apply(final ProductCreateRequest productCreateRequest) {
        return ProductCreateVo.builder()
                .categoryId(productCreateRequest.getCategoryId())
                .price(Double.valueOf(productCreateRequest.getPrice().toString()))
                .title(productCreateRequest.getTitle())
                .build();
    }

    @Override
    public ProductCreateVo convert(final ProductCreateRequest productCreateRequest) {
        return apply(productCreateRequest);
    }
}