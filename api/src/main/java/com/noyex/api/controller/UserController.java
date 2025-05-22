package com.noyex.api.controller;

import com.noyex.auth.service.IAuthenticationService;
import com.noyex.auth.service.JwtService;
import com.noyex.data.dtos.RegisterUserDto;
import com.noyex.data.dtos.RoleDto;
import com.noyex.data.dtos.UpdateUserPasswordDto;
import com.noyex.data.dtos.UpdateUsernameDto;
import com.noyex.data.model.User;
import com.noyex.service.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class UserController {

    private final IUserService userService;
    private final JwtService jwtService;
    private final IAuthenticationService authenticationService;

    public UserController(IUserService userService, JwtService jwtService, IAuthenticationService authenticationService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update-username/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody UpdateUsernameDto updateUsernameDto) {
        userService.updateUsername(id, updateUsernameDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-password/{id}")
    public ResponseEntity<Void> updateUserPassword(@PathVariable Long id, @RequestBody UpdateUserPasswordDto updateUserPasswordDto) {
        authenticationService.updatePassword(id, updateUserPasswordDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/role/{id}")
    public ResponseEntity<Void> updateUserRole(@RequestBody RoleDto role, @PathVariable Long id) {
        userService.setUserRole(id, role.getRole());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<Void> enableUser(@PathVariable Long id) {
        userService.enableOrDisableUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody RegisterUserDto registerUserDto) {
        User user = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
