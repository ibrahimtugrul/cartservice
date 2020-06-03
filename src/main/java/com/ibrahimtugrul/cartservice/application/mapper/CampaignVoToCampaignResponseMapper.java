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
                .categoryId(campaignVo.getCategoryId())
                .discount(campaignVo.getDiscount())
                .discountType(campaignVo.getDiscountType())
                .id(campaignVo.getId())
                .minimumBuyingRule(campaignVo.getMinimumBuyingRule())
                .build();
    }
}