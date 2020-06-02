package com.ibrahimtugrul.cartservice.domain.service;

import com.ibrahimtugrul.cartservice.domain.entity.Product;
import com.ibrahimtugrul.cartservice.domain.repository.ProductRepository;
import com.ibrahimtugrul.cartservice.domain.vo.ProductCreateVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public long create(final ProductCreateVo productCreateVo) {

        final Product product = Product.builder()
                .title(productCreateVo.getTitle())
                .categoryId(productCreateVo.getCategoryId())
                .price(productCreateVo.getPrice())
                .build();

        productRepository.save(product);
        return product.getId();
    }
}