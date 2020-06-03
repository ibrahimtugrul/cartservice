package com.ibrahimtugrul.cartservice.domain.vo;

import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CampaignVo {
    private long id;
    private int minimumBuyingRule;
    private long categoryId;
    private double discount;
    private DiscountType discountType;
}