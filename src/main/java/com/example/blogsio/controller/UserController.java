package com.example.blogsio.controller;

import com.example.blogsio.dto.PasswordChangeDto;
import com.example.blogsio.dto.UserDetailDto;
import com.example.blogsio.dto.UserProfileDto;
import com.example.blogsio.entity.UserEntity;
import com.example.blogsio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService myservice;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserEntity> getallusers() {
        return myservice.getAll();
    }

    @GetMapping("/getbyid/{id}")
    public UserEntity getUserbyId(@PathVariable long id) {
        return myservice.getByID(id);
    }

    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean deletebyID(@PathVariable long id) {
        return myservice.deleteUserByID(id);
    }

    @PutMapping("/update/{id}")
    public UserEntity update(@PathVariable("id") long id, @RequestBody UserEntity current) {
        return myservice.updateUser(id, current);
    }

    @PostMapping("/createuser")
    @PreAuthorize("hasRole('ADMIN')")
    public UserEntity createuser(@RequestBody UserEntity user) {
        return myservice.createUser(user);
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public UserDetailDto getUserProfile(Principal principal) {
        return myservice.getUserProfile(principal);
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public UserDetailDto updateUserProfile(Principal principal, @RequestBody UserProfileDto profileDto) {
        return myservice.updateUserProfile(principal, profileDto);
    }

    @PutMapping("/profile/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changePassword(Principal principal, @RequestBody PasswordChangeDto passwordDto) {
        myservice.changePassword(principal, passwordDto);
        return ResponseEntity.ok().build();
    }
}