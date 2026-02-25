package com.example.ExpenseTracker;

import com.example.ExpenseTracker.service.ImageStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ExpenseTrackerApplicationTests {

    @MockBean
    private ImageStorageService imageStorageService; // ✅ mock for tests

    @Test
    void contextLoads() {
    }
}
