package com.example.blogsio.service;

import com.example.blogsio.dto.CommentDto;
import com.example.blogsio.dto.PostDetailDto;
import com.example.blogsio.dto.PostDto;
import com.example.blogsio.dto.UserDto;
import com.example.blogsio.entity.CommentEntity;
import com.example.blogsio.entity.PostEntity;
import com.example.blogsio.entity.TagEntity;
import com.example.blogsio.entity.UserEntity;
import com.example.blogsio.enums.postStatus;
import com.example.blogsio.enums.userRole;
import com.example.blogsio.repository.PostRepository;
import com.example.blogsio.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository mypostRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagService tagService;

    @Transactional
    public PostDetailDto createPost(PostDto postDto, Principal principal) {
        UserEntity author = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<TagEntity> tags = tagService.findOrCreateTags(postDto.getTags());

        PostEntity newPost = new PostEntity();
        newPost.setTitle(postDto.getTitle());
        newPost.setContent(postDto.getContent());
        newPost.setStatus(postDto.getStatus());
        newPost.setAuthor(author);
        newPost.setTags(tags);
        newPost.setCreatedAt(LocalDateTime.now());

        PostEntity savedPost = mypostRepository.save(newPost);
        return convertEntityToDto(savedPost); // Return DTO
    }

    @Transactional
    public PostDetailDto updatePost(long id, PostDto postDto, Principal principal) {
        UserEntity currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        PostEntity post = mypostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id " + id));

        if (!Objects.equals(post.getAuthor().getId(), currentUser.getId()) && currentUser.getRole() != userRole.ADMIN) {
            throw new AccessDeniedException("You do not have permission to update this post");
        }

        Set<TagEntity> tags = tagService.findOrCreateTags(postDto.getTags());

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setStatus(postDto.getStatus());
        post.setTags(tags);

        PostEntity updatedPost = mypostRepository.save(post);
        return convertEntityToDto(updatedPost); // Return DTO
    }

    public void deletePost(long id, Principal principal) {
        UserEntity currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        PostEntity post = mypostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id " + id));

        // Check for permission: user must be the author or an admin
        if (!Objects.equals(post.getAuthor().getId(), currentUser.getId()) && currentUser.getRole() != userRole.ADMIN) {
            throw new AccessDeniedException("You do not have permission to delete this post");
        }

        mypostRepository.delete(post);
    }
    @Transactional(readOnly = true)
    public List<PostDetailDto> getAllPosts() {
        return mypostRepository.findByStatus(postStatus.PUBLISHED).stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostDetailDto getPostById(long id) {
        PostEntity post = mypostRepository.findById(id).orElse(null);
        if (post == null) {
            return null;
        }
        return convertEntityToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostDetailDto> getPostsByTag(String tagName) {
        return mypostRepository.findByTags_Name(tagName).stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostDetailDto> searchPosts(String keyword) {
        return mypostRepository.searchByTitleOrContent(keyword).stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }
    private PostDetailDto convertEntityToDto(PostEntity post) {
        PostDetailDto postDetailDto = new PostDetailDto();
        postDetailDto.setId(post.getId());
        postDetailDto.setTitle(post.getTitle());
        postDetailDto.setContent(post.getContent());
        postDetailDto.setStatus(post.getStatus());
        postDetailDto.setCreatedAt(post.getCreatedAt());

        UserDto authorDto = new UserDto();
        authorDto.setId(post.getAuthor().getId());
        authorDto.setName(post.getAuthor().getName());
        postDetailDto.setAuthor(authorDto);

        postDetailDto.setTags(post.getTags().stream()
                .map(TagEntity::getName)
                .collect(Collectors.toSet()));

        // Handle case where comments might be null on a new post
        if (post.getComments() != null) {
            postDetailDto.setComments(post.getComments().stream()
                    .map(this::convertCommentToDto)
                    .collect(Collectors.toList()));
        } else {
            postDetailDto.setComments(Collections.emptyList());
        }

        return postDetailDto;
    }

    private CommentDto convertCommentToDto(CommentEntity comment) {
        UserDto userDto = new UserDto();
        userDto.setId(comment.getUser().getId());
        userDto.setName(comment.getUser().getName());

        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setCommentText(comment.getCommentText());
        commentDto.setTimeCreated(comment.getTimeCreated());
        commentDto.setPostId(comment.getPost().getId());
        commentDto.setUser(userDto);

        return commentDto;
    }
    @Transactional(readOnly = true)
    public List<PostDetailDto> getMyPosts(Principal principal) {
        UserEntity currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return mypostRepository.findByAuthorId(currentUser.getId()).stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }
}