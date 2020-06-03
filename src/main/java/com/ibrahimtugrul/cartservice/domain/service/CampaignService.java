package com.ibrahimtugrul.cartservice.domain.service;

import com.ibrahimtugrul.cartservice.domain.converter.CampaignToVoConverter;
import com.ibrahimtugrul.cartservice.domain.entity.Campaign;
import com.ibrahimtugrul.cartservice.domain.exception.EntityNotFoundException;
import com.ibrahimtugrul.cartservice.domain.repository.CampaignRepository;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignToVoConverter campaignToVoConverter;

    public Long create(final CampaignCreateVo campaignCreateVo) {
        final Campaign campaign = Campaign.builder()
                .minimumBuyingRule(campaignCreateVo.getMinimumBuyingRule())
                .categoryId(campaignCreateVo.getCategoryId())
                .discount(campaignCreateVo.getDiscount())
                .discountType(campaignCreateVo.getDiscountType())
                .build();

        campaignRepository.save(campaign);
        return campaign.getId();
    }

    public List<CampaignVo> listAll() {
        final List<Campaign> campaignList = campaignRepository.findAll();
        return campaignList.stream().map(campaign -> campaignToVoConverter.convert(campaign)).collect(Collectors.toList());

    }

    public CampaignVo retrieve(final Long campaignId) {
        final Campaign campaign = campaignRepository.findById(campaignId).orElseThrow(() -> new EntityNotFoundException("campaign"));
        return campaignToVoConverter.convert(campaign);
    }

    public void delete(final Long campaignId) {
        final Campaign campaign = campaignRepository.findById(campaignId).orElseThrow(() -> new EntityNotFoundException("campaign"));
        campaignRepository.delete(campaign);
    }
}