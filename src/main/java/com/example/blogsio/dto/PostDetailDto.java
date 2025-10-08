package com.example.blogsio.dto;

import com.example.blogsio.enums.postStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class PostDetailDto {
    private Long id;
    private String title;
    private String content;
    private postStatus status;
    private LocalDateTime createdAt;
    private UserDto author;
    private Set<String> tags;
    private List<CommentDto> comments;
}