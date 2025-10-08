package com.example.blogsio.dto;

import com.example.blogsio.enums.postStatus;
import lombok.Data;
import java.util.Set;

@Data
public class PostDto {
    private String title;
    private String content;
    private postStatus status;
    private Set<String> tags;
}