package com.example.blogsio.entity;

import com.example.blogsio.enums.postStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List; // Add this import
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name="content")
    @Lob
    private String content;
    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private postStatus status;
    @Column(name="time")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name="authorid", nullable = false)
    private UserEntity author;

    // --- THIS IS THE NEW CODE TO ADD ---
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;
    // ------------------------------------

    @ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity> tags;
}