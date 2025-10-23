package com.example.blogsio.controller;

import com.example.blogsio.dto.CommentDto;
import com.example.blogsio.entity.CommentEntity;
import com.example.blogsio.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService myservice;

    @PostMapping("/post/{postId}")
    @PreAuthorize("isAuthenticated()")
    public CommentDto createComment(@RequestBody CommentEntity myComment, @PathVariable Long postId, Principal principal) {
        return myservice.createComment(myComment, postId, principal);
    }

    @GetMapping("/post/{postId}")
    public List<CommentDto> getCommentsForPost(@PathVariable Long postId) {
        return myservice.getCommentsByPostId(postId);
    }

    @DeleteMapping("/delete/{commentId}")
    @PreAuthorize("isAuthenticated()") // Any authenticated user can attempt, service layer will check ownership
    public ResponseEntity<Void> deletebyID(@PathVariable long commentId, Principal principal) {
        myservice.deleteCommentByID(commentId, principal);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()") // Any authenticated user can attempt, service layer will check ownership
    public CommentDto update(@PathVariable("id") long id, @RequestBody CommentEntity current, Principal principal) {
        return myservice.updateComment(id, current, principal);
    }

}