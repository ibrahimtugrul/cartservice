package com.ibrahimtugrul.cartservice.domain.service;

import com.ibrahimtugrul.cartservice.domain.converter.ProductToVoConverter;
import com.ibrahimtugrul.cartservice.domain.entity.Product;
import com.ibrahimtugrul.cartservice.domain.exception.EntityNotFoundException;
import com.ibrahimtugrul.cartservice.domain.repository.ProductRepository;
import com.ibrahimtugrul.cartservice.domain.vo.ProductCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.ProductVo;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Captor
    private ArgumentCaptor<Product> productArgumentCaptor;

    @Mock
    private ProductToVoConverter productToVoConverter;

    private static final String ERR_ENTITY_NOT_FOUND = "domain.entity.notFound";

    @BeforeEach
    public void setup() {
        this.productService = new ProductService(productRepository, productToVoConverter);
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

    @Test
    public void should_list_all_products() {
        // given
        final Product product = Product.builder()
                .title("title")
                .price(15.0)
                .categoryId(2L)
                .id(1L)
                .build();

        final ProductVo productVo = ProductVo.builder()
                .title("title")
                .price(15.0)
                .categoryId(2L)
                .id(1L)
                .build();

        // when
        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productToVoConverter.convert(product)).thenReturn(productVo);

        final List<ProductVo> productVoList = productService.listAll();

        // then
        productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        final InOrder inOrder = Mockito.inOrder(productRepository, productToVoConverter);
        inOrder.verify(productRepository).findAll();
        inOrder.verify(productToVoConverter).convert(productArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(productVoList).isNotEmpty();
        assertThat(productVoList.size()).isEqualTo(1);
        assertThat(productVoList.get(0).getPrice()).isEqualTo(product.getPrice());
        assertThat(productVoList.get(0).getTitle()).isEqualTo(product.getTitle());
        assertThat(productVoList.get(0).getId()).isEqualTo(product.getId());
        assertThat(productVoList.get(0).getCategoryId()).isEqualTo(product.getCategoryId());
    }

    @Test
    public void should_retrieve_product_when_product_found() {
        // given
        final Long id = 1L;
        final Product product = Product.builder()
                .title("title")
                .price(15.0)
                .categoryId(2L)
                .id(1L)
                .build();

        final ProductVo productVo = ProductVo.builder()
                .title("title")
                .price(15.0)
                .categoryId(2L)
                .id(1L)
                .build();

        // when
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productToVoConverter.convert(product)).thenReturn(productVo);

        final ProductVo returnedProductVo = productService.retrieve(id);

        // then
        productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        final InOrder inOrder = Mockito.inOrder(productRepository, productToVoConverter);
        inOrder.verify(productRepository).findById(id);
        inOrder.verify(productToVoConverter).convert(productArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(returnedProductVo.getPrice()).isEqualTo(product.getPrice());
        assertThat(returnedProductVo.getTitle()).isEqualTo(product.getTitle());
        assertThat(returnedProductVo.getId()).isEqualTo(product.getId());
        assertThat(returnedProductVo.getCategoryId()).isEqualTo(product.getCategoryId());
    }

    @Test
    public void should_throw_exception_when_product_not_found() {
        // given
        final Long id = 1L;

        // when
        when(productRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        final Throwable throwable = catchThrowable(()->{productService.retrieve(id);});

        // then
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(EntityNotFoundException.class);

        final EntityNotFoundException entityNotFoundException = (EntityNotFoundException) throwable;
        assertThat(entityNotFoundException.getLocalizedMessage().contains(ERR_ENTITY_NOT_FOUND)).isTrue();
    }

    @Test
    public void should_delete_product_when_product_found() {
        // given
        final Long id = 1L;
        final Product product = Product.builder()
                .title("title")
                .price(15.0)
                .categoryId(2L)
                .id(1L)
                .build();

        // when
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        productService.delete(id);

        // then
        productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        final InOrder inOrder = Mockito.inOrder(productRepository);
        inOrder.verify(productRepository).findById(id);
        inOrder.verify(productRepository).delete(productArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(productArgumentCaptor.getValue()).isNotNull();

        final Product foundedProduct = productArgumentCaptor.getValue();

        assertThat(foundedProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(foundedProduct.getTitle()).isEqualTo(product.getTitle());
        assertThat(foundedProduct.getId()).isEqualTo(product.getId());
        assertThat(foundedProduct.getCategoryId()).isEqualTo(product.getCategoryId());
    }

    @Test
    public void should_throw_exception_when_delete_product_not_found() {
        // given
        final Long id = 1L;

        // when
        when(productRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        final Throwable throwable = catchThrowable(()->{productService.delete(id);});

        // then
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(EntityNotFoundException.class);

        final EntityNotFoundException entityNotFoundException = (EntityNotFoundException) throwable;
        assertThat(entityNotFoundException.getLocalizedMessage().contains(ERR_ENTITY_NOT_FOUND)).isTrue();
    }
}