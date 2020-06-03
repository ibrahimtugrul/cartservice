package com.ibrahimtugrul.cartservice.application.manager;

import com.ibrahimtugrul.cartservice.application.converter.CouponCreateRequestToVoConverter;
import com.ibrahimtugrul.cartservice.application.mapper.CouponVoToCouponResponseMapper;
import com.ibrahimtugrul.cartservice.application.model.request.CouponCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CouponResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import com.ibrahimtugrul.cartservice.domain.service.CouponService;
import com.ibrahimtugrul.cartservice.domain.vo.CouponCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.CouponVo;
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
public class CouponManagerTest {

    private CouponManager couponManager;

    @Mock
    private CouponCreateRequestToVoConverter couponCreateRequestToVoConverter;

    @Mock
    private CouponService couponService;

    @Mock
    private CouponVoToCouponResponseMapper couponVoToCouponResponseMapper;

    @BeforeEach
    public void setup() {
        this.couponManager = new CouponManager(couponService, couponCreateRequestToVoConverter, couponVoToCouponResponseMapper);
    }

    @Test
    public void should_return_created_coupon_id_when_coupon_create_request_is_valid() {
        // given
        final CouponCreateRequest couponCreateRequest = CouponCreateRequest.builder()
                .discount("15.0")
                .discountType("AMOUNT")
                .minimumAmount("3")
                .build();

        final CouponCreateVo couponCreateVo = CouponCreateVo.builder()
                .discount(15.0)
                .discountType(DiscountType.AMOUNT)
                .minimumAmount(3)
                .build();

        final long couponId = 1L;

        // when
        when(couponCreateRequestToVoConverter.convert(couponCreateRequest)).thenReturn(couponCreateVo);
        when(couponService.create(couponCreateVo)).thenReturn(couponId);

        final IdResponse responseEntity = couponManager.create(couponCreateRequest);

        // then
        final InOrder inOrder = Mockito.inOrder(couponCreateRequestToVoConverter, couponService);
        inOrder.verify(couponCreateRequestToVoConverter).convert(couponCreateRequest);
        inOrder.verify(couponService).create(couponCreateVo);
        inOrder.verifyNoMoreInteractions();

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getId()).isEqualTo(String.valueOf(couponId));
    }

    @Test
    public void should_return_coupon_list() {
        // given
        final CouponVo couponVo = CouponVo.builder()
                .discount(15.0)
                .discountType(DiscountType.RATE)
                .id(1L)
                .minimumAmount(3)
                .build();

        final CouponResponse couponResponse = CouponResponse.builder()
                .discount("15.0")
                .discountType("RATE")
                .id("1")
                .minimumAmount("3.0")
                .build();

        // when
        when(couponService.listAll()).thenReturn(List.of(couponVo));
        when(couponVoToCouponResponseMapper.apply(couponVo)).thenReturn(couponResponse);

        final List<CouponResponse> couponResponseList = couponManager.listAll();

        // then
        final InOrder inOrder = Mockito.inOrder(couponService, couponVoToCouponResponseMapper);
        inOrder.verify(couponService).listAll();
        inOrder.verify(couponVoToCouponResponseMapper).apply(couponVo);
        inOrder.verifyNoMoreInteractions();

        assertThat(couponResponseList).isNotEmpty();
        assertThat(couponResponseList.size()).isEqualTo(1);
        assertThat(couponResponseList.get(0).getMinimumAmount()).isEqualTo(String.valueOf(couponVo.getMinimumAmount()));
        assertThat(couponResponseList.get(0).getId()).isEqualTo(String.valueOf(couponVo.getId()));
        assertThat(couponResponseList.get(0).getDiscount()).isEqualTo(String.valueOf(couponVo.getDiscount()));
        assertThat(couponResponseList.get(0).getDiscountType()).isEqualTo(couponVo.getDiscountType().toString());
    }

    @Test
    public void should_return_coupon_when_coupon_found_byId() {
        // given
        final Long id = 1L;
        final CouponVo couponVo = CouponVo.builder()
                .discount(15.0)
                .discountType(DiscountType.RATE)
                .id(1L)
                .minimumAmount(3)
                .build();

        final CouponResponse couponResponse = CouponResponse.builder()
                .discount("15.0")
                .discountType("RATE")
                .id("1")
                .minimumAmount("3.0")
                .build();

        // when
        when(couponService.retrieve(id)).thenReturn(couponVo);
        when(couponVoToCouponResponseMapper.apply(couponVo)).thenReturn(couponResponse);

        final CouponResponse couponResponse1 = couponManager.retrieveCoupon(id);

        // then
        final InOrder inOrder = Mockito.inOrder(couponService, couponVoToCouponResponseMapper);
        inOrder.verify(couponService).retrieve(id);
        inOrder.verify(couponVoToCouponResponseMapper).apply(couponVo);
        inOrder.verifyNoMoreInteractions();

        assertThat(couponResponse1).isNotNull();
        assertThat(couponResponse1.getDiscountType()).isEqualTo(couponVo.getDiscountType().toString());
        assertThat(couponResponse1.getId()).isEqualTo(String.valueOf(couponVo.getId()));
        assertThat(couponResponse1.getDiscount()).isEqualTo(String.valueOf(couponVo.getDiscount()));
        assertThat(couponResponse1.getMinimumAmount()).isEqualTo(String.valueOf(couponVo.getMinimumAmount()));
    }

    @Test
    public void should_delete_coupon_when_coupon_found() {
        // given
        final Long id = 1L;

        // when
        couponManager.delete(id);

        // then
        final InOrder inOrder = Mockito.inOrder(couponService);
        inOrder.verify(couponService).delete(id);
        inOrder.verifyNoMoreInteractions();
    }
}