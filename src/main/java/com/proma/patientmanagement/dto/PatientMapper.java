package com.proma.patientmanagement.dto;

import com.proma.patientmanagement.model.Patient;
import org.springframework.stereotype.Component;

/**
 * Converts between the Patient entity and its request/response DTOs.
 * Isolating this mapping in one class keeps the service layer clean and
 * gives a single place to change if the API or the entity evolves.
 */
@Component
public class PatientMapper {

    /**
     * Build a new entity from a create request.
     */
    public Patient toEntity(PatientRequest request) {
        Patient patient = new Patient();
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setBloodGroup(request.getBloodGroup());
        return patient;
    }

    /**
     * Copy editable fields from a request onto an existing entity.
     */
    public void updateEntity(Patient patient, PatientRequest request) {
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setBloodGroup(request.getBloodGroup());
    }

    /**
     * Map an entity to the response returned by the API.
     */
    public PatientResponse toResponse(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getEmail(),
                patient.getPhone(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getBloodGroup(),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }
}
