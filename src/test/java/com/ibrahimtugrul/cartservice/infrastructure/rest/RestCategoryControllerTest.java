package com.ibrahimtugrul.cartservice.infrastructure.rest;

import com.ibrahimtugrul.cartservice.application.manager.CategoryManager;
import com.ibrahimtugrul.cartservice.application.model.request.CategoryCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CategoryResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RestCategoryControllerTest {

    @Mock
    private CategoryManager categoryManager;

    private MockMvc mockMvc;

    private static final String CATEGORY_URL = "/api/v1/category";
    private static final String ERR_MISSING_CATEGORY_TITLE= "category.validation.required.title";

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new RestCategoryController(categoryManager)).build();
    }

    @Test
    public void should_return_category_id_when_category_create_request_is_valid() throws Exception {

        // given
        final CategoryCreateRequest categoryCreateRequest = CategoryCreateRequest.builder()
                .title("category")
                .parentId(2L)
                .build();

        final IdResponse categoryResponse = IdResponse.builder()
                .id("1")
                .build();

        // when
        when(categoryManager.create(categoryCreateRequest)).thenReturn(categoryResponse);

        final ResultActions resultActions =  mockMvc.perform(post(CATEGORY_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(categoryCreateRequest)));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id", is("1")));
        verify(categoryManager, times(1)).create(categoryCreateRequest);
    }

    @Test
    public void should_return_bad_request_when_category_title_is_empty() throws Exception {

        // given
        final CategoryCreateRequest categoryCreateRequest = CategoryCreateRequest.builder()
                .title(StringUtils.EMPTY)
                .parentId(2L)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CATEGORY_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(categoryCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_CATEGORY_TITLE)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_category_title_is_null() throws Exception {

        // given
        final CategoryCreateRequest categoryCreateRequest = CategoryCreateRequest.builder()
                .parentId(2L)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CATEGORY_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(categoryCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_CATEGORY_TITLE)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_category_title_is_blank() throws Exception {

        // given
        final CategoryCreateRequest categoryCreateRequest = CategoryCreateRequest.builder()
                .title(StringUtils.SPACE)
                .parentId(2L)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CATEGORY_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(categoryCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_CATEGORY_TITLE)).isTrue();
    }

    @Test
    public void should_return_all_categories() throws Exception {
        // given
        final CategoryResponse categoryResponse = CategoryResponse.builder()
                .parentId("2")
                .id("1")
                .title("category")
                .build();

        final CategoryResponse categoryResponse1 = CategoryResponse.builder()
                .parentId("1")
                .id("2")
                .title("category1")
                .build();

        // when
        when(categoryManager.listAll()).thenReturn(List.of(categoryResponse, categoryResponse1));

        final ResultActions resultActions =  mockMvc.perform(get(CATEGORY_URL)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$", hasSize(2)));
        resultActions.andExpect(jsonPath("$[0].id", is(categoryResponse.getId())));
        resultActions.andExpect(jsonPath("$[0].parentId", is(categoryResponse.getParentId())));
        resultActions.andExpect(jsonPath("$[0].title", is(categoryResponse.getTitle())));
        resultActions.andExpect(jsonPath("$[1].id", is(categoryResponse1.getId())));
        resultActions.andExpect(jsonPath("$[1].parentId", is(categoryResponse1.getParentId())));
        resultActions.andExpect(jsonPath("$[1].title", is(categoryResponse1.getTitle())));
    }

    @Test
    public void should_return_category_when_category_found_with_id() throws Exception {
        // given
        final Long categoryId = 1L;

        final CategoryResponse categoryResponse = CategoryResponse.builder()
                .parentId("2")
                .id("1")
                .title("category")
                .build();

        // when
        when(categoryManager.retrieveCategory(categoryId)).thenReturn(categoryResponse);

        final ResultActions resultActions =  mockMvc.perform(
                get(StringUtils.join(CATEGORY_URL, "/{categoryId}"), categoryId));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id", is(categoryResponse.getId())));
        resultActions.andExpect(jsonPath("$.parentId", is(categoryResponse.getParentId())));
        resultActions.andExpect(jsonPath("$.title", is(categoryResponse.getTitle())));
    }

    @Test
    public void should_delete_category_when_category_found_with_id() throws Exception {
        // given
        final Long categoryId = 1L;

        // when
        final ResultActions resultActions =  mockMvc.perform(
                delete(StringUtils.join(CATEGORY_URL, "/{categoryId}"), categoryId));

        // then
        resultActions.andExpect(status().isOk());
        verify(categoryManager, times(1)).delete(categoryId);
    }
}