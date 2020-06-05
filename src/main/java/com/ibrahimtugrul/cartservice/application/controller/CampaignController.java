package com.ibrahimtugrul.cartservice.application.controller;

import com.ibrahimtugrul.cartservice.application.model.request.CampaignCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CampaignResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CampaignController {
    ResponseEntity<IdResponse> create(final CampaignCreateRequest campaignCreateRequest);
    ResponseEntity<List<CampaignResponse>> list();
    ResponseEntity<CampaignResponse> retrieveCampaign(final Long campaignId);
    void delete(final Long campaignId);
}