package com.noyex.auth.service;

import com.noyex.auth.exceptions.*;

import com.noyex.data.dtos.LoginUserDto;
import com.noyex.data.dtos.RegisterUserDto;
import com.noyex.data.dtos.UpdateUserPasswordDto;
import com.noyex.data.enums.Role;
import com.noyex.data.model.User;

import com.noyex.data.repository.UserRepository;

import com.noyex.service.service.IEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService implements IAuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IEmailService emailService;

    @Value("${admin.email}")
    private String adminEmail;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            IEmailService emailService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public User signup(RegisterUserDto input) {
        Optional<User> existingUser = userRepository.findByUsername(input.getUsername());
        if (existingUser.isPresent()) {
            throw new UsernameAlreadyInUseException("Username already in use");
        }

        User user = new User(input.getUsername(), passwordEncoder.encode(input.getPassword()));
        user.setRole(Role.NOT_AUTHORIZED);
        user.setEnabled(true);
        emailService.sendUserRegistrationConfirmation(adminEmail, user.getUsername());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updatePassword(Long userId, UpdateUserPasswordDto input) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User authenticate(LoginUserDto input) {
        User user = userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new AccountNotVerifiedException("Account not enabled!");
        }
        if (user.getRole().equals(Role.NOT_AUTHORIZED)) {
            throw new AccountNotVerifiedException("Account not authorized!");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        input.getPassword()
                )
        );

        return user;
    }

}
