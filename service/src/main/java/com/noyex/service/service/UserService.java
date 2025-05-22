package com.noyex.service.service;

import com.noyex.data.dtos.UpdateUserPasswordDto;
import com.noyex.data.dtos.UpdateUsernameDto;
import com.noyex.data.enums.Role;
import com.noyex.data.model.User;
import com.noyex.data.repository.UserRepository;
import com.noyex.service.exceptions.InvalidRoleException;
import com.noyex.service.exceptions.UserNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public void updateUsername(Long userId, UpdateUsernameDto updateUsernameDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setUsername(updateUsernameDto.getUsername());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void setUserRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        switch (role) {
            case "RESTAURACJA" -> user.setRole(Role.RESTAURANT);
            case "DOSTAWCA" -> user.setRole(Role.DELIVERY);
            case "NIEAUTORYZOWANY" -> user.setRole(Role.NOT_AUTHORIZED);
            default -> throw new InvalidRoleException("Invalid role: " + role);
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void enableOrDisableUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if(user.isEnabled()){
            user.setEnabled(false);
            userRepository.save(user);
        }
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }
}
