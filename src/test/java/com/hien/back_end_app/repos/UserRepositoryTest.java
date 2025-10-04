package com.hien.back_end_app.repos;

import com.hien.back_end_app.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("test find by email")
    public void findByEmailTest() {
        var user = userRepository.findByEmail("hvu6582@gmail.com");
        Assertions.assertNotNull(user.get());
        Assertions.assertEquals("hvu6582@gmail.com", user.get().getEmail());
    }
}
