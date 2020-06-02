package com.ibrahimtugrul.cartservice.application.manager;

import com.ibrahimtugrul.cartservice.application.converter.ProductCreateRequestToVoConverter;
import com.ibrahimtugrul.cartservice.application.model.request.ProductCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.domain.service.ProductService;
import com.ibrahimtugrul.cartservice.domain.vo.ProductCreateVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductManager {

    private final ProductService productService;
    private final ProductCreateRequestToVoConverter productCreateRequestToVoConverter;

    public IdResponse create(final ProductCreateRequest productCreateRequest) {
        final ProductCreateVo productCreateVo = productCreateRequestToVoConverter.convert(productCreateRequest);
        final long productId = productService.create(productCreateVo);
        return IdResponse.builder().id(productId).build();
    }
}