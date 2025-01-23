package com.raps4g.rpginventory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.raps4g.rpginventory.dto.LoginRequest;
import com.raps4g.rpginventory.dto.LoginResponse;
import com.raps4g.rpginventory.dto.RegisterRequest;
import com.raps4g.rpginventory.dto.UserResponse;
import com.raps4g.rpginventory.model.User;
import com.raps4g.rpginventory.services.JwtService;
import com.raps4g.rpginventory.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequestDto) {
        User user = userService.mapFromRegisterRequestDto(registerRequestDto);
        user = userService.register(user);
        UserResponse userResponseDto = userService.mapToUserResponseDto(user);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequestDto) {
        User user = userService.mapFromLoginRequestDto(loginRequestDto);
        String token = userService.verify(user);
        LoginResponse loginResponse = new LoginResponse(token);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

}
