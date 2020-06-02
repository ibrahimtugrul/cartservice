package com.ibrahimtugrul.cartservice.application.manager;

import com.ibrahimtugrul.cartservice.application.converter.ProductCreateRequestToVoConverter;
import com.ibrahimtugrul.cartservice.application.mapper.ProductVoToProductResponseMapper;
import com.ibrahimtugrul.cartservice.application.model.request.ProductCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.application.model.response.ProductResponse;
import com.ibrahimtugrul.cartservice.domain.service.ProductService;
import com.ibrahimtugrul.cartservice.domain.vo.ProductCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.ProductVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductManagerTest {

    private ProductManager productManager;

    @Mock
    private ProductCreateRequestToVoConverter productCreateRequestToVoConverter;

    @Mock
    private ProductService productService;

    @Mock
    private ProductVoToProductResponseMapper productVoToProductResponseMapper;

    @BeforeEach
    public void setup() {
        this.productManager = new ProductManager(productService, productCreateRequestToVoConverter, productVoToProductResponseMapper);
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

    @Test
    public void should_return_product_list() {
        // given
        final ProductVo productVo = ProductVo.builder()
                .id(1L)
                .price(15.0)
                .title("product")
                .categoryId(1L)
                .build();

        final ProductResponse productResponse = ProductResponse.builder()
                .id(1L)
                .price(new BigDecimal(15.0))
                .title("product")
                .categoryId(1L)
                .build();

        // when
        when(productService.listAll()).thenReturn(List.of(productVo));
        when(productVoToProductResponseMapper.apply(productVo)).thenReturn(productResponse);

        final List<ProductResponse> productResponseList = productManager.listAll();

        // then
        final InOrder inOrder = Mockito.inOrder(productService, productVoToProductResponseMapper);
        inOrder.verify(productService).listAll();
        inOrder.verify(productVoToProductResponseMapper).apply(productVo);
        inOrder.verifyNoMoreInteractions();

        assertThat(productResponseList).isNotEmpty();
        assertThat(productResponseList.size()).isEqualTo(1);
        assertThat(productResponseList.get(0).getTitle()).isEqualTo(productVo.getTitle());
        assertThat(productResponseList.get(0).getPrice().doubleValue()).isEqualTo(productVo.getPrice());
        assertThat(productResponseList.get(0).getId()).isEqualTo(productVo.getId());
        assertThat(productResponseList.get(0).getCategoryId()).isEqualTo(productVo.getCategoryId());
    }

    @Test
    public void should_return_product_when_product_found_byId() {
        // given
        final Long id = 1L;
        final ProductVo productVo = ProductVo.builder()
                .id(1L)
                .price(15.0)
                .title("product")
                .categoryId(1L)
                .build();

        final ProductResponse productResponse = ProductResponse.builder()
                .id(1L)
                .price(new BigDecimal(15.0))
                .title("product")
                .categoryId(1L)
                .build();

        // when
        when(productService.retrieve(id)).thenReturn(productVo);
        when(productVoToProductResponseMapper.apply(productVo)).thenReturn(productResponse);

        final ProductResponse productResponse1 = productManager.retrieveProduct(id);

        // then
        final InOrder inOrder = Mockito.inOrder(productService, productVoToProductResponseMapper);
        inOrder.verify(productService).retrieve(id);
        inOrder.verify(productVoToProductResponseMapper).apply(productVo);
        inOrder.verifyNoMoreInteractions();

        assertThat(productResponse1).isNotNull();
        assertThat(productResponse1.getTitle()).isEqualTo(productVo.getTitle());
        assertThat(productResponse1.getPrice().doubleValue()).isEqualTo(productVo.getPrice());
        assertThat(productResponse1.getId()).isEqualTo(productVo.getId());
        assertThat(productResponse1.getCategoryId()).isEqualTo(productVo.getCategoryId());
    }
}