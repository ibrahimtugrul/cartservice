package com.ibrahimtugrul.cartservice.infrastructure.rest.integration;

import com.ibrahimtugrul.cartservice.application.model.request.ProductCreateRequest;
import com.ibrahimtugrul.cartservice.domain.entity.Product;
import com.ibrahimtugrul.cartservice.domain.repository.ProductRepository;
import com.ibrahimtugrul.cartservice.infrastructure.rest.util.WebTestUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestProductControllerIT extends BaseWebIT {

    @Autowired
    private ProductRepository productRepository;

    private static final String PRODUCT_URL = "/api/v1/product";

    @Test
    public void should_save_product() throws Exception {
        // given
        final ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
                .title("product")
                .price("15")
                .categoryId(2L)
                .build();

        // when
        final ResultActions resultActions = mockMvc.perform(post(PRODUCT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(productCreateRequest)));

        // then
        resultActions.andExpect(status().isOk());

        final List<Product> productList = productRepository.findAll();

        assertThat(productList).isNotEmpty();
        assertThat(productList.size()).isEqualTo(1);

        final Product savedProduct = productList.get(0);

        assertThat(savedProduct.getPrice()).isEqualTo(Double.valueOf(productCreateRequest.getPrice().toString()));
        assertThat(savedProduct.getTitle()).isEqualTo(productCreateRequest.getTitle());
        assertThat(savedProduct.getCategoryId()).isEqualTo(productCreateRequest.getCategoryId());

        resultActions.andExpect(jsonPath("$.id", is(String.valueOf(savedProduct.getId()))));
    }

    @Test
    public void should_return_product_list() throws Exception {
        // given
        final Product product = Product.builder()
                .title("product")
                .price(15.0)
                .categoryId(2L)
                .build();

        productRepository.save(product);

        // when
        final ResultActions resultActions = mockMvc.perform(get(PRODUCT_URL)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$", hasSize(1)));
        resultActions.andExpect(jsonPath("$[0].id", is(String.valueOf(product.getId()))));
        resultActions.andExpect(jsonPath("$[0].price", is(String.valueOf(product.getPrice()))));
        resultActions.andExpect(jsonPath("$[0].categoryId", is(String.valueOf(product.getCategoryId()))));
        resultActions.andExpect(jsonPath("$[0].title", is(product.getTitle())));
    }

    @Test
    public void should_return_product_when_product_found() throws Exception {
        // given
        final Product product = Product.builder()
                .title("product")
                .price(15.0)
                .categoryId(2L)
                .build();

        productRepository.save(product);

        // when
        final ResultActions resultActions = mockMvc.perform(get(StringUtils.join(PRODUCT_URL, "/{productId}")
                , product.getId()));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id", is(String.valueOf(product.getId()))));
        resultActions.andExpect(jsonPath("$.price", is(String.valueOf(product.getPrice()))));
        resultActions.andExpect(jsonPath("$.categoryId", is(String.valueOf(product.getCategoryId()))));
        resultActions.andExpect(jsonPath("$.title", is(product.getTitle())));
    }

    @Test
    public void should_delete_product_when_product_found() throws  Exception {
        final Product product = Product.builder()
                .title("product")
                .price(15.0)
                .categoryId(2L)
                .build();

        productRepository.save(product);

        // when
        final ResultActions resultActions = mockMvc.perform(delete(StringUtils.join(PRODUCT_URL, "/{productId}")
                , product.getId()));

        // then
        resultActions.andExpect(status().isOk());
    }

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
    }
}