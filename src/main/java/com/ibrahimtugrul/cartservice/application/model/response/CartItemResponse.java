package com.ibrahimtugrul.cartservice.application.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private String productId;
    private String quantity;
    private String categoryId;
    private String appliedCampaign;
    private String totalAmount;
    private String totalAmountAfterCampaign;
}