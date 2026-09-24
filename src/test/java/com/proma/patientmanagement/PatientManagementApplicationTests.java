package com.proma.patientmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Smoke test: boots the full application context (security, JPA, H2,
 * data seeder). If the wiring is broken, this fails fast.
 */
@SpringBootTest
class PatientManagementApplicationTests {

    @Test
    void contextLoads() {
    }
}
