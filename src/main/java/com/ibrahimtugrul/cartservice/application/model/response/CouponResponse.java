package com.ibrahimtugrul.cartservice.application.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {
    private String id;
    private String minimumAmount;
    private String discount;
    private String discountType;
}