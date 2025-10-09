package com.example.blogsio.controller;

import com.example.blogsio.dto.RoleUpdateDto;
import com.example.blogsio.dto.UserDetailDto;
import com.example.blogsio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')") // Secures the entire controller for ADMINs only
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<UserDetailDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/role")
    public UserDetailDto updateUserRole(@PathVariable Long id, @RequestBody RoleUpdateDto roleUpdateDto) {
        return userService.updateUserRole(id, roleUpdateDto.getRole());
    }
}