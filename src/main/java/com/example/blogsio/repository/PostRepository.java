package com.example.blogsio.repository;

import com.example.blogsio.entity.PostEntity;
import com.example.blogsio.enums.postStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface PostRepository extends JpaRepository<PostEntity, Long>, PagingAndSortingRepository<PostEntity, Long> {

    // Add Pageable to this query
    @Query("SELECT p FROM PostEntity p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    Page<PostEntity> searchByTitleOrContent(@Param("keyword") String keyword, Pageable pageable);

    Page<PostEntity> findByStatus(postStatus status, Pageable pageable);
    Page<PostEntity> findByAuthorId(Long authorId, Pageable pageable);
    Page<PostEntity> findByTags_Name(String name, Pageable pageable);
}