package com.ibrahimtugrul.cartservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private Long productId;
    private Long quantity;
    private Long categoryId;
    private Long appliedCampaign;
    private double totalAmount;
    private double totalAmountAfterCampaign;
}