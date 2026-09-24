package com.proma.patientmanagement.service;

import com.proma.patientmanagement.dto.PatientRequest;
import com.proma.patientmanagement.dto.PatientResponse;

import java.util.List;

/**
 * Business operations for patients. Coding to an interface keeps the
 * controller decoupled from the implementation and makes the service
 * easy to mock in tests.
 */
public interface PatientService {

    PatientResponse create(PatientRequest request);

    List<PatientResponse> findAll();

    PatientResponse findById(Long id);

    PatientResponse update(Long id, PatientRequest request);

    void delete(Long id);
}
