package com.ibrahimtugrul.cartservice.domain.service;

import com.ibrahimtugrul.cartservice.domain.converter.CouponToVoConverter;
import com.ibrahimtugrul.cartservice.domain.entity.Coupon;
import com.ibrahimtugrul.cartservice.domain.exception.EntityNotFoundException;
import com.ibrahimtugrul.cartservice.domain.repository.CouponRepository;
import com.ibrahimtugrul.cartservice.domain.vo.CouponCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.CouponVo;
import com.ibrahimtugrul.cartservice.domain.vo.CouponCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.CouponVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponToVoConverter couponToVoConverter;

    public Long create(final CouponCreateVo couponCreateVo) {
        final Coupon coupon = Coupon.builder()
                .minimumAmount(couponCreateVo.getMinimumAmount())
                .discount(couponCreateVo.getDiscount())
                .discountType(couponCreateVo.getDiscountType())
                .build();

        couponRepository.save(coupon);
        return coupon.getId();
    }

    public List<CouponVo> listAll() {
        final List<Coupon> couponList = couponRepository.findAll();
        return couponList.stream().map(coupon -> couponToVoConverter.convert(coupon)).collect(Collectors.toList());

    }

    public CouponVo retrieve(final Long couponId) {
        final Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new EntityNotFoundException("coupon"));
        return couponToVoConverter.convert(coupon);
    }

    public void delete(final Long couponId) {
        final Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new EntityNotFoundException("coupon"));
        couponRepository.delete(coupon);
    }
}