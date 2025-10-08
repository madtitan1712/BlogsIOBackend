package com.example.blogsio.repository;

import com.example.blogsio.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    @Query("SELECT p FROM PostEntity p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    List<PostEntity> searchByTitleOrContent(@Param("keyword") String keyword);

    // Add this method to find posts by a specific tag name
    List<PostEntity> findByTags_Name(String name);
}