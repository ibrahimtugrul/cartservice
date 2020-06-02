package com.ibrahimtugrul.cartservice.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CategoryVo {
    private Long id;

    private String title;

    private Long parentId;
}