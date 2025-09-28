package com.example.blogsio.service;
import com.example.blogsio.entity.CommentEntity;
import com.example.blogsio.entity.UserEntity;
import com.example.blogsio.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    public CommentEntity createComment(CommentEntity myComment){
        return commentRepository.save(myComment);
    }
    public CommentEntity updateComment(long id,CommentEntity myComment){
        if(commentRepository.existsById(id)){
            CommentEntity current=commentRepository.findById(id).get();
            current.setCommentText(myComment.getCommentText());
            current.setTimeCreated(myComment.getTimeCreated());
            return commentRepository.save(current);
        }
        return myComment;
    }
    public boolean deleteCommentBytID(long id){
        if(commentRepository.existsById(id)){
            commentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public List<CommentEntity> getAll(){
        return commentRepository.findAll();
    }
    public CommentEntity getByID(long id){
        return commentRepository.findById(id).get();
    }


}
