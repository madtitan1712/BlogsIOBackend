package com.example.blogsio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Column(name="comment_text")
    private String commentText;
    @Column(name="time_created")
    private LocalDateTime timeCreated;

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    private PostEntity post;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;
}
