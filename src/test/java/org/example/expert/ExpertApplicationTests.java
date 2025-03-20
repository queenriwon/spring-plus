package org.example.expert;

import org.example.expert.config.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import({JwtUtil.class})
@SpringBootTest
class ExpertApplicationTests {

    @Test
    void contextLoads() {
    }

}