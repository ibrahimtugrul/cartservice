package com.ibrahimtugrul.cartservice.infrastructure.rest;

import com.ibrahimtugrul.cartservice.application.manager.CampaignManager;
import com.ibrahimtugrul.cartservice.application.model.request.CampaignCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CampaignResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import com.ibrahimtugrul.cartservice.infrastructure.rest.util.WebTestUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RestCampaignControllerTest {

    @Mock
    private CampaignManager campaignManager;

    private MockMvc mockMvc;

    private static final String CAMPAIGN_URL = "/api/v1/campaign";
    private static final String ERR_MISSING_CAMPAIGN_RULE = "campaign.validation.required.minimumBuyingRule";
    private static final String ERR_MISSING_CAMPAIGN_CATEGORY = "campaign.validation.required.category";
    private static final String ERR_MISSING_CAMPAIGN_DISCOUNT = "campaign.validation.required.discount";
    private static final String ERR_MISSING_CAMPAIGN_DISCOUNT_TYPE = "campaign.validation.required.discountType";

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new RestCampaignController(campaignManager)).build();
    }

    @Test
    public void should_return_campaign_id_when_campaign_create_request_is_valid() throws Exception {
        // given
        final CampaignCreateRequest campaignCreateRequest = CampaignCreateRequest.builder()
                .categoryId(2)
                .discount("15.0")
                .discountType("AMOUNT")
                .minimumBuyingRule(3)
                .build();

        final IdResponse campaignResponse = IdResponse.builder()
                .id("1")
                .build();

        // when
        when(campaignManager.create(campaignCreateRequest)).thenReturn(campaignResponse);

        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(campaignCreateRequest)));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id", is("1")));
        verify(campaignManager, times(1)).create(campaignCreateRequest);
    }

    @Test
    public void should_return_bad_request_when_campaign_minimumBuyingRule_is_below_minimum() throws Exception {
        // given
        final CampaignCreateRequest campaignCreateRequest = CampaignCreateRequest.builder()
                .categoryId(2L)
                .discount("15.0")
                .discountType("AMOUNT")
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(campaignCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_CAMPAIGN_RULE)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_campaign_category_is_not_valid() throws Exception {
        // given
        final CampaignCreateRequest campaignCreateRequest = CampaignCreateRequest.builder()
                .discount("15.0")
                .discountType("AMOUNT")
                .minimumBuyingRule(1)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(campaignCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_CAMPAIGN_CATEGORY)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_campaign_discount_is_null() throws Exception {
        // given
        final CampaignCreateRequest campaignCreateRequest = CampaignCreateRequest.builder()
                .categoryId(1)
                .discountType("AMOUNT")
                .minimumBuyingRule(1)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(campaignCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_CAMPAIGN_DISCOUNT)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_campaign_discount_is_empty() throws Exception {
        // given
        final CampaignCreateRequest campaignCreateRequest = CampaignCreateRequest.builder()
                .categoryId(1)
                .discountType("AMOUNT")
                .minimumBuyingRule(1)
                .discount(StringUtils.EMPTY)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(campaignCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_CAMPAIGN_DISCOUNT)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_campaign_discount_is_blank() throws Exception {
        // given
        final CampaignCreateRequest campaignCreateRequest = CampaignCreateRequest.builder()
                .categoryId(1)
                .discountType("AMOUNT")
                .minimumBuyingRule(1)
                .discount(StringUtils.SPACE)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(campaignCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_CAMPAIGN_DISCOUNT)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_campaign_discountType_is_not_valid() throws Exception {
        // given
        final CampaignCreateRequest campaignCreateRequest = CampaignCreateRequest.builder()
                .minimumBuyingRule(1)
                .discountType("INVALID")
                .discount("15.0")
                .minimumBuyingRule(1)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(campaignCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_CAMPAIGN_DISCOUNT_TYPE)).isTrue();
    }

    @Test
    public void should_return_all_campaigns() throws Exception {
        // given
        final CampaignResponse campaignResponse = CampaignResponse.builder()
                .id("1")
                .minimumBuyingRule("1")
                .discountType("RATE")
                .categoryId("1")
                .build();

        final CampaignResponse campaignResponse1 = CampaignResponse.builder()
                .id("2")
                .minimumBuyingRule("3")
                .discountType("RATE")
                .categoryId("4")
                .build();

        // when
        when(campaignManager.listAll()).thenReturn(List.of(campaignResponse, campaignResponse1));

        final ResultActions resultActions =  mockMvc.perform(get(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$", hasSize(2)));
        resultActions.andExpect(jsonPath("$[0].id", is(campaignResponse.getId())));
        resultActions.andExpect(jsonPath("$[0].minimumBuyingRule", is(campaignResponse.getMinimumBuyingRule())));
        resultActions.andExpect(jsonPath("$[0].discountType", is(campaignResponse.getDiscountType())));
        resultActions.andExpect(jsonPath("$[0].categoryId", is(campaignResponse.getCategoryId())));
        resultActions.andExpect(jsonPath("$[1].id", is(campaignResponse1.getId())));
        resultActions.andExpect(jsonPath("$[1].minimumBuyingRule", is(campaignResponse1.getMinimumBuyingRule())));
        resultActions.andExpect(jsonPath("$[1].discountType", is(campaignResponse1.getDiscountType())));
        resultActions.andExpect(jsonPath("$[1].categoryId", is(String.valueOf(campaignResponse1.getCategoryId()))));
    }

    @Test
    public void should_return_campaign_when_campaign_found_with_id() throws Exception {
        // given
        final Long campaignId = 1L;

        final CampaignResponse campaignResponse = CampaignResponse.builder()
                .id("1")
                .minimumBuyingRule("1")
                .discountType("RATE")
                .categoryId("1")
                .build();

        // when
        when(campaignManager.retrieveCampaign(campaignId)).thenReturn(campaignResponse);

        final ResultActions resultActions =  mockMvc.perform(
                get(StringUtils.join(CAMPAIGN_URL, "/{campaignId}"), campaignId));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id", is(campaignResponse.getId())));
        resultActions.andExpect(jsonPath("$.minimumBuyingRule", is(campaignResponse.getMinimumBuyingRule())));
        resultActions.andExpect(jsonPath("$.discountType", is(campaignResponse.getDiscountType().toString())));
        resultActions.andExpect(jsonPath("$.categoryId", is(campaignResponse.getCategoryId())));

    }

    @Test
    public void should_delete_campaign_when_campaign_found_with_id() throws Exception {
        // given
        final Long campaignId = 1L;

        // when
        final ResultActions resultActions =  mockMvc.perform(
                delete(StringUtils.join(CAMPAIGN_URL, "/{campaignId}"), campaignId));

        // then
        resultActions.andExpect(status().isOk());
        verify(campaignManager, times(1)).delete(campaignId);
    }
}