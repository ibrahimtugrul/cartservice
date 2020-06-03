package com.ibrahimtugrul.cartservice.application.manager;

import com.ibrahimtugrul.cartservice.application.converter.CategoryCreateRequestToVoConverter;
import com.ibrahimtugrul.cartservice.application.mapper.CategoryVoToCategoryResponseMapper;
import com.ibrahimtugrul.cartservice.application.model.request.CategoryCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CategoryResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.domain.service.CategoryService;
import com.ibrahimtugrul.cartservice.domain.vo.CategoryCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.CategoryVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryManagerTest {

    private CategoryManager categoryManager;

    @Mock
    private CategoryCreateRequestToVoConverter categoryCreateRequestToVoConverter;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryVoToCategoryResponseMapper categoryVoToCategoryResponseMapper;

    @BeforeEach
    public void setup() {
        this.categoryManager = new CategoryManager(categoryService, categoryCreateRequestToVoConverter, categoryVoToCategoryResponseMapper);
    }

    @Test
    public void should_return_created_category_id_when_category_create_request_is_valid() {
        // given
        final CategoryCreateRequest categoryCreateRequest = CategoryCreateRequest.builder()
                .title("product")
                .parentId(1L)
                .build();

        final CategoryCreateVo categoryCreateVo = CategoryCreateVo.builder()
                .title("product")
                .parentId(1L)
                .build();

        final long categoryId = 1L;

        // when
        when(categoryCreateRequestToVoConverter.convert(categoryCreateRequest)).thenReturn(categoryCreateVo);
        when(categoryService.create(categoryCreateVo)).thenReturn(categoryId);

        final IdResponse responseEntity = categoryManager.create(categoryCreateRequest);

        // then
        final InOrder inOrder = Mockito.inOrder(categoryCreateRequestToVoConverter, categoryService);
        inOrder.verify(categoryCreateRequestToVoConverter).convert(categoryCreateRequest);
        inOrder.verify(categoryService).create(categoryCreateVo);
        inOrder.verifyNoMoreInteractions();

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getId()).isEqualTo(String.valueOf(categoryId));
    }

    @Test
    public void should_return_category_list() {
        // given
        final CategoryVo categoryVo = CategoryVo.builder()
                .id(1L)
                .title("product")
                .parentId(1L)
                .build();

        final CategoryResponse categoryResponse = CategoryResponse.builder()
                .id("1")
                .title("product")
                .parentId("1")
                .build();

        // when
        when(categoryService.listAll()).thenReturn(List.of(categoryVo));
        when(categoryVoToCategoryResponseMapper.apply(categoryVo)).thenReturn(categoryResponse);

        final List<CategoryResponse> categoryResponseList = categoryManager.listAll();

        // then
        final InOrder inOrder = Mockito.inOrder(categoryService, categoryVoToCategoryResponseMapper);
        inOrder.verify(categoryService).listAll();
        inOrder.verify(categoryVoToCategoryResponseMapper).apply(categoryVo);
        inOrder.verifyNoMoreInteractions();

        assertThat(categoryResponseList).isNotEmpty();
        assertThat(categoryResponseList.size()).isEqualTo(1);
        assertThat(categoryResponseList.get(0).getTitle()).isEqualTo(categoryVo.getTitle());
        assertThat(categoryResponseList.get(0).getId()).isEqualTo(String.valueOf(categoryVo.getId()));
        assertThat(categoryResponseList.get(0).getParentId()).isEqualTo(String.valueOf(categoryVo.getParentId()));
    }

    @Test
    public void should_return_category_when_category_found_byId() {
        // given
        final Long id = 1L;
        final CategoryVo categoryVo = CategoryVo.builder()
                .id(1L)
                .title("product")
                .parentId(1L)
                .build();

        final CategoryResponse categoryResponse = CategoryResponse.builder()
                .id("1")
                .title("product")
                .parentId("1")
                .build();

        // when
        when(categoryService.retrieve(id)).thenReturn(categoryVo);
        when(categoryVoToCategoryResponseMapper.apply(categoryVo)).thenReturn(categoryResponse);

        final CategoryResponse categoryResponse1 = categoryManager.retrieveCategory(id);

        // then
        final InOrder inOrder = Mockito.inOrder(categoryService, categoryVoToCategoryResponseMapper);
        inOrder.verify(categoryService).retrieve(id);
        inOrder.verify(categoryVoToCategoryResponseMapper).apply(categoryVo);
        inOrder.verifyNoMoreInteractions();

        assertThat(categoryResponse1).isNotNull();
        assertThat(categoryResponse1.getTitle()).isEqualTo(categoryVo.getTitle());
        assertThat(categoryResponse1.getId()).isEqualTo(String.valueOf(categoryVo.getId()));
        assertThat(categoryResponse1.getParentId()).isEqualTo(String.valueOf(categoryVo.getParentId()));
    }

    @Test
    public void should_delete_category_when_category_found() {
        // given
        final Long id = 1L;

        // when
        categoryManager.delete(id);

        // then
        final InOrder inOrder = Mockito.inOrder(categoryService);
        inOrder.verify(categoryService).delete(id);
        inOrder.verifyNoMoreInteractions();
    }
}