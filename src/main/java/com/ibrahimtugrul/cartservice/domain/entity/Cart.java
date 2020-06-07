package com.ibrahimtugrul.cartservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue
    private Long id;
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();
    @Builder.Default
    private Long appliedCoupon = 0L;
    @Builder.Default
    private double totalAmount = 0.0;
    @Builder.Default
    private double totalAmountAfterDiscount = 0.0;
    /*@Builder.Default
    private double couponAmount = 0.0;
    @Builder.Default
    private double totalAmountAfterCoupon = 0.0;*/
}