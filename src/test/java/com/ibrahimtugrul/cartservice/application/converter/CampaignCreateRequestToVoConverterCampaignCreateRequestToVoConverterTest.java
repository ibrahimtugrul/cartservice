package com.ibrahimtugrul.cartservice.application.converter;

import com.ibrahimtugrul.cartservice.application.model.request.CampaignCreateRequest;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

public class CampaignCreateRequestToVoConverterCampaignCreateRequestToVoConverterTest {

    private CampaignCreateRequestToVoConverter campaignCreateRequestToVoConverter;

    @BeforeEach
    public void setup() {
        this.campaignCreateRequestToVoConverter = new CampaignCreateRequestToVoConverter();
    }

    @Test
    public void should_convert() {
        // given
        final CampaignCreateRequest campaignCreateRequest = CampaignCreateRequest.builder()
                .minimumBuyingRule(3)
                .discountType("amount")
                .discount("15.0")
                .categoryId(2)
                .build();

        // when
        final CampaignCreateVo campaignCreateVo = campaignCreateRequestToVoConverter.convert(campaignCreateRequest);

        // then
        assertThat(campaignCreateVo).isNotNull();
        assertThat(campaignCreateVo.getDiscountType().toString()).isEqualTo(campaignCreateRequest.getDiscountType().toUpperCase());
        assertThat(campaignCreateVo.getDiscount()).isEqualTo(Double.valueOf(campaignCreateRequest.getDiscount()));
        assertThat(campaignCreateVo.getCategoryId()).isEqualTo(campaignCreateRequest.getCategoryId());
        assertThat(campaignCreateVo.getMinimumBuyingRule()).isEqualTo(campaignCreateRequest.getMinimumBuyingRule());
    }
}