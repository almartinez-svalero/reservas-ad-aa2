package com.aa.reservas.controller;

import com.aa.reservas.config.JwtService;
import com.aa.reservas.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AuthControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean AuthenticationManager authenticationManager;
    @MockBean UserDetailsService userDetailsService;
    @MockBean JwtService jwtService;

    @Test
    void login_devuelveTokenJwt() throws Exception {
        when(userDetailsService.loadUserByUsername("admin")).thenReturn(User.withUsername("admin").password("password").roles("API_USER").build());
        when(jwtService.generateToken(any())).thenReturn("token-test");
        when(jwtService.getExpirationMinutes()).thenReturn(120L);

        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
{"username":"admin","password":"admin123"}
"""))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tokenType").value("Bearer"))
            .andExpect(jsonPath("$.accessToken").value("token-test"))
            .andExpect(jsonPath("$.expiresInMinutes").value(120));
    }

    @Test
    void login_devuelve400_siFaltanCredenciales() throws Exception {
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
{"username":"","password":""}
"""))
            .andExpect(status().isBadRequest());
    }
}
