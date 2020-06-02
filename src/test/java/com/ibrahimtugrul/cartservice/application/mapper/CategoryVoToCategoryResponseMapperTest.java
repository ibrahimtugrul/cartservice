package com.ibrahimtugrul.cartservice.application.mapper;

import com.ibrahimtugrul.cartservice.application.model.response.CategoryResponse;
import com.ibrahimtugrul.cartservice.domain.vo.CategoryVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryVoToCategoryResponseMapperTest {

    private CategoryVoToCategoryResponseMapper categoryVoToCategoryResponseMapper;

    @BeforeEach
    public void setup() {
        this.categoryVoToCategoryResponseMapper = new CategoryVoToCategoryResponseMapper();
    }

    @Test
    public void should_map() {
        // given
        final CategoryVo categoryVo = CategoryVo.builder()
                .parentId(1L)
                .id(2L)
                .title("category")
                .build();

        // when
        final CategoryResponse categoryResponse = categoryVoToCategoryResponseMapper.apply(categoryVo);

        // then
        assertThat(categoryResponse).isNotNull();
        assertThat(categoryResponse.getParentId()).isEqualTo(categoryVo.getParentId());
        assertThat(categoryResponse.getId()).isEqualTo(categoryVo.getId());
        assertThat(categoryResponse.getTitle()).isEqualTo(categoryVo.getTitle());
    }
}