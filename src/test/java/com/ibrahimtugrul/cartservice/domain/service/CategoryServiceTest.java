package com.ibrahimtugrul.cartservice.domain.service;

import com.ibrahimtugrul.cartservice.domain.converter.CategoryToVoConverter;
import com.ibrahimtugrul.cartservice.domain.entity.Category;
import com.ibrahimtugrul.cartservice.domain.exception.EntityNotFoundException;
import com.ibrahimtugrul.cartservice.domain.repository.CategoryRepository;
import com.ibrahimtugrul.cartservice.domain.vo.CategoryCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.CategoryVo;
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
public class CategoryServiceTest {
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Captor
    private ArgumentCaptor<Category> categoryArgumentCaptor;

    @Mock
    private CategoryToVoConverter categoryToVoConverter;

    private static final String ERR_ENTITY_NOT_FOUND = "domain.entity.notFound";

    @BeforeEach
    public void setup() {
        this.categoryService = new CategoryService(categoryRepository, categoryToVoConverter);
    }

    @Test
    public void should_save_category() {
        // given
        final CategoryCreateVo categoryCreateVo = CategoryCreateVo.builder()
                .title("title")
                .parentId(2L)
                .build();

        // when
        when(categoryRepository.save(any(Category.class))).thenAnswer(new Answer<Category>() {
            @Override
            public Category answer(InvocationOnMock invocation) {
                Category category = (Category) invocation.getArguments()[0];
                category.setId(1L);
                return category;
            }
        });

        final long productId = categoryService.create(categoryCreateVo);

        // then
        categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);

        final InOrder inOrder = Mockito.inOrder(categoryRepository);
        inOrder.verify(categoryRepository).save(categoryArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(categoryArgumentCaptor.getValue()).isNotNull();

        final Category savedCategory = categoryArgumentCaptor.getValue();

        assertThat(savedCategory.getId()).isEqualTo(productId);
        assertThat(savedCategory.getParentId()).isEqualTo(categoryCreateVo.getParentId());
        assertThat(savedCategory.getTitle()).isEqualTo(categoryCreateVo.getTitle());
    }

    @Test
    public void should_list_all_categories() {
        // given
        final Category category = Category.builder()
                .title("title")
                .parentId(2L)
                .id(1L)
                .build();

        final CategoryVo categoryVo = CategoryVo.builder()
                .title("title")
                .parentId(2L)
                .id(1L)
                .build();

        // when
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryToVoConverter.convert(category)).thenReturn(categoryVo);

        final List<CategoryVo> categoryVoList = categoryService.listAll();

        // then
        categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);

        final InOrder inOrder = Mockito.inOrder(categoryRepository, categoryToVoConverter);
        inOrder.verify(categoryRepository).findAll();
        inOrder.verify(categoryToVoConverter).convert(categoryArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(categoryVoList).isNotEmpty();
        assertThat(categoryVoList.size()).isEqualTo(1);
        assertThat(categoryVoList.get(0).getTitle()).isEqualTo(category.getTitle());
        assertThat(categoryVoList.get(0).getId()).isEqualTo(category.getId());
        assertThat(categoryVoList.get(0).getParentId()).isEqualTo(category.getParentId());
    }

    @Test
    public void should_retrieve_category_when_category_found() {
        // given
        final Long id = 1L;
        final Category category = Category.builder()
                .title("title")
                .parentId(2L)
                .id(1L)
                .build();

        final CategoryVo categoryVo = CategoryVo.builder()
                .title("title")
                .parentId(2L)
                .id(1L)
                .build();

        // when
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryToVoConverter.convert(category)).thenReturn(categoryVo);

        final CategoryVo returnedCategoryVo = categoryService.retrieve(id);

        // then
        categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);

        final InOrder inOrder = Mockito.inOrder(categoryRepository, categoryToVoConverter);
        inOrder.verify(categoryRepository).findById(id);
        inOrder.verify(categoryToVoConverter).convert(categoryArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(returnedCategoryVo.getTitle()).isEqualTo(category.getTitle());
        assertThat(returnedCategoryVo.getId()).isEqualTo(category.getId());
        assertThat(returnedCategoryVo.getParentId()).isEqualTo(category.getParentId());
    }

    @Test
    public void should_throw_exception_when_category_not_found() {
        // given
        final Long id = 1L;

        // when
        when(categoryRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        final Throwable throwable = catchThrowable(()->{categoryService.retrieve(id);});

        // then
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(EntityNotFoundException.class);

        final EntityNotFoundException entityNotFoundException = (EntityNotFoundException) throwable;
        assertThat(entityNotFoundException.getLocalizedMessage().contains(ERR_ENTITY_NOT_FOUND)).isTrue();
    }

    @Test
    public void should_delete_category_when_category_found() {
        // given
        final Long id = 1L;
        final Category category = Category.builder()
                .title("title")
                .parentId(2L)
                .id(1L)
                .build();

        // when
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        categoryService.delete(id);

        // then
        categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);

        final InOrder inOrder = Mockito.inOrder(categoryRepository);
        inOrder.verify(categoryRepository).findById(id);
        inOrder.verify(categoryRepository).delete(categoryArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(categoryArgumentCaptor.getValue()).isNotNull();

        final Category foundedCategory = categoryArgumentCaptor.getValue();

        assertThat(foundedCategory.getTitle()).isEqualTo(category.getTitle());
        assertThat(foundedCategory.getId()).isEqualTo(category.getId());
        assertThat(foundedCategory.getParentId()).isEqualTo(category.getParentId());
    }

    @Test
    public void should_throw_exception_when_delete_category_not_found() {
        // given
        final Long id = 1L;

        // when
        when(categoryRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        final Throwable throwable = catchThrowable(()->{categoryService.delete(id);});

        // then
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(EntityNotFoundException.class);

        final EntityNotFoundException entityNotFoundException = (EntityNotFoundException) throwable;
        assertThat(entityNotFoundException.getLocalizedMessage().contains(ERR_ENTITY_NOT_FOUND)).isTrue();
    }
}