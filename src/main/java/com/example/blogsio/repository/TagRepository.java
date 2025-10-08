package com.example.blogsio.repository;

import com.example.blogsio.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Add this import

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    // Add this method to find a tag by its name
    Optional<TagEntity> findByName(String name);
}