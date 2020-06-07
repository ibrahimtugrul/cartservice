package com.ibrahimtugrul.cartservice.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartAddItemRequest {
    @NotBlank(message = "cart.validation.required.productId")
    private String productId;
    @NotBlank(message = "cart.validation.required.quantity")
    private String quantity;
}