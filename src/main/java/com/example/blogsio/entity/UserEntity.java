package com.example.blogsio.entity;

import com.example.blogsio.enums.userRole;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name="email", unique = true)
    private String email;
    @Column(name="password_hash")
    private String passwordHash;
    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private userRole role;
}
