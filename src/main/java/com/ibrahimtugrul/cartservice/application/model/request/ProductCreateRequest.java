package com.ibrahimtugrul.cartservice.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {
    @NotBlank(message = "product.validation.required.title")
    private String title;
    @NotBlank(message = "product.validation.required.price")
    private String price;
    @Min(value = 1, message = "product.validation.required.category")
    private long categoryId;
}