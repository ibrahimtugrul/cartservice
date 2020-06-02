package com.ibrahimtugrul.cartservice.domain.repository;

import com.ibrahimtugrul.cartservice.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}