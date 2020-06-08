package com.ibrahimtugrul.cartservice.application.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private String id;
    private List<CartItemResponse> items;
    private String appliedCoupon;
    private String totalAmount;
    private String totalAmountAfterDiscount;
    private String couponAmount;
    private String totalAmountAfterCoupon;
}