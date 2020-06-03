package com.ibrahimtugrul.cartservice.application.manager;

import com.ibrahimtugrul.cartservice.application.converter.CampaignCreateRequestToVoConverter;
import com.ibrahimtugrul.cartservice.application.mapper.CampaignVoToCampaignResponseMapper;
import com.ibrahimtugrul.cartservice.application.model.request.CampaignCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CampaignResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.domain.service.CampaignService;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CampaignManager {

    private final CampaignService campaignService;
    private final CampaignCreateRequestToVoConverter campaignCreateRequestToVoConverter;
    private final CampaignVoToCampaignResponseMapper campaignVoToCampaignResponseMapper;

    public IdResponse create(final CampaignCreateRequest campaignCreateRequest) {
        final CampaignCreateVo campaignCreateVo = campaignCreateRequestToVoConverter.convert(campaignCreateRequest);
        final long campaignId = campaignService.create(campaignCreateVo);
        return IdResponse.builder().id(String.valueOf(campaignId)).build();
    }

    public List<CampaignResponse> listAll() {
        final List<CampaignVo> campaignVoList = campaignService.listAll();
        return campaignVoList.stream().map(campaignVo -> campaignVoToCampaignResponseMapper.apply(campaignVo)).collect(Collectors.toList());

    }

    public CampaignResponse retrieveCampaign(final Long campaignId) {
        final CampaignVo campaignVo = campaignService.retrieve(campaignId);
        return campaignVoToCampaignResponseMapper.apply(campaignVo);
    }

    public void delete(final Long campaignId) {
        campaignService.delete(campaignId);
    }
}