package com.example.blogsio.dto;

import com.example.blogsio.enums.userRole;
import lombok.Data;

@Data
public class RoleUpdateDto {
    private userRole role;
}