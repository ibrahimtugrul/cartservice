package com.ibrahimtugrul.cartservice.domain.service;

import com.ibrahimtugrul.cartservice.domain.converter.CampaignToVoConverter;
import com.ibrahimtugrul.cartservice.domain.entity.Campaign;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import com.ibrahimtugrul.cartservice.domain.exception.EntityNotFoundException;
import com.ibrahimtugrul.cartservice.domain.repository.CampaignRepository;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CampaignServiceTest {
    private CampaignService campaignService;

    @Mock
    private CampaignRepository campaignRepository;

    @Captor
    private ArgumentCaptor<Campaign> campaignArgumentCaptor;

    @Mock
    private CampaignToVoConverter campaignToVoConverter;

    private static final String ERR_ENTITY_NOT_FOUND = "domain.entity.notFound";

    @BeforeEach
    public void setup() {
        this.campaignService = new CampaignService(campaignRepository, campaignToVoConverter);
    }

    @Test
    public void should_save_campaign() {
        // given
        final CampaignCreateVo campaignCreateVo = CampaignCreateVo.builder()
                .minimumBuyingRule(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .categoryId(1)
                .build();

        // when
        when(campaignRepository.save(any(Campaign.class))).thenAnswer(new Answer<Campaign>() {
            @Override
            public Campaign answer(InvocationOnMock invocation) {
                Campaign campaign = (Campaign) invocation.getArguments()[0];
                campaign.setId(1L);
                return campaign;
            }
        });

        final long campaignId = campaignService.create(campaignCreateVo);

        // then
        campaignArgumentCaptor = ArgumentCaptor.forClass(Campaign.class);

        final InOrder inOrder = Mockito.inOrder(campaignRepository);
        inOrder.verify(campaignRepository).save(campaignArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(campaignArgumentCaptor.getValue()).isNotNull();

        final Campaign savedCampaign = campaignArgumentCaptor.getValue();

        assertThat(savedCampaign.getId()).isEqualTo(campaignId);
        assertThat(savedCampaign.getCategoryId()).isEqualTo(campaignCreateVo.getCategoryId());
        assertThat(savedCampaign.getDiscount()).isEqualTo(campaignCreateVo.getDiscount());
        assertThat(savedCampaign.getDiscountType()).isEqualTo(campaignCreateVo.getDiscountType());
        assertThat(savedCampaign.getMinimumBuyingRule()).isEqualTo(campaignCreateVo.getMinimumBuyingRule());
    }

    @Test
    public void should_list_all_categories() {
        // given
        final Campaign campaign = Campaign.builder()
                .minimumBuyingRule(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .categoryId(1L)
                .id(1L)
                .build();

        final CampaignVo campaignVo = CampaignVo.builder()
                .minimumBuyingRule(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .categoryId(1)
                .id(1L)
                .build();

        // when
        when(campaignRepository.findAll()).thenReturn(List.of(campaign));
        when(campaignToVoConverter.convert(campaign)).thenReturn(campaignVo);

        final List<CampaignVo> campaignVoList = campaignService.listAll();

        // then
        campaignArgumentCaptor = ArgumentCaptor.forClass(Campaign.class);

        final InOrder inOrder = Mockito.inOrder(campaignRepository, campaignToVoConverter);
        inOrder.verify(campaignRepository).findAll();
        inOrder.verify(campaignToVoConverter).convert(campaignArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(campaignVoList).isNotEmpty();
        assertThat(campaignVoList.size()).isEqualTo(1);
        assertThat(campaignVoList.get(0).getDiscountType()).isEqualTo(campaign.getDiscountType());
        assertThat(campaignVoList.get(0).getId()).isEqualTo(campaign.getId());
        assertThat(campaignVoList.get(0).getDiscount()).isEqualTo(campaign.getDiscount());
        assertThat(campaignVoList.get(0).getMinimumBuyingRule()).isEqualTo(campaign.getMinimumBuyingRule());
        assertThat(campaignVoList.get(0).getCategoryId()).isEqualTo(campaign.getCategoryId());
    }

    @Test
    public void should_retrieve_campaign_when_campaign_found() {
        // given
        final Long id = 1L;
        final Campaign campaign = Campaign.builder()
                .minimumBuyingRule(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .categoryId(1L)
                .id(1L)
                .build();

        final CampaignVo campaignVo = CampaignVo.builder()
                .minimumBuyingRule(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .categoryId(1)
                .id(1L)
                .build();

        // when
        when(campaignRepository.findById(id)).thenReturn(Optional.of(campaign));
        when(campaignToVoConverter.convert(campaign)).thenReturn(campaignVo);

        final CampaignVo returnedCampaignVo = campaignService.retrieve(id);

        // then
        campaignArgumentCaptor = ArgumentCaptor.forClass(Campaign.class);

        final InOrder inOrder = Mockito.inOrder(campaignRepository, campaignToVoConverter);
        inOrder.verify(campaignRepository).findById(id);
        inOrder.verify(campaignToVoConverter).convert(campaignArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(returnedCampaignVo.getCategoryId()).isEqualTo(campaign.getCategoryId());
        assertThat(returnedCampaignVo.getId()).isEqualTo(campaign.getId());
        assertThat(returnedCampaignVo.getMinimumBuyingRule()).isEqualTo(campaign.getMinimumBuyingRule());
        assertThat(returnedCampaignVo.getDiscount()).isEqualTo(campaign.getDiscount());
        assertThat(returnedCampaignVo.getDiscountType()).isEqualTo(campaign.getDiscountType());
    }

    @Test
    public void should_throw_exception_when_campaign_not_found() {
        // given
        final Long id = 1L;

        // when
        when(campaignRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        final Throwable throwable = catchThrowable(()->{campaignService.retrieve(id);});

        // then
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(EntityNotFoundException.class);

        final EntityNotFoundException entityNotFoundException = (EntityNotFoundException) throwable;
        assertThat(entityNotFoundException.getLocalizedMessage().contains(ERR_ENTITY_NOT_FOUND)).isTrue();
    }

    @Test
    public void should_delete_campaign_when_campaign_found() {
        // given
        final Long id = 1L;
        final Campaign campaign = Campaign.builder()
                .minimumBuyingRule(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .categoryId(1L)
                .id(1L)
                .build();

        // when
        when(campaignRepository.findById(id)).thenReturn(Optional.of(campaign));

        campaignService.delete(id);

        // then
        campaignArgumentCaptor = ArgumentCaptor.forClass(Campaign.class);

        final InOrder inOrder = Mockito.inOrder(campaignRepository);
        inOrder.verify(campaignRepository).findById(id);
        inOrder.verify(campaignRepository).delete(campaignArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(campaignArgumentCaptor.getValue()).isNotNull();

        final Campaign foundedCampaign = campaignArgumentCaptor.getValue();

        assertThat(foundedCampaign.getMinimumBuyingRule()).isEqualTo(campaign.getMinimumBuyingRule());
        assertThat(foundedCampaign.getId()).isEqualTo(campaign.getId());
        assertThat(foundedCampaign.getCategoryId()).isEqualTo(campaign.getCategoryId());
        assertThat(foundedCampaign.getDiscountType()).isEqualTo(campaign.getDiscountType());
        assertThat(foundedCampaign.getDiscount()).isEqualTo(campaign.getDiscount());
    }

    @Test
    public void should_throw_exception_when_delete_campaign_not_found() {
        // given
        final Long id = 1L;

        // when
        when(campaignRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        final Throwable throwable = catchThrowable(()->{campaignService.delete(id);});

        // then
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(EntityNotFoundException.class);

        final EntityNotFoundException entityNotFoundException = (EntityNotFoundException) throwable;
        assertThat(entityNotFoundException.getLocalizedMessage().contains(ERR_ENTITY_NOT_FOUND)).isTrue();
    }
}