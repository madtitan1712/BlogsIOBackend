package com.example.blogsio.repository;

import com.example.blogsio.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // Add this import

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    // Add this method to find comments by post ID
    List<CommentEntity> findByPostId(Long postId);
}