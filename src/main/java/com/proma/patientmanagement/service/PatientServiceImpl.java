package com.proma.patientmanagement.service;

import com.proma.patientmanagement.dto.PatientMapper;
import com.proma.patientmanagement.dto.PatientRequest;
import com.proma.patientmanagement.dto.PatientResponse;
import com.proma.patientmanagement.exception.DuplicateResourceException;
import com.proma.patientmanagement.exception.ResourceNotFoundException;
import com.proma.patientmanagement.model.Patient;
import com.proma.patientmanagement.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Default implementation of {@link PatientService}. Handles the business
 * rules (unique email, existence checks) and delegates persistence to the
 * repository. Dependencies are injected through the constructor.
 */
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientServiceImpl(PatientRepository patientRepository,
                              PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    @Override
    @Transactional
    public PatientResponse create(PatientRequest request) {
        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "A patient with email " + request.getEmail() + " already exists");
        }
        Patient patient = patientMapper.toEntity(request);
        Patient saved = patientRepository.save(patient);
        return patientMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientResponse> findAll() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse findById(Long id) {
        Patient patient = getPatientOrThrow(id);
        return patientMapper.toResponse(patient);
    }

    @Override
    @Transactional
    public PatientResponse update(Long id, PatientRequest request) {
        Patient patient = getPatientOrThrow(id);

        // Block reusing another patient's email.
        patientRepository.findByEmail(request.getEmail())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "A patient with email " + request.getEmail() + " already exists");
                });

        patientMapper.updateEntity(patient, request);
        Patient saved = patientRepository.save(patient);
        return patientMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Patient patient = getPatientOrThrow(id);
        patientRepository.delete(patient);
    }

    private Patient getPatientOrThrow(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with id " + id));
    }
}
