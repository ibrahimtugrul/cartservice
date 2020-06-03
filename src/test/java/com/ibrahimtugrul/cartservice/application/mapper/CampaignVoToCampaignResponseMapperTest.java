package com.ibrahimtugrul.cartservice.application.mapper;

import com.ibrahimtugrul.cartservice.application.model.response.CampaignResponse;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CampaignVoToCampaignResponseMapperTest {

    private CampaignVoToCampaignResponseMapper campaignVoToCampaignResponseMapper;

    @BeforeEach
    public void setup() {
        this.campaignVoToCampaignResponseMapper = new CampaignVoToCampaignResponseMapper();
    }

    @Test
    public void should_map() {
        // given
        final CampaignVo campaignVo = CampaignVo.builder()
                .categoryId(1L)
                .discount(15.0)
                .discountType(DiscountType.RATE)
                .id(1)
                .minimumBuyingRule(3)
                .build();

        // when
        final CampaignResponse campaignResponse = campaignVoToCampaignResponseMapper.apply(campaignVo);

        // then
        assertThat(campaignResponse).isNotNull();
        assertThat(campaignResponse.getDiscountType()).isEqualTo(campaignVo.getDiscountType());
        assertThat(campaignResponse.getDiscount()).isEqualTo(campaignVo.getDiscount());
        assertThat(campaignResponse.getCategoryId()).isEqualTo(campaignVo.getCategoryId());
        assertThat(campaignResponse.getId()).isEqualTo(campaignVo.getId());
        assertThat(campaignResponse.getMinimumBuyingRule()).isEqualTo(campaignVo.getMinimumBuyingRule());
    }
}