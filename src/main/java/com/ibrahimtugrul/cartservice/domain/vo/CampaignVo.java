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

    public double calculateCampaignDiscount(final Long quantity, final double productPrice) {
        switch (this.getDiscountType()) {
            case RATE:
                return ((quantity * productPrice) * discount) / 100;
            case AMOUNT:
                return (quantity * productPrice) - discount >= 0 ? discount : 0;
            default:
                return 0.0;
        }
    }
}