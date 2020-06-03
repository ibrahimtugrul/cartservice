package com.ibrahimtugrul.cartservice.application.model.response;

import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CampaignResponse {
    private long id;
    private int minimumBuyingRule;
    private long categoryId;
    private double discount;
    private DiscountType discountType;
}