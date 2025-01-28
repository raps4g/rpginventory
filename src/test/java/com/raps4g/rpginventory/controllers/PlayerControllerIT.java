package com.raps4g.rpginventory.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raps4g.rpginventory.dto.PlayerDto;
import com.raps4g.rpginventory.dto.RegisterRequestDto;
import com.raps4g.rpginventory.services.impl.JwtServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PlayerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
   
    @Autowired
    private JwtServiceImpl jwtService;

    private String token;

    @BeforeEach
    public void setToken() {
        String username = "user";
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        token = jwtService.generateToken(username, authorities);
        UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Test
    public void PlayerContreller_CreatePlayer() throws Exception {

        PlayerDto playerDto = PlayerDto.builder().name("coolplayername").build();
        String requestJson = objectMapper.writeValueAsString(playerDto);

        MvcResult result = mockMvc
            .perform(MockMvcRequestBuilders.post("/players")
            .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();
        
        String responseJson = result.getResponse().getContentAsString();
        PlayerDto responseDto = objectMapper.readValue(responseJson, PlayerDto.class);
        Assertions.assertThat(responseDto.getId()).isNotNull();
        Assertions.assertThat(responseDto.getName()).isEqualTo("coolplayername");
        Assertions.assertThat(responseDto.getGold()).isEqualTo(1000L);
    }
    
    @Test
    public void PlayerContreller_GetPlayer() throws Exception {

        PlayerDto playerDto = PlayerDto.builder().name("coolplayername").build();
        String requestJson = objectMapper.writeValueAsString(playerDto);

        mockMvc
            .perform(MockMvcRequestBuilders.post("/players")
            .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        MvcResult result = mockMvc
            .perform(MockMvcRequestBuilders.get("/players/2")
            .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        
        String responseJson = result.getResponse().getContentAsString();
        PlayerDto responseDto = objectMapper.readValue(responseJson, PlayerDto.class);
        Assertions.assertThat(responseDto.getId()).isNotNull();
        Assertions.assertThat(responseDto.getName()).isEqualTo("coolplayername");
        Assertions.assertThat(responseDto.getGold()).isEqualTo(1000L);
    }
    
    
}
