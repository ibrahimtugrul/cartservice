package com.ibrahimtugrul.cartservice.domain.service;

import com.ibrahimtugrul.cartservice.domain.entity.Cart;
import com.ibrahimtugrul.cartservice.domain.entity.CartItem;
import com.ibrahimtugrul.cartservice.domain.enums.IConstants;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class DeliveryCostCalculator {

    public double calculateDeliveryCost(final Cart cart) {
        final int numberOfProduct = cart.getItems().size();
        final int numberOfCategory = cart.getItems().stream().collect(Collectors.groupingBy(CartItem::getCategoryId)).size();
        return (numberOfCategory * IConstants.DELIVERY_COST) + (numberOfProduct * IConstants.PRODUCT_COST) + IConstants.FIXED_DELIVERY_COST;
    }
}