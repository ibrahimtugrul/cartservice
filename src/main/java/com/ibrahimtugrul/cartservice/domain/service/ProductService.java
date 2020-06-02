package com.ibrahimtugrul.cartservice.domain.service;

import com.ibrahimtugrul.cartservice.domain.converter.ProductToVoConverter;
import com.ibrahimtugrul.cartservice.domain.entity.Product;
import com.ibrahimtugrul.cartservice.domain.exception.EntityNotFoundException;
import com.ibrahimtugrul.cartservice.domain.repository.ProductRepository;
import com.ibrahimtugrul.cartservice.domain.vo.ProductCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductToVoConverter productToVoConverter;

    public long create(final ProductCreateVo productCreateVo) {

        final Product product = Product.builder()
                .title(productCreateVo.getTitle())
                .categoryId(productCreateVo.getCategoryId())
                .price(productCreateVo.getPrice())
                .build();

        productRepository.save(product);
        return product.getId();
    }

    public List<ProductVo> listAll() {
        final List<Product> productList = productRepository.findAll();
        return productList.stream().map(product -> productToVoConverter.convert(product)).collect(Collectors.toList());
    }

    public ProductVo retrieve(final Long productId) {
        final Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("product"));
        return productToVoConverter.convert(product);
    }

    public void delete(final Long productId) {
        final Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("product"));
        productRepository.delete(product);
    }
}