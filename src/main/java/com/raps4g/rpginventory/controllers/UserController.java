package com.raps4g.rpginventory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.raps4g.rpginventory.dto.LoginRequestDto;
import com.raps4g.rpginventory.dto.LoginResponseDto;
import com.raps4g.rpginventory.dto.RegisterRequestDto;
import com.raps4g.rpginventory.dto.UserResponseDto;
import com.raps4g.rpginventory.model.User;
import com.raps4g.rpginventory.services.JwtService;
import com.raps4g.rpginventory.services.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        User user = userService.mapFromRegisterRequestDto(registerRequestDto);
        user = userService.register(user);
        UserResponseDto userResponseDto = userService.mapToUserResponseDto(user);
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        User user = userService.mapFromLoginRequestDto(loginRequestDto);
        String token = userService.verify(user);
        LoginResponseDto loginResponse = new LoginResponseDto(token);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
    
    @GetMapping("/health")
    public ResponseEntity healthcheck() {
        return new ResponseEntity(HttpStatus.OK);
    }


}
