package com.ibrahimtugrul.cartservice.domain.service;

import com.ibrahimtugrul.cartservice.domain.entity.CartItem;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignVo;
import com.ibrahimtugrul.cartservice.domain.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountCalculatorService {

    private final ProductService productService;
    private final CampaignService campaignService;

    public CartItem createCartItemWithCampaign(final CartItem cartItem) {

        final ProductVo productVo = productService.retrieve(cartItem.getProductId());
        final List<CampaignVo> campaignVoList = campaignService.retrieveCampaignsByCategoryId(productVo.getCategoryId()).stream().filter(campaignVo -> cartItem.getQuantity()>=campaignVo.getMinimumBuyingRule()).collect(Collectors.toList());;

        if (campaignVoList.isEmpty()) {
            return CartItem.builder()
                    .quantity(cartItem.getQuantity())
                    .totalAmount(cartItem.getQuantity() * productVo.getPrice())
                    .totalAmountAfterCampaign(cartItem.getQuantity() * productVo.getPrice())
                    .productId(productVo.getId())
                    .build();
        } else {
            final Long foundedCampaignId = findMaximumDiscount(campaignVoList, cartItem.getQuantity(), productVo.getPrice());
            final CampaignVo campaignVo = campaignVoList.stream().filter(campaignVo1 -> foundedCampaignId.equals(campaignVo1.getId())).findFirst().get();

            return CartItem.builder()
                    .quantity(cartItem.getQuantity())
                    .appliedCampaign(campaignVo.getId())
                    .totalAmount(cartItem.getQuantity() * productVo.getPrice())
                    .totalAmountAfterCampaign((cartItem.getQuantity() * productVo.getPrice()) - campaignVo.calculateCampaignDiscount(cartItem.getQuantity(), productVo.getPrice()))
                    .categoryId(campaignVo.getCategoryId())
                    .productId(productVo.getId())
                    .build();
        }
    }

    private Long findMaximumDiscount(final List<CampaignVo> campaignVoList, final Long quantity, final double productPrice) {
        double maximumDiscount = 0.0;
        Long campaignId = 0L;
        for (CampaignVo item : campaignVoList) {
            double discount = item.calculateCampaignDiscount(quantity, productPrice);
            maximumDiscount = Double.compare(maximumDiscount, discount) > 0 ? maximumDiscount : discount;
            campaignId =  Double.compare(maximumDiscount, discount) > 0 ? campaignId : item.getId();
        }
        return campaignId;
    }
}