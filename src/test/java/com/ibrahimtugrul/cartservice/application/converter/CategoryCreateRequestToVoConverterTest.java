package com.ibrahimtugrul.cartservice.application.converter;

import com.ibrahimtugrul.cartservice.application.model.request.CategoryCreateRequest;
import com.ibrahimtugrul.cartservice.domain.vo.CategoryCreateVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryCreateRequestToVoConverterTest {

    private CategoryCreateRequestToVoConverter categoryCreateRequestToVoConverter;

    @BeforeEach
    public void setup() {
        this.categoryCreateRequestToVoConverter = new CategoryCreateRequestToVoConverter();
    }

    @Test
    public void should_convert() {
        // given
        final CategoryCreateRequest categoryCreateRequest = CategoryCreateRequest.builder()
                .title("product")
                .parentId(1L)
                .build();

        // when
        final CategoryCreateVo categoryCreateVo = categoryCreateRequestToVoConverter.convert(categoryCreateRequest);

        // then
        assertThat(categoryCreateVo).isNotNull();
        assertThat(categoryCreateVo.getTitle()).isEqualTo(categoryCreateVo.getTitle());
        assertThat(categoryCreateVo.getParentId()).isEqualTo(categoryCreateVo.getParentId());
    }
}