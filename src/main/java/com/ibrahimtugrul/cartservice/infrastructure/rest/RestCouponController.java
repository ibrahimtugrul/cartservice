package com.ibrahimtugrul.cartservice.infrastructure.rest;

import com.ibrahimtugrul.cartservice.application.controller.CouponController;
import com.ibrahimtugrul.cartservice.application.manager.CouponManager;
import com.ibrahimtugrul.cartservice.application.model.request.CouponCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CouponResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/coupon")
@RequiredArgsConstructor
public class RestCouponController implements CouponController {

    private final CouponManager couponManager;

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdResponse> create(@Valid @RequestBody final CouponCreateRequest couponCreateRequest) {
        final IdResponse response = couponManager.create(couponCreateRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CouponResponse>> list() {
        final List<CouponResponse> couponResponseList = couponManager.listAll();
        return ResponseEntity.ok(couponResponseList);
    }

    @Override
    @GetMapping(value = "/{couponId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CouponResponse> retrieveCategory(@PathVariable("couponId") final Long couponId) {
        final CouponResponse couponResponse = couponManager.retrieveCoupon(couponId);
        return ResponseEntity.ok(couponResponse);
    }

    @Override
    @DeleteMapping("/{couponId}")
    public void delete(@PathVariable("couponId") final Long couponId) {
        couponManager.delete(couponId);
    }
}