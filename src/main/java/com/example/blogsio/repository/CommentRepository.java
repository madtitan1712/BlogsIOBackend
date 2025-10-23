package com.example.blogsio.repository;

import com.example.blogsio.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository; // Add this import
import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long>, PagingAndSortingRepository<CommentEntity, Long> {
    List<CommentEntity> findByPostId(Long postId);
}