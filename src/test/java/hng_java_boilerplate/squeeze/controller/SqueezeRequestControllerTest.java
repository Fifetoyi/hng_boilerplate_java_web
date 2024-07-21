package hng_java_boilerplate.squeeze.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.squeeze.controller.SqueezeRequestController;
import hng_java_boilerplate.squeeze.entity.SqueezeRequest;
import hng_java_boilerplate.squeeze.exceptions.DuplicateEmailException;
import hng_java_boilerplate.squeeze.repository.SqueezeRequestRepository;
import hng_java_boilerplate.squeeze.service.SqueezeRequestService;
import hng_java_boilerplate.squeeze.util.ResponseMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SqueezeRequestController.class)
public class SqueezeRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SqueezeRequestService service;

    @Autowired
    private ObjectMapper objectMapper;

    private SqueezeRequest validRequest;

    @BeforeEach
    public void setUp() {
        validRequest = SqueezeRequest.builder()
                .email("testemail@email.com")
                .first_name("John")
                .last_name("Doe")
                .phone("08098761234")
                .location("Lagos, Nigeria")
                .job_title("Software Engineer")
                .company("X-Corp")
                .interests(new ArrayList<>(List.of("Web Development", "Cloud Computing")))
                .referral_source("LinkedIn")
                .build();
    }

    @Test
    public void testHandleSqueezeRequest_Success() throws Exception {
        when(service.saveSqueezeRequest(any(SqueezeRequest.class))).thenReturn(validRequest);

        mockMvc.perform(post("/api/v1/squeeze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Your request has been received. You will get a template shortly."));
    }

    @Test
    public void testHandleSqueezeRequest_DuplicateEmail() throws Exception {
        when(service.saveSqueezeRequest(any(SqueezeRequest.class))).thenThrow(new DuplicateEmailException("Email address already exists"));

        mockMvc.perform(post("/api/v1/squeeze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email address already exists"))
                .andExpect(jsonPath("$.status_code").value(409));
    }

}