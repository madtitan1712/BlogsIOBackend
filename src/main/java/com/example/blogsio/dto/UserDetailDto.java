package com.example.blogsio.dto;

import com.example.blogsio.enums.userRole;
import lombok.Data;

@Data
public class UserDetailDto {
    private Long id;
    private String name;
    private String email;
    private userRole role;
}