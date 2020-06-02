package com.ibrahimtugrul.cartservice.infrastructure.rest.integration;

import com.ibrahimtugrul.cartservice.CartserviceApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase()
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = CartserviceApplication.class)
@AutoConfigureMockMvc
public abstract class BaseWebIT {

    @Autowired
    protected MockMvc mockMvc;
}