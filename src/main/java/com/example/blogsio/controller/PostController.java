package com.example.blogsio.controller;

import com.example.blogsio.dto.PostDetailDto;
import com.example.blogsio.dto.PostDto;
import com.example.blogsio.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Add this import
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService myservice;

    @GetMapping("/getAll")
    public List<PostDetailDto> getallposts() {
        return myservice.getAllPosts();
    }

    @GetMapping("/getbyid/{id}")
    public PostDetailDto getPostbyId(@PathVariable long id) {
        return myservice.getPostById(id);
    }

    @GetMapping("/tag/{tagName}")
    public List<PostDetailDto> getPostsByTag(@PathVariable String tagName) {
        return myservice.getPostsByTag(tagName);
    }

    // Note: The create and update methods can still return the full PostEntity
    // because the transaction is still open within those methods.
    // However, it's good practice to also have them return a DTO.

    @PostMapping("/Create")
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    public PostDetailDto create(@RequestBody PostDto postDto, Principal principal) {
        return myservice.createPost(postDto, principal);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    public PostDetailDto update(@PathVariable("id") long id, @RequestBody PostDto postDto, Principal principal) {
        return myservice.updatePost(id, postDto, principal);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    public ResponseEntity<Void> deletebyID(@PathVariable long id, Principal principal) {
        myservice.deletePost(id, principal);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/search")
    public List<PostDetailDto> searchPosts(@RequestParam String keyword) {
        return myservice.searchPosts(keyword);
    }
    // --- NEW ENDPOINT FOR AUTHORS ---
    @GetMapping("/my-posts")
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    public List<PostDetailDto> getMyPosts(Principal principal) {
        return myservice.getMyPosts(principal);
    }

}