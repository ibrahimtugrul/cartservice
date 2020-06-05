package com.ibrahimtugrul.cartservice.domain.repository;

import com.ibrahimtugrul.cartservice.domain.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}