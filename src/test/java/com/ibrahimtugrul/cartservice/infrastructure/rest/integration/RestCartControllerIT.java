package com.ibrahimtugrul.cartservice.infrastructure.rest.integration;

import com.ibrahimtugrul.cartservice.application.model.request.CartAddItemRequest;
import com.ibrahimtugrul.cartservice.domain.entity.*;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import com.ibrahimtugrul.cartservice.domain.repository.CampaignRepository;
import com.ibrahimtugrul.cartservice.domain.repository.CartRepository;
import com.ibrahimtugrul.cartservice.domain.repository.CouponRepository;
import com.ibrahimtugrul.cartservice.domain.repository.ProductRepository;
import com.ibrahimtugrul.cartservice.domain.vo.CampaignVo;
import com.ibrahimtugrul.cartservice.domain.vo.ProductVo;
import com.ibrahimtugrul.cartservice.infrastructure.rest.util.WebTestUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestCartControllerIT extends BaseWebIT {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponRepository couponRepository;

    private static final String CART_URL = "/api/v1/cart";

    @BeforeEach
    public void setup() {// to delete initial values for integration test health
        cartRepository.deleteAll();
        productRepository.deleteAll();
        campaignRepository.deleteAll();
    }

    @Test
    public void should_save_cart() throws Exception {
        // given

        // when
        final ResultActions resultActions = mockMvc.perform(post(CART_URL));

        // then
        resultActions.andExpect(status().isOk());

        final List<Cart> cartList = cartRepository.findAll();

        assertThat(cartList).isNotEmpty();
        assertThat(cartList.size()).isEqualTo(1);

        final Cart savedCart = cartList.get(0);

        assertThat(savedCart.getAppliedCoupon()).isEqualTo(0L);
        assertThat(savedCart.getTotalAmount()).isEqualTo(0.0);
        assertThat(savedCart.getTotalAmountAfterDiscount()).isEqualTo(0.0);
        assertThat(savedCart.getItems()).isEmpty();

        resultActions.andExpect(jsonPath("$.id", is(String.valueOf(savedCart.getId()))));
    }

    @Test
    public void should_return_cart_list() throws Exception {
        // given
        final CartItem cartItem = CartItem.builder()
                .appliedCampaign(2L)
                .categoryId(3L)
                .productId(1L)
                .quantity(4L)
                .totalAmount(100.0)
                .totalAmountAfterCampaign(75.0)
                .build();

        final Cart cart = Cart.builder()
                .appliedCoupon(2L)
                .items(List.of(cartItem))
                .totalAmount(100.0)
                .totalAmountAfterDiscount(75.0)
                .couponAmount(15.0)
                .totalAmountAfterCoupon(60.0)
                .build();

        cartRepository.save(cart);

        // when
        final ResultActions resultActions = mockMvc.perform(get(CART_URL)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$", hasSize(1)));
        resultActions.andExpect(jsonPath("$[0].id", is(String.valueOf(cart.getId()))));
        resultActions.andExpect(jsonPath("$[0].appliedCoupon", is(String.valueOf(cart.getAppliedCoupon()))));
        resultActions.andExpect(jsonPath("$[0].totalAmount", is(String.valueOf(cart.getTotalAmount()))));
        resultActions.andExpect(jsonPath("$[0].totalAmountAfterDiscount", is(String.valueOf(cart.getTotalAmountAfterDiscount()))));
        resultActions.andExpect(jsonPath("$[0].couponAmount", is(String.valueOf(cart.getCouponAmount()))));
        resultActions.andExpect(jsonPath("$[0].totalAmountAfterCoupon", is(String.valueOf(cart.getTotalAmountAfterCoupon()))));
        resultActions.andExpect(jsonPath("$[0].deliveryCost", is(String.valueOf(7.99))));
        resultActions.andExpect(jsonPath("$[0].items", hasSize(1)));
        resultActions.andExpect(jsonPath("$[0].items[0].appliedCampaign", is((String.valueOf(cart.getItems().get(0).getAppliedCampaign())))));
        resultActions.andExpect(jsonPath("$[0].items[0].categoryId", is((String.valueOf(cart.getItems().get(0).getCategoryId())))));
        resultActions.andExpect(jsonPath("$[0].items[0].productId", is((String.valueOf(cart.getItems().get(0).getProductId())))));
        resultActions.andExpect(jsonPath("$[0].items[0].quantity", is((String.valueOf(cart.getItems().get(0).getQuantity())))));
        resultActions.andExpect(jsonPath("$[0].items[0].totalAmount", is((String.valueOf(cart.getItems().get(0).getTotalAmount())))));
        resultActions.andExpect(jsonPath("$[0].items[0].totalAmountAfterCampaign", is((String.valueOf(cart.getItems().get(0).getTotalAmountAfterCampaign())))));
    }

    @Test
    public void should_return_cart_when_cart_found() throws Exception {
        // given
        final CartItem cartItem = CartItem.builder()
                .appliedCampaign(2L)
                .categoryId(3L)
                .productId(1L)
                .quantity(4L)
                .totalAmount(100.0)
                .totalAmountAfterCampaign(75.0)
                .build();

        final Cart cart = Cart.builder()
                .appliedCoupon(2L)
                .items(List.of(cartItem))
                .totalAmount(100.0)
                .totalAmountAfterDiscount(75.0)
                .couponAmount(15.0)
                .totalAmountAfterCoupon(60.0)
                .build();

        cartRepository.save(cart);

        // when
        final ResultActions resultActions = mockMvc.perform(get(StringUtils.join(CART_URL, "/{cartId}")
                , cart.getId()));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id", is(String.valueOf(cart.getId()))));
        resultActions.andExpect(jsonPath("$.appliedCoupon", is(String.valueOf(cart.getAppliedCoupon()))));
        resultActions.andExpect(jsonPath("$.totalAmount", is(String.valueOf(cart.getTotalAmount()))));
        resultActions.andExpect(jsonPath("$.totalAmountAfterDiscount", is(String.valueOf(cart.getTotalAmountAfterDiscount()))));
        resultActions.andExpect(jsonPath("$.couponAmount", is(String.valueOf(cart.getCouponAmount()))));
        resultActions.andExpect(jsonPath("$.totalAmountAfterCoupon", is(String.valueOf(cart.getTotalAmountAfterCoupon()))));
        resultActions.andExpect(jsonPath("$.deliveryCost", is(String.valueOf(7.99))));
        resultActions.andExpect(jsonPath("$.items", hasSize(1)));
        resultActions.andExpect(jsonPath("$.items[0].appliedCampaign", is((String.valueOf(cart.getItems().get(0).getAppliedCampaign())))));
        resultActions.andExpect(jsonPath("$.items[0].categoryId", is((String.valueOf(cart.getItems().get(0).getCategoryId())))));
        resultActions.andExpect(jsonPath("$.items[0].productId", is((String.valueOf(cart.getItems().get(0).getProductId())))));
        resultActions.andExpect(jsonPath("$.items[0].quantity", is((String.valueOf(cart.getItems().get(0).getQuantity())))));
        resultActions.andExpect(jsonPath("$.items[0].totalAmount", is((String.valueOf(cart.getItems().get(0).getTotalAmount())))));
        resultActions.andExpect(jsonPath("$.items[0].totalAmountAfterCampaign", is((String.valueOf(cart.getItems().get(0).getTotalAmountAfterCampaign())))));

    }

    @Test
    public void should_delete_cart_when_cart_found() throws  Exception {
        // given
        final CartItem cartItem = CartItem.builder()
                .appliedCampaign(2L)
                .categoryId(3L)
                .productId(1L)
                .quantity(4L)
                .totalAmount(100.0)
                .totalAmountAfterCampaign(75.0)
                .build();

        final Cart cart = Cart.builder()
                .appliedCoupon(2L)
                .items(List.of(cartItem))
                .totalAmount(100.0)
                .totalAmountAfterDiscount(75.0)
                .build();

        cartRepository.save(cart);

        // when
        final ResultActions resultActions = mockMvc.perform(delete(StringUtils.join(CART_URL, "/{cartId}")
                , cart.getId()));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void should_add_item_when_no_cart_item_found() throws Exception {
        // given
        final Product product = Product.builder()
                .categoryId(1L)
                .price(100.0)
                .title("product")
                .build();

        productRepository.save(product);

        final Campaign campaignWithAmount = Campaign.builder()
                .categoryId(1L)
                .discount(25.0)
                .discountType(DiscountType.AMOUNT)
                .minimumBuyingRule(2)
                .build();

        campaignRepository.save(campaignWithAmount);

        final Campaign campaignVoWithRate = Campaign.builder()
                .categoryId(1L)
                .discount(35.0)
                .discountType(DiscountType.RATE)
                .minimumBuyingRule(2)
                .build();

        campaignRepository.save(campaignVoWithRate);

        final Cart cart = Cart.builder()
                .appliedCoupon(2L)
                .totalAmount(100.0)
                .totalAmountAfterDiscount(75.0)
                .build();

        cartRepository.save(cart);

        final CartAddItemRequest cartAddItemRequest = CartAddItemRequest.builder()
                .quantity("2")
                .productId(String.valueOf(product.getId()))
                .build();

        // when
        final ResultActions resultActions = mockMvc.perform(put(CART_URL + "/{cartId}/item", cart.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(WebTestUtil.convertObjectToJsonBytes(cartAddItemRequest)));

        resultActions.andExpect(status().isOk());

        final List<Cart> savedCartList = cartRepository.findAll();
        assertThat(savedCartList).isNotEmpty();
        assertThat(savedCartList.size()).isEqualTo(1);

        final Cart savedCart = savedCartList.get(0);
        assertThat(savedCart.getItems()).isNotEmpty();
        assertThat(savedCart.getItems().size()).isEqualTo(1);

        final CartItem savedCartItem = savedCart.getItems().get(0);

        assertThat(savedCartItem).isNotNull();
        assertThat(savedCartItem.getProductId()).isEqualTo(product.getId());
        assertThat(savedCartItem.getQuantity()).isEqualTo(Long.valueOf(cartAddItemRequest.getQuantity()));
        assertThat(savedCartItem.getCategoryId()).isEqualTo(campaignVoWithRate.getCategoryId());
        assertThat(savedCartItem.getAppliedCampaign()).isEqualTo(campaignVoWithRate.getId());
        assertThat(savedCartItem.getTotalAmount()).isEqualTo(Long.valueOf(cartAddItemRequest.getQuantity()) * product.getPrice());
        assertThat(savedCartItem.getTotalAmountAfterCampaign()).isEqualTo(130.0);
    }

    @Test
    public void should_apply_coupon_when_cart_and_coupon_found() throws Exception {
        // given
        final CartItem cartItem = CartItem.builder()
                .appliedCampaign(2L)
                .categoryId(3L)
                .productId(1L)
                .quantity(4L)
                .totalAmount(100.0)
                .totalAmountAfterCampaign(75.0)
                .build();

        final Cart cart = Cart.builder()
                .appliedCoupon(2L)
                .items(List.of(cartItem))
                .totalAmount(100.0)
                .totalAmountAfterDiscount(75.0)
                .build();

        cartRepository.save(cart);

        final Coupon coupon = Coupon.builder()
                .discount(15)
                .discountType(DiscountType.AMOUNT)
                .minimumAmount(50)
                .build();

        couponRepository.save(coupon);

        // when
        final ResultActions resultActions = mockMvc.perform(put(CART_URL + "/{cartId}/coupon/{couponId}", cart.getId(), coupon.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        final List<Cart> savedCartList = cartRepository.findAll();
        assertThat(savedCartList).isNotEmpty();
        assertThat(savedCartList.size()).isEqualTo(1);

        final Cart savedCart = savedCartList.get(0);
        assertThat(savedCart.getItems()).isNotEmpty();
        assertThat(savedCart.getItems().size()).isEqualTo(1);

        assertThat(savedCart.getAppliedCoupon()).isEqualTo(coupon.getId());
        assertThat(savedCart.getCouponAmount()).isEqualTo(coupon.getDiscount());
        assertThat(savedCart.getTotalAmountAfterCoupon()).isEqualTo(60.0);
    }

    @AfterEach
    public void tearDown() {
        cartRepository.deleteAll();
        productRepository.deleteAll();
        campaignRepository.deleteAll();
        couponRepository.deleteAll();
    }
}