package com.ibrahimtugrul.cartservice.application.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CampaignResponse {
    private String id;
    private String minimumBuyingRule;
    private String categoryId;
    private String discount;
    private String discountType;
}