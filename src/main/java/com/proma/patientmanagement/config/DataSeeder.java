package com.proma.patientmanagement.config;

import com.proma.patientmanagement.model.Gender;
import com.proma.patientmanagement.model.Patient;
import com.proma.patientmanagement.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

/**
 * Seeds a couple of sample patients on startup so the API returns data
 * right away when someone tries it. Skipped in the "test" profile.
 */
@Configuration
@Profile("!test")
public class DataSeeder {

    @Bean
    CommandLineRunner seedPatients(PatientRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            Patient ayesha = new Patient();
            ayesha.setFirstName("Ayesha");
            ayesha.setLastName("Rahman");
            ayesha.setEmail("ayesha.rahman@example.com");
            ayesha.setPhone("+8801700000001");
            ayesha.setDateOfBirth(LocalDate.of(1995, 4, 12));
            ayesha.setGender(Gender.FEMALE);
            ayesha.setBloodGroup("B+");
            repository.save(ayesha);

            Patient karim = new Patient();
            karim.setFirstName("Karim");
            karim.setLastName("Hasan");
            karim.setEmail("karim.hasan@example.com");
            karim.setPhone("+8801700000002");
            karim.setDateOfBirth(LocalDate.of(1988, 9, 30));
            karim.setGender(Gender.MALE);
            karim.setBloodGroup("O+");
            repository.save(karim);
        };
    }
}
