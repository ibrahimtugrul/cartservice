package com.ibrahimtugrul.cartservice.domain.converter;

import com.ibrahimtugrul.cartservice.domain.entity.Campaign;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignVo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CampaignToVoConverter implements Function<Campaign, CampaignVo>, Converter<Campaign, CampaignVo> {
    @Override
    public CampaignVo apply(final Campaign campaign) {
        return CampaignVo.builder()
                .minimumBuyingRule(campaign.getMinimumBuyingRule())
                .id(campaign.getId())
                .discountType(campaign.getDiscountType())
                .discount(campaign.getDiscount())
                .categoryId(campaign.getCategoryId())
                .build();
    }

    @Override
    public CampaignVo convert(final Campaign campaign) {
        return apply(campaign);
    }
}