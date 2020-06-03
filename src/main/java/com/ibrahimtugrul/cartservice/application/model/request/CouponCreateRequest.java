package com.ibrahimtugrul.cartservice.application.model.request;

import com.ibrahimtugrul.cartservice.application.validator.aspect.ValidateEnum;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponCreateRequest {
    @NotBlank(message = "coupon.validation.required.minimumAmount")
    private String minimumAmount;
    @NotBlank(message = "coupon.validation.required.discount")
    private String discount;
    @ValidateEnum(enumClazz = DiscountType.class, message = "coupon.validation.required.discountType")
    private String discountType;
}