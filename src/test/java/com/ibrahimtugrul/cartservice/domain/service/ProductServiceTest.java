package com.ibrahimtugrul.cartservice.domain.service;

import com.ibrahimtugrul.cartservice.domain.entity.Product;
import com.ibrahimtugrul.cartservice.domain.repository.ProductRepository;
import com.ibrahimtugrul.cartservice.domain.vo.ProductCreateVo;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Captor
    private ArgumentCaptor<Product> productArgumentCaptor;

    @BeforeEach
    public void setup() {
        this.productService = new ProductService(productRepository);
    }

    @Test
    public void should_save_product() {
        // given
        final ProductCreateVo productCreateVo = ProductCreateVo.builder()
                .title("title")
                .price(15.0)
                .categoryId(2L)
                .build();

        // when
        when(productRepository.save(any(Product.class))).thenAnswer(new Answer<Product>() {
            @Override
            public Product answer(InvocationOnMock invocation) {
                Product product = (Product) invocation.getArguments()[0];
                product.setId(1L);
                return product;
            }
        });

        final long productId = productService.create(productCreateVo);

        // then
        productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        final InOrder inOrder = Mockito.inOrder(productRepository);
        inOrder.verify(productRepository).save(productArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(productArgumentCaptor.getValue()).isNotNull();

        final Product savedProduct = productArgumentCaptor.getValue();

        assertThat(savedProduct.getId()).isEqualTo(productId);
        assertThat(savedProduct.getCategoryId()).isEqualTo(productCreateVo.getCategoryId());
        assertThat(savedProduct.getTitle()).isEqualTo(productCreateVo.getTitle());
        assertThat(savedProduct.getPrice()).isEqualTo(productCreateVo.getPrice());
    }
}