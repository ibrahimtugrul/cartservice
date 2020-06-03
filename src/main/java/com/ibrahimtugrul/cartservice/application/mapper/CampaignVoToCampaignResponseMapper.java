package com.ibrahimtugrul.cartservice.application.mapper;

import com.ibrahimtugrul.cartservice.application.model.response.CampaignResponse;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignVo;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CampaignVoToCampaignResponseMapper implements Function<CampaignVo, CampaignResponse> {
    @Override
    public CampaignResponse apply(final CampaignVo campaignVo) {
        return CampaignResponse.builder()
                .categoryId(String.valueOf(campaignVo.getCategoryId()))
                .discount(String.valueOf(campaignVo.getDiscount()))
                .discountType(String.valueOf(campaignVo.getDiscountType()))
                .id(String.valueOf(campaignVo.getId()))
                .minimumBuyingRule(String.valueOf(campaignVo.getMinimumBuyingRule()))
                .build();
    }
}