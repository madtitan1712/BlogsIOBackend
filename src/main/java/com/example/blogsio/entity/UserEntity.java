package com.example.blogsio.entity;

import com.example.blogsio.enums.userRole;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    // This tells JPA to delete all posts by this user when the user is deleted.
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostEntity> posts;

    // This tells JPA to delete all comments made by this user when the user is deleted.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;

}
