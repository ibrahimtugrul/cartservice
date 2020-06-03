package com.ibrahimtugrul.cartservice.application.manager;

import com.ibrahimtugrul.cartservice.application.converter.CouponCreateRequestToVoConverter;
import com.ibrahimtugrul.cartservice.application.mapper.CouponVoToCouponResponseMapper;
import com.ibrahimtugrul.cartservice.application.model.request.CouponCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CouponResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.domain.service.CouponService;
import com.ibrahimtugrul.cartservice.domain.vo.CouponCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.CouponVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CouponManager {

    private final CouponService couponService;
    private final CouponCreateRequestToVoConverter couponCreateRequestToVoConverter;
    private final CouponVoToCouponResponseMapper couponVoToCouponResponseMapper;

    public IdResponse create(final CouponCreateRequest couponCreateRequest) {
        final CouponCreateVo couponCreateVo = couponCreateRequestToVoConverter.convert(couponCreateRequest);
        final long couponId = couponService.create(couponCreateVo);
        return IdResponse.builder().id(String.valueOf(couponId)).build();
    }

    public List<CouponResponse> listAll() {
        final List<CouponVo> couponVoList = couponService.listAll();
        return couponVoList.stream().map(couponVo -> couponVoToCouponResponseMapper.apply(couponVo)).collect(Collectors.toList());

    }

    public CouponResponse retrieveCoupon(final Long couponId) {
        final CouponVo couponVo = couponService.retrieve(couponId);
        return couponVoToCouponResponseMapper.apply(couponVo);
    }

    public void delete(final Long couponId) {
        couponService.delete(couponId);
    }
}