package com.example.blogsio.service;

import com.example.blogsio.dto.UserDetailDto;
import com.example.blogsio.entity.UserEntity;
import com.example.blogsio.enums.userRole;
import com.example.blogsio.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity createUser(UserEntity user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    public UserEntity updateUser(long id, UserEntity user) {
        if (userRepository.existsById(id)) {
            UserEntity current = userRepository.findById(id).get();
            current.setEmail(user.getEmail());
            current.setRole(user.getRole());
            if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                current.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            }
            return userRepository.save(current);
        }
        return null;
    }
    @Transactional
    public boolean deleteUserByID(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public UserEntity getByID(long id) {
        return userRepository.findById(id).orElse(null);
    }
    public List<UserDetailDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public UserDetailDto updateUserRole(Long userId, userRole role) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        UserEntity updatedUser = userRepository.save(user);
        return convertEntityToDto(updatedUser);
    }
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }

    // Helper method to convert UserEntity to UserDetailDto
    private UserDetailDto convertEntityToDto(UserEntity user) {
        UserDetailDto dto = new UserDetailDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}