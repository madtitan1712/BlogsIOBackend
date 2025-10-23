package com.example.blogsio.service;

import com.example.blogsio.dto.CommentDto;
import com.example.blogsio.dto.UserDto;
import com.example.blogsio.entity.CommentEntity;
import com.example.blogsio.entity.PostEntity;
import com.example.blogsio.entity.UserEntity;
import com.example.blogsio.enums.userRole;
import com.example.blogsio.repository.CommentRepository;
import com.example.blogsio.repository.PostRepository;
import com.example.blogsio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;
    public CommentDto createComment(CommentEntity myComment, Long postId, Principal principal) {
        UserEntity user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        myComment.setUser(user);
        myComment.setPost(post);
        myComment.setTimeCreated(LocalDateTime.now());

        CommentEntity savedComment = commentRepository.save(myComment);

        return convertToDto(savedComment);
    }

    public CommentDto updateComment(long id, CommentEntity commentDetails, Principal principal) {
        UserEntity currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // Check for permission: user must be the original commenter
        if (!Objects.equals(comment.getUser().getId(), currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to update this comment");
        }

        comment.setCommentText(commentDetails.getCommentText());
        CommentEntity updatedComment = commentRepository.save(comment);
        return convertToDto(updatedComment);
    }

    public void deleteCommentByID(long id, Principal principal) {
        UserEntity currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // Check for permission: user must be the original commenter or an admin
        if (!Objects.equals(comment.getUser().getId(), currentUser.getId()) && currentUser.getRole() != userRole.ADMIN) {
            throw new AccessDeniedException("You do not have permission to delete this comment");
        }
        commentRepository.delete(comment);
    }

    public List<CommentEntity> getAll() {
        return commentRepository.findAll();
    }

    public CommentEntity getByID(long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public List<CommentDto> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CommentDto convertToDto(CommentEntity comment) {
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
    public Page<CommentDto> getAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable).map(this::convertToDto);
    }
}