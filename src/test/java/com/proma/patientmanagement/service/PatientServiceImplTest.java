package com.proma.patientmanagement.service;

import com.proma.patientmanagement.dto.PatientMapper;
import com.proma.patientmanagement.dto.PatientRequest;
import com.proma.patientmanagement.dto.PatientResponse;
import com.proma.patientmanagement.exception.DuplicateResourceException;
import com.proma.patientmanagement.exception.ResourceNotFoundException;
import com.proma.patientmanagement.model.Gender;
import com.proma.patientmanagement.model.Patient;
import com.proma.patientmanagement.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the service layer. The repository and mapper are mocked
 * with Mockito, so these tests run without a database or Spring context.
 */
@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    private PatientRequest request;
    private Patient patient;
    private PatientResponse response;

    @BeforeEach
    void setUp() {
        request = new PatientRequest();
        request.setFirstName("Ayesha");
        request.setLastName("Rahman");
        request.setEmail("ayesha.rahman@example.com");
        request.setDateOfBirth(LocalDate.of(1995, 4, 12));
        request.setGender(Gender.FEMALE);

        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Ayesha");
        patient.setLastName("Rahman");
        patient.setEmail("ayesha.rahman@example.com");

        response = new PatientResponse(1L, "Ayesha", "Rahman",
                "ayesha.rahman@example.com", null,
                LocalDate.of(1995, 4, 12), Gender.FEMALE, null, null, null);
    }

    @Test
    void create_savesPatient_whenEmailIsNew() {
        when(patientRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(patientMapper.toEntity(request)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toResponse(patient)).thenReturn(response);

        PatientResponse result = patientService.create(request);

        assertThat(result).isEqualTo(response);
        verify(patientRepository).save(patient);
    }

    @Test
    void create_throwsDuplicate_whenEmailAlreadyExists() {
        when(patientRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> patientService.create(request))
                .isInstanceOf(DuplicateResourceException.class);

        verify(patientRepository, never()).save(any());
    }

    @Test
    void findById_returnsPatient_whenFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientMapper.toResponse(patient)).thenReturn(response);

        PatientResponse result = patientService.findById(1L);

        assertThat(result.email()).isEqualTo("ayesha.rahman@example.com");
    }

    @Test
    void findById_throwsNotFound_whenMissing() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findAll_returnsMappedList() {
        when(patientRepository.findAll()).thenReturn(List.of(patient));
        when(patientMapper.toResponse(patient)).thenReturn(response);

        List<PatientResponse> result = patientService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).firstName()).isEqualTo("Ayesha");
    }

    @Test
    void delete_removesPatient_whenFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        patientService.delete(1L);

        verify(patientRepository).delete(patient);
    }

    @Test
    void delete_throwsNotFound_whenMissing() {
        when(patientRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.delete(42L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(patientRepository, never()).delete(any());
    }
}
