package com.noyex.service.service;

import com.noyex.data.dtos.UpdateUsernameDto;
import com.noyex.data.model.User;

import java.util.List;

public interface IUserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    // create jest w auth
    // update password tez
    void updateUsername(Long userId, UpdateUsernameDto updateUsernameDto);
    void setUserRole(Long userId, String role);
    void enableOrDisableUser(Long userId);
    void deleteUser(Long userId);
}
