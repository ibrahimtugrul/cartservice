package com.ibrahimtugrul.cartservice.domain.entity;

import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue
    private Long id;
    private double minimumAmount;
    private double discount;
    private DiscountType discountType;
}