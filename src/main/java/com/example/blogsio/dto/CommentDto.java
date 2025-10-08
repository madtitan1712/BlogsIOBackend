package com.example.blogsio.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String commentText;
    private LocalDateTime timeCreated;
    private UserDto user;
    private Long postId;
}