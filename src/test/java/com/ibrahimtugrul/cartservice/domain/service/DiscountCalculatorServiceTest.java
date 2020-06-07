package com.ibrahimtugrul.cartservice.domain.service;

import com.ibrahimtugrul.cartservice.domain.entity.CartItem;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignVo;
import com.ibrahimtugrul.cartservice.domain.vo.CartAddItemVo;
import com.ibrahimtugrul.cartservice.domain.vo.ProductVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DiscountCalculatorServiceTest {

    private DiscountCalculatorService discountCalculatorService;

    @Mock
    private ProductService productService;

    @Mock
    private CampaignService campaignService;

    @BeforeEach
    public void setup() {
        this.discountCalculatorService = new DiscountCalculatorService(productService, campaignService);
    }

    @Test
    public void should_return_calculated_cart_item_when_applicable_campaign_found_with_discount_type_is_rate() {
        // given
        final ProductVo productVo = ProductVo.builder()
                .categoryId(1L)
                .price(100.0)
                .title("product")
                .id(2L)
                .build();

        final CampaignVo campaignVoWithAmount = CampaignVo.builder()
                .categoryId(1L)
                .discount(25.0)
                .discountType(DiscountType.AMOUNT)
                .minimumBuyingRule(2)
                .id(2L)
                .build();

        final CampaignVo campaignVoWithRate = CampaignVo.builder()
                .categoryId(1L)
                .discount(35.0)
                .discountType(DiscountType.RATE)
                .minimumBuyingRule(2)
                .id(1L)
                .build();

        final CartItem cartItem = CartItem.builder()
                .productId(2L)
                .quantity(2L)
                .build();

        // when
        when(productService.retrieve(cartItem.getProductId())).thenReturn(productVo);
        when(campaignService.retrieveCampaignsByCategoryId(productVo.getCategoryId())).thenReturn(List.of(campaignVoWithAmount, campaignVoWithRate));

        final CartItem returnedCartItem = discountCalculatorService.createCartItemWithCampaign(cartItem);

        // then
        final InOrder inOrder = Mockito.inOrder(productService, campaignService);
        inOrder.verify(productService).retrieve(cartItem.getProductId());
        inOrder.verify(campaignService).retrieveCampaignsByCategoryId(productVo.getCategoryId());
        inOrder.verifyNoMoreInteractions();

        assertThat(returnedCartItem).isNotNull();
        assertThat(returnedCartItem.getProductId()).isEqualTo(productVo.getId());
        assertThat(returnedCartItem.getCategoryId()).isEqualTo(productVo.getCategoryId());
        assertThat(returnedCartItem.getAppliedCampaign()).isEqualTo(campaignVoWithRate.getId());
        assertThat(returnedCartItem.getQuantity()).isEqualTo(cartItem.getQuantity());
        assertThat(returnedCartItem.getTotalAmount()).isEqualTo(cartItem.getQuantity() * productVo.getPrice());
        assertThat(returnedCartItem.getTotalAmountAfterCampaign())
                .isEqualTo((cartItem.getQuantity() * productVo.getPrice()) - campaignVoWithRate.calculateCampaignDiscount(cartItem.getQuantity(), productVo.getPrice()));
    }

    @Test
    public void should_return_calculated_cart_item_when_applicable_campaign_found_with_discount_type_is_amount() {
        // given
        final ProductVo productVo = ProductVo.builder()
                .categoryId(1L)
                .price(100.0)
                .title("product")
                .id(2L)
                .build();

        final CampaignVo campaignVoWithAmount = CampaignVo.builder()
                .categoryId(1L)
                .discount(80.0)
                .discountType(DiscountType.AMOUNT)
                .minimumBuyingRule(2)
                .id(2L)
                .build();

        final CampaignVo campaignVoWithRate = CampaignVo.builder()
                .categoryId(1L)
                .discount(35.0)
                .discountType(DiscountType.RATE)
                .minimumBuyingRule(2)
                .id(1L)
                .build();

        final CartItem cartItem = CartItem.builder()
                .productId(2L)
                .quantity(2L)
                .build();

        // when
        when(productService.retrieve(cartItem.getProductId())).thenReturn(productVo);
        when(campaignService.retrieveCampaignsByCategoryId(productVo.getCategoryId())).thenReturn(List.of(campaignVoWithAmount, campaignVoWithRate));

        final CartItem returnedCartItem = discountCalculatorService.createCartItemWithCampaign(cartItem);

        // then
        final InOrder inOrder = Mockito.inOrder(productService, campaignService);
        inOrder.verify(productService).retrieve(cartItem.getProductId());
        inOrder.verify(campaignService).retrieveCampaignsByCategoryId(productVo.getCategoryId());
        inOrder.verifyNoMoreInteractions();

        assertThat(returnedCartItem).isNotNull();
        assertThat(returnedCartItem.getProductId()).isEqualTo(productVo.getId());
        assertThat(returnedCartItem.getCategoryId()).isEqualTo(productVo.getCategoryId());
        assertThat(returnedCartItem.getAppliedCampaign()).isEqualTo(campaignVoWithAmount.getId());
        assertThat(returnedCartItem.getQuantity()).isEqualTo(cartItem.getQuantity());
        assertThat(returnedCartItem.getTotalAmount()).isEqualTo(cartItem.getQuantity() * productVo.getPrice());
        assertThat(returnedCartItem.getTotalAmountAfterCampaign())
                .isEqualTo((cartItem.getQuantity() * productVo.getPrice()) - campaignVoWithAmount.calculateCampaignDiscount(cartItem.getQuantity(), productVo.getPrice()));
    }

    @Test
    public void should_return_calculated_cart_item_when_applicable_campaign_found_with_discount_type_is_rate_and_other_is_more_then_total_amount() {
        // given
        final ProductVo productVo = ProductVo.builder()
                .categoryId(1L)
                .price(100.0)
                .title("product")
                .id(2L)
                .build();

        final CampaignVo campaignVoWithAmount = CampaignVo.builder()
                .categoryId(1L)
                .discount(250.0)
                .discountType(DiscountType.AMOUNT)
                .minimumBuyingRule(2)
                .id(2L)
                .build();

        final CampaignVo campaignVoWithRate = CampaignVo.builder()
                .categoryId(1L)
                .discount(35.0)
                .discountType(DiscountType.RATE)
                .minimumBuyingRule(2)
                .id(1L)
                .build();

        final CartItem cartItem = CartItem.builder()
                .productId(2L)
                .quantity(2L)
                .build();

        // when
        when(productService.retrieve(cartItem.getProductId())).thenReturn(productVo);
        when(campaignService.retrieveCampaignsByCategoryId(productVo.getCategoryId())).thenReturn(List.of(campaignVoWithAmount, campaignVoWithRate));

        final CartItem returnedCartItem = discountCalculatorService.createCartItemWithCampaign(cartItem);

        // then
        final InOrder inOrder = Mockito.inOrder(productService, campaignService);
        inOrder.verify(productService).retrieve(cartItem.getProductId());
        inOrder.verify(campaignService).retrieveCampaignsByCategoryId(productVo.getCategoryId());
        inOrder.verifyNoMoreInteractions();

        assertThat(returnedCartItem).isNotNull();
        assertThat(returnedCartItem.getProductId()).isEqualTo(productVo.getId());
        assertThat(returnedCartItem.getCategoryId()).isEqualTo(productVo.getCategoryId());
        assertThat(returnedCartItem.getAppliedCampaign()).isEqualTo(campaignVoWithRate.getId());
        assertThat(returnedCartItem.getQuantity()).isEqualTo(cartItem.getQuantity());
        assertThat(returnedCartItem.getTotalAmount()).isEqualTo(cartItem.getQuantity() * productVo.getPrice());
        assertThat(returnedCartItem.getTotalAmountAfterCampaign())
                .isEqualTo((cartItem.getQuantity() * productVo.getPrice()) - campaignVoWithRate.calculateCampaignDiscount(cartItem.getQuantity(), productVo.getPrice()));
    }

    @Test
    public void should_return_calculated_cart_item_when_no_campaign_found() {
        // given
        final ProductVo productVo = ProductVo.builder()
                .categoryId(1L)
                .price(100.0)
                .title("product")
                .id(2L)
                .build();

        final CartItem cartItem = CartItem.builder()
                .productId(2L)
                .quantity(2L)
                .build();

        // when
        when(productService.retrieve(cartItem.getProductId())).thenReturn(productVo);
        when(campaignService.retrieveCampaignsByCategoryId(productVo.getCategoryId())).thenReturn(List.of());

        final CartItem returnedCartItem = discountCalculatorService.createCartItemWithCampaign(cartItem);

        // then
        final InOrder inOrder = Mockito.inOrder(productService, campaignService);
        inOrder.verify(productService).retrieve(cartItem.getProductId());
        inOrder.verify(campaignService).retrieveCampaignsByCategoryId(productVo.getCategoryId());
        inOrder.verifyNoMoreInteractions();

        assertThat(returnedCartItem).isNotNull();
        assertThat(returnedCartItem.getProductId()).isEqualTo(productVo.getId());
        assertThat(returnedCartItem.getQuantity()).isEqualTo(cartItem.getQuantity());
        assertThat(returnedCartItem.getTotalAmount()).isEqualTo(cartItem.getQuantity() * productVo.getPrice());
        assertThat(returnedCartItem.getTotalAmountAfterCampaign())
                .isEqualTo(cartItem.getQuantity() * productVo.getPrice());
    }

}