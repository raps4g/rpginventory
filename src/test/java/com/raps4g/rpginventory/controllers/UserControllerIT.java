package com.raps4g.rpginventory.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raps4g.rpginventory.dto.LoginRequestDto;
import com.raps4g.rpginventory.dto.LoginResponseDto;
import com.raps4g.rpginventory.dto.RegisterRequestDto;
import com.raps4g.rpginventory.services.impl.JwtServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
   
    @Autowired
    private JwtServiceImpl jwtService;

    @Test
    public void UserController_UserRegistrationSuccessful() throws Exception {
        RegisterRequestDto requestDto = RegisterRequestDto.builder()
                                                        .username("coolusername")
                                                        .password("secretpassword")
                                                        .build();
        String requestJson = objectMapper.writeValueAsString(requestDto);
        mockMvc
            .perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    
    @Test
    public void UserController_UserRegistrationFailsOnDuplicatedUsername() throws Exception {
        RegisterRequestDto requestDto = RegisterRequestDto.builder()
                                                        .username("user")
                                                        .password("test2")
                                                        .build();
        String requestJson = objectMapper.writeValueAsString(requestDto);
        mockMvc
            .perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(MockMvcResultMatchers.status().isConflict())
            .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value("RESOURCE_ALREADY_EXISTS"));
    }

    @Test
    public void UserController_UserLoginSuccessful() throws Exception {
        LoginRequestDto requestDto = LoginRequestDto.builder()
                                                    .username("user")
                                                    .password("test")
                                                    .build();
        String requestJson = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc
            .perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();


        String responseJson = result.getResponse().getContentAsString();
        LoginResponseDto responseDto = objectMapper.readValue(responseJson, LoginResponseDto.class);
        Assertions.assertThat(responseDto.getToken()).isNotNull();
        Assertions.assertThat(jwtService.extractUsername(responseDto.getToken())).isEqualTo(requestDto.getUsername());
    }
    
    @Test
    public void UserController_UserLoginFails() throws Exception {
        LoginRequestDto requestDto = LoginRequestDto.builder()
                                                    .username("user")
                                                    .password("test2")
                                                    .build();
        String requestJson = objectMapper.writeValueAsString(requestDto);
        mockMvc
            .perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    
}
