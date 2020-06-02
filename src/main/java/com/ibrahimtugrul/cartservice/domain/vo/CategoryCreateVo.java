package com.ibrahimtugrul.cartservice.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CategoryCreateVo {
    private String title;
    private long parentId;
}