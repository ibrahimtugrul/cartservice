package com.ibrahimtugrul.cartservice.infrastructure.rest;

import com.ibrahimtugrul.cartservice.application.manager.CampaignManager;
import com.ibrahimtugrul.cartservice.application.model.request.CampaignCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.infrastructure.rest.util.WebTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RestCampaignControllerTest {

    @Mock
    private CampaignManager campaignManager;

    private MockMvc mockMvc;

    private static final String CAMPAIGN_URL = "/cartservice/v1/campaign";
    private static final String ERR_MISSING_CAMPAIGN_RULE = "campaign.validation.required.minimumBuyingRule";
    private static final String ERR_MISSING_CAMPAIGN_CATEEGORY = "campaign.validation.required.category";
    private static final String ERR_MISSING_CAMPAIGN_DISOCOUNT = "campaign.validation.required.discount";
    private static final String ERR_MISSING_CAMPAIGN_DISCOUNT_TYPE = "campaign.validation.required.discountType";

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new RestCampaignController(campaignManager)).build();
    }

    @Test
    public void should_return_campaign_id_when_campaign_create_request_is_valid() throws Exception {
        // given
        final CampaignCreateRequest campaignCreateRequest = CampaignCreateRequest.builder()
                .categoryId(2L)
                .discount(15.0)
                .discountType("AMOUNT")
                .minimumBuyingRule(3)
                .build();

        final IdResponse campaignResponse = IdResponse.builder()
                .id(1L)
                .build();

        // when
        when(campaignManager.create(campaignCreateRequest)).thenReturn(campaignResponse);

        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(campaignCreateRequest)));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id", is(1)));
        verify(campaignManager, times(1)).create(campaignCreateRequest);
    }

    @Test
    public void should_return_bad_request_when_campaign_minimumBuyingRule_is_below_minimum() throws Exception {
        // given
        final CampaignCreateRequest campaignCreateRequest = CampaignCreateRequest.builder()
                .categoryId(2L)
                .discount(15.0)
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
                .discount(15.0)
                .discountType("AMOUNT")
                .minimumBuyingRule(1)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(campaignCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_CAMPAIGN_CATEEGORY)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_campaign_discount_is_not_valid() throws Exception {
        // given
        final CampaignCreateRequest campaignCreateRequest = CampaignCreateRequest.builder()
                .minimumBuyingRule(1)
                .discountType("AMOUNT")
                .minimumBuyingRule(1)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(campaignCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_CAMPAIGN_DISOCOUNT)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_campaign_discountType_is_not_valid() throws Exception {
        // given
        final CampaignCreateRequest campaignCreateRequest = CampaignCreateRequest.builder()
                .minimumBuyingRule(1)
                .discountType("INVALID")
                .discount(15.0)
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
}