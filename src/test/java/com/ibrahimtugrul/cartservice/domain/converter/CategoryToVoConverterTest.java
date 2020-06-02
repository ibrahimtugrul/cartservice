package com.ibrahimtugrul.cartservice.domain.converter;

import com.ibrahimtugrul.cartservice.domain.entity.Category;
import com.ibrahimtugrul.cartservice.domain.vo.CategoryVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryToVoConverterTest {
    private CategoryToVoConverter categoryToVoConverter;

    @BeforeEach
    public void setup() {
        this.categoryToVoConverter = new CategoryToVoConverter();
    }

    @Test
    public void should_convert() {
        // given
        final Category category = Category.builder()
                .parentId(1L)
                .title("category")
                .id(1L)
                .build();

        // when
        final CategoryVo categoryVo = categoryToVoConverter.convert(category);

        // then
        assertThat(categoryVo).isNotNull();
        assertThat(categoryVo.getParentId()).isEqualTo(category.getParentId());
        assertThat(categoryVo.getId()).isEqualTo(category.getId());
        assertThat(categoryVo.getTitle()).isEqualTo(category.getTitle());
    }
}