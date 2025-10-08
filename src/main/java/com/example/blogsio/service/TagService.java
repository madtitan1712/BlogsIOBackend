package com.example.blogsio.service;

import com.example.blogsio.entity.TagEntity;
import com.example.blogsio.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public Set<TagEntity> findOrCreateTags(Set<String> tagNames) {
        Set<TagEntity> tags = new HashSet<>();
        if (tagNames != null) {
            for (String tagName : tagNames) {
                // Find tag by name, or create a new one if it doesn't exist
                TagEntity tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> {
                            TagEntity newTag = new TagEntity();
                            newTag.setName(tagName);
                            return tagRepository.save(newTag);
                        });
                tags.add(tag);
            }
        }
        return tags;
    }
}