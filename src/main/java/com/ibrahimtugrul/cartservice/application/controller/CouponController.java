package com.ibrahimtugrul.cartservice.application.controller;

import com.ibrahimtugrul.cartservice.application.model.request.CouponCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CouponResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CouponController {
    ResponseEntity<IdResponse> create(final CouponCreateRequest couponCreateRequest);
    ResponseEntity<List<CouponResponse>> list();
    ResponseEntity<CouponResponse> retrieveCategory(final Long couponId);
    void delete(final Long couponId);
}