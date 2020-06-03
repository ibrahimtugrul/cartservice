package com.ibrahimtugrul.cartservice.domain.converter;

import com.ibrahimtugrul.cartservice.domain.entity.Campaign;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CampaignToVoConverterTest {

    private CampaignToVoConverter campaignToVoConverter;

    @BeforeEach
    public void setup() {
        this.campaignToVoConverter = new CampaignToVoConverter();
    }

    @Test
    public void should_convert() {
        // given
        final Campaign campaign = Campaign.builder()
                .minimumBuyingRule(1)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .categoryId(1L)
                .id(1L)
                .build();

        // when
        final CampaignVo campaignVo = campaignToVoConverter.convert(campaign);

        // then
        assertThat(campaignVo).isNotNull();
        assertThat(campaignVo.getCategoryId()).isEqualTo(campaign.getCategoryId());
        assertThat(campaignVo.getId()).isEqualTo(campaign.getId());
        assertThat(campaignVo.getDiscount()).isEqualTo(campaign.getDiscount());
        assertThat(campaignVo.getMinimumBuyingRule()).isEqualTo(campaign.getMinimumBuyingRule());
        assertThat(campaignVo.getDiscountType()).isEqualTo(campaign.getDiscountType());
    }
}