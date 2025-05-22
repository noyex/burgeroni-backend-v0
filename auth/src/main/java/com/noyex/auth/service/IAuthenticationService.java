package com.noyex.auth.service;

import com.noyex.data.dtos.LoginUserDto;
import com.noyex.data.dtos.RegisterUserDto;
import com.noyex.data.dtos.UpdateUserPasswordDto;
import com.noyex.data.model.User;

public interface IAuthenticationService {
    User signup(RegisterUserDto input);
    User authenticate(LoginUserDto input);
    User updatePassword(Long userId, UpdateUserPasswordDto input);
}
