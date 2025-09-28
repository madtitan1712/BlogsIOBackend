package com.example.blogsio.controller;

import com.example.blogsio.entity.UserEntity;
import com.example.blogsio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService myservice;

    @GetMapping("/getAll")
    public List<UserEntity> getallusers() {
        return myservice.getAll();
    }

    @GetMapping("/getbyid")
    public UserEntity getUserbyId(long id) {
        return myservice.getByID(id);
    }

    @DeleteMapping("/deleteById")
    public boolean deletebyID(long id) {
        return myservice.deleteUserByID(id);
    }

    @PutMapping("/update/{id}")
    public UserEntity update(@PathVariable("id") long id,@RequestBody UserEntity current) {
        return myservice.updateUser(id, current);
    }
    @PostMapping("/createuser")
    public UserEntity createuser(@RequestBody UserEntity user){
        return myservice.createUser(user);
    }
}