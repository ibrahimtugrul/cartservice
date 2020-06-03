package com.ibrahimtugrul.cartservice.application.manager;

import com.ibrahimtugrul.cartservice.application.converter.CampaignCreateRequestToVoConverter;
import com.ibrahimtugrul.cartservice.application.mapper.CampaignVoToCampaignResponseMapper;
import com.ibrahimtugrul.cartservice.application.model.request.CampaignCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CampaignResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import com.ibrahimtugrul.cartservice.domain.service.CampaignService;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CampaignManagerTest {
    
    private CampaignManager campaignManager;

    @Mock
    private CampaignCreateRequestToVoConverter campaignCreateRequestToVoConverter;

    @Mock
    private CampaignService campaignService;

    @Mock
    private CampaignVoToCampaignResponseMapper campaignVoToCampaignResponseMapper;

    @BeforeEach
    public void setup() {
        this.campaignManager = new CampaignManager(campaignService, campaignCreateRequestToVoConverter, campaignVoToCampaignResponseMapper);
    }

    @Test
    public void should_return_created_campaign_id_when_campaign_create_request_is_valid() {
        // given
        final CampaignCreateRequest campaignCreateRequest = CampaignCreateRequest.builder()
                .categoryId(2L)
                .discount(15.0)
                .discountType("AMOUNT")
                .minimumBuyingRule(3)
                .build();

        final CampaignCreateVo campaignCreateVo = CampaignCreateVo.builder()
                .categoryId(2L)
                .discount(15.0)
                .discountType(DiscountType.AMOUNT)
                .minimumBuyingRule(3)
                .build();

        final long campaignId = 1L;

        // when
        when(campaignCreateRequestToVoConverter.convert(campaignCreateRequest)).thenReturn(campaignCreateVo);
        when(campaignService.create(campaignCreateVo)).thenReturn(campaignId);

        final IdResponse responseEntity = campaignManager.create(campaignCreateRequest);

        // then
        final InOrder inOrder = Mockito.inOrder(campaignCreateRequestToVoConverter, campaignService);
        inOrder.verify(campaignCreateRequestToVoConverter).convert(campaignCreateRequest);
        inOrder.verify(campaignService).create(campaignCreateVo);
        inOrder.verifyNoMoreInteractions();

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getId()).isEqualTo(campaignId);
    }

    @Test
    public void should_return_campaign_list() {
        // given
        final CampaignVo campaignVo = CampaignVo.builder()
                .categoryId(1L)
                .discount(15.0)
                .discountType(DiscountType.RATE)
                .id(1)
                .minimumBuyingRule(3)
                .build();

        final CampaignResponse campaignResponse = CampaignResponse.builder()
                .categoryId(1L)
                .discount(15.0)
                .discountType(DiscountType.RATE)
                .id(1)
                .minimumBuyingRule(3)
                .build();

        // when
        when(campaignService.listAll()).thenReturn(List.of(campaignVo));
        when(campaignVoToCampaignResponseMapper.apply(campaignVo)).thenReturn(campaignResponse);

        final List<CampaignResponse> campaignResponseList = campaignManager.listAll();

        // then
        final InOrder inOrder = Mockito.inOrder(campaignService, campaignVoToCampaignResponseMapper);
        inOrder.verify(campaignService).listAll();
        inOrder.verify(campaignVoToCampaignResponseMapper).apply(campaignVo);
        inOrder.verifyNoMoreInteractions();

        assertThat(campaignResponseList).isNotEmpty();
        assertThat(campaignResponseList.size()).isEqualTo(1);
        assertThat(campaignResponseList.get(0).getMinimumBuyingRule()).isEqualTo(campaignVo.getMinimumBuyingRule());
        assertThat(campaignResponseList.get(0).getId()).isEqualTo(campaignVo.getId());
        assertThat(campaignResponseList.get(0).getCategoryId()).isEqualTo(campaignVo.getCategoryId());
        assertThat(campaignResponseList.get(0).getDiscount()).isEqualTo(campaignVo.getDiscount());
        assertThat(campaignResponseList.get(0).getDiscountType()).isEqualTo(campaignVo.getDiscountType());
    }

    @Test
    public void should_return_campaign_when_campaign_found_byId() {
        // given
        final Long id = 1L;
        final CampaignVo campaignVo = CampaignVo.builder()
                .categoryId(1L)
                .discount(15.0)
                .discountType(DiscountType.RATE)
                .id(1)
                .minimumBuyingRule(3)
                .build();

        final CampaignResponse campaignResponse = CampaignResponse.builder()
                .categoryId(1L)
                .discount(15.0)
                .discountType(DiscountType.RATE)
                .id(1)
                .minimumBuyingRule(3)
                .build();

        // when
        when(campaignService.retrieve(id)).thenReturn(campaignVo);
        when(campaignVoToCampaignResponseMapper.apply(campaignVo)).thenReturn(campaignResponse);

        final CampaignResponse campaignResponse1 = campaignManager.retrieveCampaign(id);

        // then
        final InOrder inOrder = Mockito.inOrder(campaignService, campaignVoToCampaignResponseMapper);
        inOrder.verify(campaignService).retrieve(id);
        inOrder.verify(campaignVoToCampaignResponseMapper).apply(campaignVo);
        inOrder.verifyNoMoreInteractions();

        assertThat(campaignResponse1).isNotNull();
        assertThat(campaignResponse1.getDiscountType()).isEqualTo(campaignVo.getDiscountType());
        assertThat(campaignResponse1.getId()).isEqualTo(campaignVo.getId());
        assertThat(campaignResponse1.getDiscount()).isEqualTo(campaignVo.getDiscount());
        assertThat(campaignResponse1.getCategoryId()).isEqualTo(campaignVo.getCategoryId());
        assertThat(campaignResponse1.getMinimumBuyingRule()).isEqualTo(campaignVo.getMinimumBuyingRule());
    }

    @Test
    public void should_delete_campaign_when_campaign_found() {
        // given
        final Long id = 1L;

        // when
        campaignManager.delete(id);

        // then
        final InOrder inOrder = Mockito.inOrder(campaignService);
        inOrder.verify(campaignService).delete(id);
        inOrder.verifyNoMoreInteractions();
    }
}