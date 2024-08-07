package TMS.controllers;

import TMS.dto.auth.JwtAuthenticationResponse;
import TMS.dto.auth.SignInRequest;
import TMS.dto.auth.SignUpRequest;
import TMS.services.auth.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @MockBean
    private AuthenticationService authenticationService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(WebApplicationContext wac) {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testSignUp() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("admin@gmail.com")
                .password("1234567")
                .build();

        JwtAuthenticationResponse response = JwtAuthenticationResponse.builder()
                .token("mockedToken")
                .build();

        given(authenticationService.signUp(signUpRequest)).willReturn(response);

        mockMvc.perform(post("/api/v1/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockedToken"));
    }

    @Test
    public void testSignIn() throws Exception {
        SignInRequest signInRequest = SignInRequest.builder()
                .email("admin@gmail.com")
                .password("1234567")
                .build();

        JwtAuthenticationResponse response = JwtAuthenticationResponse.builder()
                .token("mockedToken")
                .build();

        given(authenticationService.signIn(signInRequest)).willReturn(response);

        mockMvc.perform(post("/api/v1/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockedToken"));
    }
}
