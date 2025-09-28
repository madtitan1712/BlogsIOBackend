package com.example.blogsio.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "post_tags")
public class PostTagEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "post_id",referencedColumnName = "id")
    private PostEntity post;
    @Id
    @ManyToOne
    @JoinColumn(name = "tag_id",referencedColumnName = "id")
    private TagEntity user;
}
