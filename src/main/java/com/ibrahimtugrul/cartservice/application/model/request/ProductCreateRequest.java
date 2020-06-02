package com.ibrahimtugrul.cartservice.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {
    @NotBlank(message = "product.validation.required.title")
    private String title;
    @NotNull(message = "product.validation.required.price")
    @DecimalMin(value ="0.1", message = "product.validation.required.price")
    private BigDecimal price;
    @Min(value = 1, message = "product.validation.required.category")
    private long categoryId;
}