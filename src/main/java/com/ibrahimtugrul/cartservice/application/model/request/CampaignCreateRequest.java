package com.ibrahimtugrul.cartservice.application.model.request;

import com.ibrahimtugrul.cartservice.application.validator.aspect.ValidateEnum;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CampaignCreateRequest {
    @Min(value =1, message = "campaign.validation.required.minimumBuyingRule")
    private int minimumBuyingRule;
    @Min(value = 1, message = "campaign.validation.required.category")
    private long categoryId;
    @NotBlank(message = "campaign.validation.required.discount")
    private String discount;
    @ValidateEnum(enumClazz = DiscountType.class, message = "campaign.validation.required.discountType")
    private String discountType;
}