package com.example.blogsio.service;

import com.example.blogsio.entity.UserEntity;
import com.example.blogsio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity createUser(UserEntity user) {
        return userRepository.save(user);
    }

    public UserEntity updateUser(long id, UserEntity user) {
        if (userRepository.existsById(id)) {
            UserEntity current= userRepository.findById(id).get();
            current.setEmail(user.getEmail());
            current.setRole(user.getRole());
            current.setPasswordHash(user.getPasswordHash());
            return userRepository.save(current);
        }
        return user;
    }

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
}