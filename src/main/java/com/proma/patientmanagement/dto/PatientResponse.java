package com.proma.patientmanagement.dto;

import com.proma.patientmanagement.model.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Outgoing representation of a patient. Using a response DTO keeps JPA
 * entities out of the web layer and lets the API shape stay stable even
 * if the database model changes.
 */
public record PatientResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDate dateOfBirth,
        Gender gender,
        String bloodGroup,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
