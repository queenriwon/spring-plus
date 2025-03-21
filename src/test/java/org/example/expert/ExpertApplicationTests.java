package org.example.expert;

import org.example.expert.config.JwtUtil;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserBulkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Import({JwtUtil.class})
class ExpertApplicationTests {

    @Autowired
    private UserBulkRepository userBulkRepository;

    @Test
    void 유저_데이터_백만건_생성() {

        List<User> batchList = new ArrayList<>();

        for (int i = 0; i < 1_000_000; i++) {
            User user = new User(i + "@email.com", "name" + i, "password", UserRole.ROLE_USER);
            batchList.add(user);

            if (batchList.size() == 1000) {
               userBulkRepository.bulkInsertUsers(batchList);
               batchList.clear();

                sleep(500);
            }
        }

        if (!batchList.isEmpty()) {
            userBulkRepository.bulkInsertUsers(batchList);
            batchList.clear();
        }
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}