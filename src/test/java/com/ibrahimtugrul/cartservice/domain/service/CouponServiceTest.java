package com.ibrahimtugrul.cartservice.domain.service;

import com.ibrahimtugrul.cartservice.domain.converter.CouponToVoConverter;
import com.ibrahimtugrul.cartservice.domain.entity.Coupon;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import com.ibrahimtugrul.cartservice.domain.exception.EntityNotFoundException;
import com.ibrahimtugrul.cartservice.domain.repository.CouponRepository;
import com.ibrahimtugrul.cartservice.domain.vo.CouponCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.CouponVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Captor
    private ArgumentCaptor<Coupon> couponArgumentCaptor;

    @Mock
    private CouponToVoConverter couponToVoConverter;

    private static final String ERR_ENTITY_NOT_FOUND = "domain.entity.notFound";

    @BeforeEach
    public void setup() {
        this.couponService = new CouponService(couponRepository, couponToVoConverter);
    }

    @Test
    public void should_save_coupon() {
        // given
        final CouponCreateVo couponCreateVo = CouponCreateVo.builder()
                .minimumAmount(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .build();

        // when
        when(couponRepository.save(any(Coupon.class))).thenAnswer(new Answer<Coupon>() {
            @Override
            public Coupon answer(InvocationOnMock invocation) {
                Coupon coupon = (Coupon) invocation.getArguments()[0];
                coupon.setId(1L);
                return coupon;
            }
        });

        final long couponId = couponService.create(couponCreateVo);

        // then
        couponArgumentCaptor = ArgumentCaptor.forClass(Coupon.class);

        final InOrder inOrder = Mockito.inOrder(couponRepository);
        inOrder.verify(couponRepository).save(couponArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(couponArgumentCaptor.getValue()).isNotNull();

        final Coupon savedCoupon = couponArgumentCaptor.getValue();

        assertThat(savedCoupon.getId()).isEqualTo(couponId);
        assertThat(savedCoupon.getDiscount()).isEqualTo(couponCreateVo.getDiscount());
        assertThat(savedCoupon.getDiscountType()).isEqualTo(couponCreateVo.getDiscountType());
        assertThat(savedCoupon.getMinimumAmount()).isEqualTo(couponCreateVo.getMinimumAmount());
    }

    @Test
    public void should_list_all_categories() {
        // given
        final Coupon coupon = Coupon.builder()
                .minimumAmount(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .id(1L)
                .build();

        final CouponVo couponVo = CouponVo.builder()
                .minimumAmount(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .id(1L)
                .build();

        // when
        when(couponRepository.findAll()).thenReturn(List.of(coupon));
        when(couponToVoConverter.convert(coupon)).thenReturn(couponVo);

        final List<CouponVo> couponVoList = couponService.listAll();

        // then
        couponArgumentCaptor = ArgumentCaptor.forClass(Coupon.class);

        final InOrder inOrder = Mockito.inOrder(couponRepository, couponToVoConverter);
        inOrder.verify(couponRepository).findAll();
        inOrder.verify(couponToVoConverter).convert(couponArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(couponVoList).isNotEmpty();
        assertThat(couponVoList.size()).isEqualTo(1);
        assertThat(couponVoList.get(0).getDiscountType()).isEqualTo(coupon.getDiscountType());
        assertThat(couponVoList.get(0).getId()).isEqualTo(coupon.getId());
        assertThat(couponVoList.get(0).getDiscount()).isEqualTo(coupon.getDiscount());
        assertThat(couponVoList.get(0).getMinimumAmount()).isEqualTo(coupon.getMinimumAmount());
    }

    @Test
    public void should_retrieve_coupon_when_coupon_found() {
        // given
        final Long id = 1L;
        final Coupon coupon = Coupon.builder()
                .minimumAmount(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .id(1L)
                .build();

        final CouponVo couponVo = CouponVo.builder()
                .minimumAmount(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .id(1L)
                .build();

        // when
        when(couponRepository.findById(id)).thenReturn(Optional.of(coupon));
        when(couponToVoConverter.convert(coupon)).thenReturn(couponVo);

        final CouponVo returnedCouponVo = couponService.retrieve(id);

        // then
        couponArgumentCaptor = ArgumentCaptor.forClass(Coupon.class);

        final InOrder inOrder = Mockito.inOrder(couponRepository, couponToVoConverter);
        inOrder.verify(couponRepository).findById(id);
        inOrder.verify(couponToVoConverter).convert(couponArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(returnedCouponVo.getId()).isEqualTo(coupon.getId());
        assertThat(returnedCouponVo.getMinimumAmount()).isEqualTo(coupon.getMinimumAmount());
        assertThat(returnedCouponVo.getDiscount()).isEqualTo(coupon.getDiscount());
        assertThat(returnedCouponVo.getDiscountType()).isEqualTo(coupon.getDiscountType());
    }

    @Test
    public void should_throw_exception_when_coupon_not_found() {
        // given
        final Long id = 1L;

        // when
        when(couponRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        final Throwable throwable = catchThrowable(()->{couponService.retrieve(id);});

        // then
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(EntityNotFoundException.class);

        final EntityNotFoundException entityNotFoundException = (EntityNotFoundException) throwable;
        assertThat(entityNotFoundException.getLocalizedMessage().contains(ERR_ENTITY_NOT_FOUND)).isTrue();
    }

    @Test
    public void should_delete_coupon_when_coupon_found() {
        // given
        final Long id = 1L;
        final Coupon coupon = Coupon.builder()
                .minimumAmount(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .id(1L)
                .build();

        // when
        when(couponRepository.findById(id)).thenReturn(Optional.of(coupon));

        couponService.delete(id);

        // then
        couponArgumentCaptor = ArgumentCaptor.forClass(Coupon.class);

        final InOrder inOrder = Mockito.inOrder(couponRepository);
        inOrder.verify(couponRepository).findById(id);
        inOrder.verify(couponRepository).delete(couponArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(couponArgumentCaptor.getValue()).isNotNull();

        final Coupon foundedCoupon = couponArgumentCaptor.getValue();

        assertThat(foundedCoupon.getMinimumAmount()).isEqualTo(coupon.getMinimumAmount());
        assertThat(foundedCoupon.getId()).isEqualTo(coupon.getId());
        assertThat(foundedCoupon.getDiscountType()).isEqualTo(coupon.getDiscountType());
        assertThat(foundedCoupon.getDiscount()).isEqualTo(coupon.getDiscount());
    }

    @Test
    public void should_throw_exception_when_delete_coupon_not_found() {
        // given
        final Long id = 1L;

        // when
        when(couponRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        final Throwable throwable = catchThrowable(()->{couponService.delete(id);});

        // then
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(EntityNotFoundException.class);

        final EntityNotFoundException entityNotFoundException = (EntityNotFoundException) throwable;
        assertThat(entityNotFoundException.getLocalizedMessage().contains(ERR_ENTITY_NOT_FOUND)).isTrue();
    }
}