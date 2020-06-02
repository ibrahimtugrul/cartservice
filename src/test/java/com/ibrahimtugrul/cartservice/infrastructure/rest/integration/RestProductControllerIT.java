package com.ibrahimtugrul.cartservice.infrastructure.rest.integration;

import com.ibrahimtugrul.cartservice.application.model.request.ProductCreateRequest;
import com.ibrahimtugrul.cartservice.domain.entity.Product;
import com.ibrahimtugrul.cartservice.domain.repository.ProductRepository;
import com.ibrahimtugrul.cartservice.infrastructure.rest.util.WebTestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestProductControllerIT extends BaseWebIT {

    @Autowired
    private ProductRepository productRepository;

    private static final String PRODUCT_URL = "/cartservice/v1/product";

    @BeforeEach
    public void setup() {// to delete initial values for integration test health
        productRepository.deleteAll();
    }
    @Test
    public void should_save_product() throws Exception {
        // given
        final ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
                .title("product")
                .price(new BigDecimal(15))
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

        resultActions.andExpect(jsonPath("$.id", is(savedProduct.getId().intValue())));
    }

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
    }
}