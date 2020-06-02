package com.ibrahimtugrul.cartservice.application.manager;

import com.ibrahimtugrul.cartservice.application.converter.ProductCreateRequestToVoConverter;
import com.ibrahimtugrul.cartservice.application.mapper.ProductVoToProductResponseMapper;
import com.ibrahimtugrul.cartservice.application.model.request.ProductCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.application.model.response.ProductResponse;
import com.ibrahimtugrul.cartservice.domain.service.ProductService;
import com.ibrahimtugrul.cartservice.domain.vo.ProductCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductManager {

    private final ProductService productService;
    private final ProductCreateRequestToVoConverter productCreateRequestToVoConverter;
    private final ProductVoToProductResponseMapper productVoToProductResponseMapper;

    public IdResponse create(final ProductCreateRequest productCreateRequest) {
        final ProductCreateVo productCreateVo = productCreateRequestToVoConverter.convert(productCreateRequest);
        final long productId = productService.create(productCreateVo);
        return IdResponse.builder().id(productId).build();
    }

    public List<ProductResponse> listAll() {
        final List<ProductVo> productVoList = productService.listAll();
        return productVoList.stream().map(productVo -> productVoToProductResponseMapper.apply(productVo)).collect(Collectors.toList());
    }

    public ProductResponse retrieveProduct(final Long productId) {
        final ProductVo productVo = productService.retrieve(productId);
        return productVoToProductResponseMapper.apply(productVo);
    }

    public void delete(final Long productId) {
        productService.delete(productId);
    }
}