package com.example.blogsio.controller;

import com.example.blogsio.entity.PostEntity;
import com.example.blogsio.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService myservice;

    @GetMapping("/getAll")
    public List<PostEntity> getallposts() {
        return myservice.getAllPosts();
    }

    @GetMapping("/getbyid")
    public PostEntity getPostbyId(long id) {
        return myservice.getPostById(id);
    }

    @DeleteMapping("/delete")
    public boolean deletebyID(long id) {
        return myservice.deletePost(id);
    }

    @PutMapping("/update/{id}")
    public PostEntity update(@PathVariable("id") long id, @RequestBody PostEntity current) {
        return myservice.updatePost(id, current);
    }
    @PostMapping("/Create")
    public PostEntity create(@RequestBody PostEntity post){
        return myservice.createPost(post);
    }
}