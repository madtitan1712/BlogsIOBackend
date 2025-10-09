package com.example.blogsio.controller;

import com.example.blogsio.dto.PostDetailDto;
import com.example.blogsio.dto.PostDto;
import com.example.blogsio.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Add this import
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService myservice;

    @GetMapping("/getAll")
    public Page<PostDetailDto> getallposts(Pageable pageable) {
        return myservice.getAllPosts(pageable);
    }

    @GetMapping("/getbyid/{id}")
    public PostDetailDto getPostbyId(@PathVariable long id) {
        return myservice.getPostById(id);
    }

    @GetMapping("/tag/{tagName}")
    public Page<PostDetailDto> getPostsByTag(@PathVariable String tagName, Pageable pageable) {
        return myservice.getPostsByTag(tagName, pageable);
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
    public Page<PostDetailDto> searchPosts(@RequestParam String keyword, Pageable pageable) {
        return myservice.searchPosts(keyword, pageable);
    }
    // --- NEW ENDPOINT FOR AUTHORS ---
    @GetMapping("/my-posts")
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    public Page<PostDetailDto> getMyPosts(Principal principal, Pageable pageable) {
        return myservice.getMyPosts(principal, pageable);
    }

}