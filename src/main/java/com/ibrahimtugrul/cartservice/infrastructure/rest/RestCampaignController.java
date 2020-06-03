package com.ibrahimtugrul.cartservice.infrastructure.rest;

import com.ibrahimtugrul.cartservice.application.controller.CampaignController;
import com.ibrahimtugrul.cartservice.application.manager.CampaignManager;
import com.ibrahimtugrul.cartservice.application.model.request.CampaignCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CampaignResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/campaign")
@RequiredArgsConstructor
public class RestCampaignController implements CampaignController {

    private final CampaignManager campaignManager;

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdResponse> create(@Valid @RequestBody  final CampaignCreateRequest campaignCreateRequest) {
        final IdResponse response = campaignManager.create(campaignCreateRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CampaignResponse>> list() {
        final List<CampaignResponse> campaignResponseList = campaignManager.listAll();
        return ResponseEntity.ok(campaignResponseList);
    }

    @Override
    @GetMapping(value = "/{campaignId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CampaignResponse> retrieveCategory(@PathVariable("campaignId") final Long campaignId) {
        final CampaignResponse campaignResponse = campaignManager.retrieveCampaign(campaignId);
        return ResponseEntity.ok(campaignResponse);
    }

    @Override
    @DeleteMapping("/{campaignId}")
    public void delete(@PathVariable("campaignId") final Long campaignId) {
        campaignManager.delete(campaignId);
    }
}