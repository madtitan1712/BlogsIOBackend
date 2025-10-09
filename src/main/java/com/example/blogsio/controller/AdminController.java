package com.example.blogsio.controller;

import com.example.blogsio.dto.RoleUpdateDto;
import com.example.blogsio.dto.UserDetailDto;
import com.example.blogsio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<UserDetailDto> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
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