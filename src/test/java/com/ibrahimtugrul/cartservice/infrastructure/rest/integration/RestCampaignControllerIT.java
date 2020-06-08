package com.ibrahimtugrul.cartservice.infrastructure.rest.integration;

import com.ibrahimtugrul.cartservice.application.model.request.CampaignCreateRequest;
import com.ibrahimtugrul.cartservice.domain.entity.Campaign;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import com.ibrahimtugrul.cartservice.domain.repository.CampaignRepository;
import com.ibrahimtugrul.cartservice.infrastructure.rest.util.WebTestUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestCampaignControllerIT extends BaseWebIT {
    @Autowired
    private CampaignRepository campaignRepository;

    private static final String CATEGORY_URL = "/api/v1/campaign";

    @Test
    public void should_save_campaign() throws Exception {
        // given
        final CampaignCreateRequest campaignCreateRequest = CampaignCreateRequest.builder()
                .categoryId(2L)
                .discount("15.0")
                .discountType("AMOUNT")
                .minimumBuyingRule(3)
                .build();

        // when
        final ResultActions resultActions = mockMvc.perform(post(CATEGORY_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(campaignCreateRequest)));

        // then
        resultActions.andExpect(status().isOk());

        final List<Campaign> campaignList = campaignRepository.findAll();

        assertThat(campaignList).isNotEmpty();
        assertThat(campaignList.size()).isEqualTo(1);

        final Campaign savedCampaign = campaignList.get(0);

        assertThat(savedCampaign.getDiscount()).isEqualTo(Double.valueOf(campaignCreateRequest.getDiscount()));
        assertThat(savedCampaign.getDiscountType().toString()).isEqualTo(campaignCreateRequest.getDiscountType());
        assertThat(savedCampaign.getCategoryId()).isEqualTo(campaignCreateRequest.getCategoryId());
        assertThat(savedCampaign.getMinimumBuyingRule()).isEqualTo(campaignCreateRequest.getMinimumBuyingRule());

        resultActions.andExpect(jsonPath("$.id", is(String.valueOf(savedCampaign.getId()))));
    }

    @Test
    public void should_return_campaign_list() throws Exception {
        // given
        final Campaign campaign = Campaign.builder()
                .minimumBuyingRule(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .categoryId(1L)
                .build();

        campaignRepository.save(campaign);

        // when
        final ResultActions resultActions = mockMvc.perform(get(CATEGORY_URL)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$", hasSize(1)));
        resultActions.andExpect(jsonPath("$[0].id", is(String.valueOf(campaign.getId()))));
        resultActions.andExpect(jsonPath("$[0].categoryId", is(String.valueOf(campaign.getCategoryId()))));
        resultActions.andExpect(jsonPath("$[0].minimumBuyingRule", is(String.valueOf(campaign.getMinimumBuyingRule()))));
        resultActions.andExpect(jsonPath("$[0].discount", is(String.valueOf(campaign.getDiscount()))));
        resultActions.andExpect(jsonPath("$[0].discountType", is(campaign.getDiscountType().toString())));
    }

    @Test
    public void should_return_campaign_when_campaign_found() throws Exception {
        // given
        final Campaign campaign = Campaign.builder()
                .minimumBuyingRule(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .categoryId(1L)
                .build();

        campaignRepository.save(campaign);

        // when
        final ResultActions resultActions = mockMvc.perform(get(StringUtils.join(CATEGORY_URL, "/{campaignId}")
                , campaign.getId()));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id", is(String.valueOf(campaign.getId()))));
        resultActions.andExpect(jsonPath("$.categoryId", is(String.valueOf(campaign.getCategoryId()))));
        resultActions.andExpect(jsonPath("$.minimumBuyingRule", is(String.valueOf(campaign.getMinimumBuyingRule()))));
        resultActions.andExpect(jsonPath("$.discount", is(String.valueOf(campaign.getDiscount()))));
        resultActions.andExpect(jsonPath("$.discountType", is(campaign.getDiscountType().toString())));
    }

    @Test
    public void should_delete_campaign_when_campaign_found() throws  Exception {
        final Campaign campaign = Campaign.builder()
                .minimumBuyingRule(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .categoryId(1L)
                .build();

        campaignRepository.save(campaign);

        // when
        final ResultActions resultActions = mockMvc.perform(delete(StringUtils.join(CATEGORY_URL, "/{campaignId}")
                , campaign.getId()));

        // then
        resultActions.andExpect(status().isOk());
    }

    @AfterEach
    public void tearDown() {
        campaignRepository.deleteAll();
    }
}