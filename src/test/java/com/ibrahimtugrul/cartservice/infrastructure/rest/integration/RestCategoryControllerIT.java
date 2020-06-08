package com.ibrahimtugrul.cartservice.infrastructure.rest.integration;

import com.ibrahimtugrul.cartservice.application.model.request.CategoryCreateRequest;
import com.ibrahimtugrul.cartservice.domain.entity.Category;
import com.ibrahimtugrul.cartservice.domain.repository.CategoryRepository;
import com.ibrahimtugrul.cartservice.infrastructure.rest.util.WebTestUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestCategoryControllerIT extends BaseWebIT {
    @Autowired
    private CategoryRepository categoryRepository;

    private static final String CATEGORY_URL = "/api/v1/category";

    @Test
    public void should_save_category() throws Exception {
        // given
        final CategoryCreateRequest categoryCreateRequest = CategoryCreateRequest.builder()
                .title("category")
                .parentId(2L)
                .build();

        // when
        final ResultActions resultActions = mockMvc.perform(post(CATEGORY_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(categoryCreateRequest)));

        // then
        resultActions.andExpect(status().isOk());

        final List<Category> categoryList = categoryRepository.findAll();

        assertThat(categoryList).isNotEmpty();
        assertThat(categoryList.size()).isEqualTo(1);

        final Category savedCategory = categoryList.get(0);

        assertThat(savedCategory.getTitle()).isEqualTo(categoryCreateRequest.getTitle());
        assertThat(savedCategory.getParentId()).isEqualTo(categoryCreateRequest.getParentId());

        resultActions.andExpect(jsonPath("$.id", is(String.valueOf(savedCategory.getId()))));
    }

    @Test
    public void should_return_category_list() throws Exception {
        // given
        final Category category = Category.builder()
                .title("category")
                .parentId(2L)
                .build();

        categoryRepository.save(category);

        // when
        final ResultActions resultActions = mockMvc.perform(get(CATEGORY_URL)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$", hasSize(1)));
        resultActions.andExpect(jsonPath("$[0].id", is(String.valueOf(category.getId()))));
        resultActions.andExpect(jsonPath("$[0].parentId", is(String.valueOf(category.getParentId()))));
        resultActions.andExpect(jsonPath("$[0].title", is(category.getTitle())));
    }

    @Test
    public void should_return_category_when_category_found() throws Exception {
        // given
        final Category category = Category.builder()
                .title("category")
                .parentId(2L)
                .build();

        categoryRepository.save(category);

        // when
        final ResultActions resultActions = mockMvc.perform(get(StringUtils.join(CATEGORY_URL, "/{categoryId}")
                , category.getId()));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id", is(String.valueOf(category.getId()))));
        resultActions.andExpect(jsonPath("$.parentId", is(String.valueOf(category.getParentId()))));
        resultActions.andExpect(jsonPath("$.title", is(category.getTitle())));
    }

    @Test
    public void should_delete_category_when_category_found() throws  Exception {
        final Category category = Category.builder()
                .title("category")
                .parentId(2L)
                .build();

        categoryRepository.save(category);

        // when
        final ResultActions resultActions = mockMvc.perform(delete(StringUtils.join(CATEGORY_URL, "/{categoryId}")
                , category.getId()));

        // then
        resultActions.andExpect(status().isOk());
    }

    @AfterEach
    public void tearDown() {
        categoryRepository.deleteAll();
    }
}