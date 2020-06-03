package com.ibrahimtugrul.cartservice.domain.repository;

import com.ibrahimtugrul.cartservice.domain.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}