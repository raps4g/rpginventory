package com.raps4g.rpginventory.services;

import com.raps4g.rpginventory.dto.LoginRequestDto;
import com.raps4g.rpginventory.dto.RegisterRequestDto;
import com.raps4g.rpginventory.dto.UserResponseDto;
import com.raps4g.rpginventory.model.User;

public interface UserService {

    UserResponseDto mapToUserResponseDto(User user);

    User mapFromLoginRequestDto(LoginRequestDto loginRequestDto);

    User mapFromRegisterRequestDto(RegisterRequestDto registerRequestDto);

    User register(User user);

    String verify(User user);
}
