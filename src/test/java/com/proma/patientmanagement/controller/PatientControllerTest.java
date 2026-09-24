package com.proma.patientmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proma.patientmanagement.dto.PatientRequest;
import com.proma.patientmanagement.dto.PatientResponse;
import com.proma.patientmanagement.exception.ResourceNotFoundException;
import com.proma.patientmanagement.config.SecurityConfig;
import com.proma.patientmanagement.model.Gender;
import com.proma.patientmanagement.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Web-layer tests. @WebMvcTest loads only the controller and security,
 * with the service mocked, so these verify request mapping, validation,
 * status codes, and the JSON error body.
 */
@WebMvcTest(PatientController.class)
@Import(SecurityConfig.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;

    private PatientResponse sampleResponse() {
        return new PatientResponse(1L, "Ayesha", "Rahman",
                "ayesha.rahman@example.com", null,
                LocalDate.of(1995, 4, 12), Gender.FEMALE, "B+", null, null);
    }

    @Test
    void getAll_returnsOk() throws Exception {
        when(patientService.findAll()).thenReturn(List.of(sampleResponse()));

        mockMvc.perform(get("/api/v1/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Ayesha"));
    }

    @Test
    void getById_returnsOk() throws Exception {
        when(patientService.findById(1L)).thenReturn(sampleResponse());

        mockMvc.perform(get("/api/v1/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ayesha.rahman@example.com"));
    }

    @Test
    void getById_returnsNotFound_whenMissing() throws Exception {
        when(patientService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Patient not found with id 99"));

        mockMvc.perform(get("/api/v1/patients/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_returnsCreated_whenValid() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setFirstName("Karim");
        request.setLastName("Hasan");
        request.setEmail("karim.hasan@example.com");

        when(patientService.create(any())).thenReturn(sampleResponse());

        mockMvc.perform(post("/api/v1/patients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_returnsBadRequest_whenEmailInvalid() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setFirstName("Karim");
        request.setLastName("Hasan");
        request.setEmail("not-an-email");

        mockMvc.perform(post("/api/v1/patients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    @Test
    void create_returnsUnauthorized_whenNotAuthenticated() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setFirstName("Karim");
        request.setLastName("Hasan");
        request.setEmail("karim.hasan@example.com");

        mockMvc.perform(post("/api/v1/patients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
