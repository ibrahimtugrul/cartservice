package com.ibrahimtugrul.cartservice;

import com.ibrahimtugrul.cartservice.domain.entity.Product;
import com.ibrahimtugrul.cartservice.domain.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.Currency;

@SpringBootApplication
public class CartserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartserviceApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(ProductRepository productRepository) {
		return args -> {
			productRepository.save(Product.builder().categoryId(0L).price(25).title("init").build());
		};

	}
}