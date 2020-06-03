package com.ibrahimtugrul.cartservice.application.converter;

import com.ibrahimtugrul.cartservice.application.model.request.CampaignCreateRequest;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignCreateVo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CampaignCreateRequestToVoConverter implements Function<CampaignCreateRequest, CampaignCreateVo>, Converter<CampaignCreateRequest, CampaignCreateVo> {
    @Override
    public CampaignCreateVo apply(final CampaignCreateRequest campaignCreateRequest) {
        return CampaignCreateVo.builder()
                .categoryId(campaignCreateRequest.getCategoryId())
                .discount(campaignCreateRequest.getDiscount())
                .discountType(DiscountType.valueOf(campaignCreateRequest.getDiscountType().toUpperCase()))
                .minimumBuyingRule(campaignCreateRequest.getMinimumBuyingRule())
                .build();
    }

    @Override
    public CampaignCreateVo convert(final CampaignCreateRequest campaignCreateRequest) {
        return apply(campaignCreateRequest);
    }
}