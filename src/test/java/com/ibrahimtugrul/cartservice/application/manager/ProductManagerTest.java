package com.ibrahimtugrul.cartservice.application.manager;

import com.ibrahimtugrul.cartservice.application.converter.ProductCreateRequestToVoConverter;
import com.ibrahimtugrul.cartservice.application.model.request.ProductCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.domain.service.ProductService;
import com.ibrahimtugrul.cartservice.domain.vo.ProductCreateVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductManagerTest {

    private ProductManager productManager;

    @Mock
    private ProductCreateRequestToVoConverter productCreateRequestToVoConverter;

    @Mock
    private ProductService productService;

    @BeforeEach
    public void setup() {
        this.productManager = new ProductManager(productService, productCreateRequestToVoConverter);
    }

    @Test
    public void should_return_created_product_id_when_product_create_request_is_valid() {
        // given
        final ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
                .price(new BigDecimal(15.0))
                .title("product")
                .categoryId(1L)
                .build();

        final ProductCreateVo productCreateVo = ProductCreateVo.builder()
                .price(15.0)
                .title("product")
                .categoryId(1L)
                .build();

        final long productId = 1L;

        // when
        when(productCreateRequestToVoConverter.convert(productCreateRequest)).thenReturn(productCreateVo);
        when(productService.create(productCreateVo)).thenReturn(productId);

        final IdResponse responseEntity = productManager.create(productCreateRequest);

        // then
        final InOrder inOrder = Mockito.inOrder(productCreateRequestToVoConverter, productService);
        inOrder.verify(productCreateRequestToVoConverter).convert(productCreateRequest);
        inOrder.verify(productService).create(productCreateVo);
        inOrder.verifyNoMoreInteractions();

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getId()).isEqualTo(productId);
    }
}