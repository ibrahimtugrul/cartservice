package com.ibrahimtugrul.cartservice.infrastructure.rest;

import com.ibrahimtugrul.cartservice.application.manager.ProductManager;
import com.ibrahimtugrul.cartservice.application.model.request.ProductCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.application.model.response.ProductResponse;
import com.ibrahimtugrul.cartservice.infrastructure.rest.util.WebTestUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RestProductControllerTest {

    @Mock
    private ProductManager productManager;

    private MockMvc mockMvc;

    private static final String PRODUCT_URL = "/cartservice/v1/product";
    private static final String ERR_MISSING_PRODUCT_TITLE= "product.validation.required.title";
    private static final String ERR_MISSING_PRODUCT_PRICE= "product.validation.required.price";
    private static final String ERR_INVALID_PRODUCT_CATEGORY= "product.validation.required.category";

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new RestProductController(productManager)).build();
    }

    @Test
    public void should_return_product_id_when_product_create_request_is_valid() throws Exception {

        // given
        final ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
                .title("product")
                .price(new BigDecimal(15))
                .categoryId(2L)
                .build();

        final IdResponse productResponse = IdResponse.builder()
                .id(1L)
                .build();

        // when
        when(productManager.create(productCreateRequest)).thenReturn(productResponse);

        final ResultActions resultActions =  mockMvc.perform(post(PRODUCT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(productCreateRequest)));
        
        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id", is(1)));
        verify(productManager, times(1)).create(productCreateRequest);
    }
    
    @Test
    public void should_return_bad_request_when_product_title_is_empty() throws Exception {

        // given
        final ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
                .title(StringUtils.EMPTY)
                .price(new BigDecimal(15))
                .categoryId(2L)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(PRODUCT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(productCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_PRODUCT_TITLE)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_product_title_is_null() throws Exception {

        // given
        final ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
                .price(new BigDecimal(15))
                .categoryId(2L)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(PRODUCT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(productCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_PRODUCT_TITLE)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_product_title_is_blank() throws Exception {

        // given
        final ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
                .title(StringUtils.SPACE)
                .price(new BigDecimal(15))
                .categoryId(2L)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(PRODUCT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(productCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_PRODUCT_TITLE)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_product_price_is_null() throws Exception {

        // given
        final ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
                .title("product")
                .categoryId(2L)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(PRODUCT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(productCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_PRODUCT_PRICE)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_product_price_is_lower_than_0_1() throws Exception {

        // given
        final ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
                .title("product")
                .price(new BigDecimal(0.0))
                .categoryId(2L)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(PRODUCT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(productCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_PRODUCT_PRICE)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_product_category_is_invalid() throws Exception {

        // given
        final ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
                .title("product")
                .price(new BigDecimal(15))
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(PRODUCT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(productCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_INVALID_PRODUCT_CATEGORY)).isTrue();
    }
    
    @Test
    public void should_return_all_products() throws Exception {
        // given
        final ProductResponse productResponse = ProductResponse.builder()
                .categoryId(2L)
                .id(1L)
                .price(new BigDecimal(15.0))
                .title("product")
                .build();

        final ProductResponse productResponse1 = ProductResponse.builder()
                .categoryId(1L)
                .id(2L)
                .price(new BigDecimal(14.0))
                .title("product1")
                .build();
        
        // when
        when(productManager.listAll()).thenReturn(List.of(productResponse, productResponse1));

        final ResultActions resultActions =  mockMvc.perform(get(PRODUCT_URL)
                .contentType(MediaType.APPLICATION_JSON));
        
        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$", hasSize(2)));
        resultActions.andExpect(jsonPath("$[0].id", is(productResponse.getId().intValue())));
        resultActions.andExpect(jsonPath("$[0].categoryId", is(productResponse.getCategoryId().intValue())));
        resultActions.andExpect(jsonPath("$[0].title", is(productResponse.getTitle())));
        resultActions.andExpect(jsonPath("$[0].price", is(productResponse.getPrice().intValue())));
        resultActions.andExpect(jsonPath("$[1].id", is(productResponse1.getId().intValue())));
        resultActions.andExpect(jsonPath("$[1].categoryId", is(productResponse1.getCategoryId().intValue())));
        resultActions.andExpect(jsonPath("$[1].title", is(productResponse1.getTitle())));
        resultActions.andExpect(jsonPath("$[1].price", is(productResponse1.getPrice().intValue())));
    }
    
    @Test
    public void should_return_product_when_product_found_with_id() throws Exception {
        // given
        final Long productId = 1L;

        final ProductResponse productResponse = ProductResponse.builder()
                .categoryId(2L)
                .id(1L)
                .price(new BigDecimal(15.0))
                .title("product")
                .build();
        
        // when
        when(productManager.retrieveProduct(productId)).thenReturn(productResponse);
        
        final ResultActions resultActions =  mockMvc.perform(
                get(StringUtils.join(PRODUCT_URL, "/{productId}"), productId));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id", is(productResponse.getId().intValue())));
        resultActions.andExpect(jsonPath("$.categoryId", is(productResponse.getCategoryId().intValue())));
        resultActions.andExpect(jsonPath("$.title", is(productResponse.getTitle())));
        resultActions.andExpect(jsonPath("$.price", is(productResponse.getPrice().intValue())));
    }

    @Test
    public void should_delete_product_when_product_found_with_id() throws Exception {
        // given
        final Long productId = 1L;
        
        // when
        final ResultActions resultActions =  mockMvc.perform(
                delete(StringUtils.join(PRODUCT_URL, "/{productId}"), productId));

        // then
        resultActions.andExpect(status().isOk());
        verify(productManager, times(1)).delete(productId);
    }
}