package com.example.blogsio.service;

import com.example.blogsio.entity.PostEntity;
import com.example.blogsio.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository mypostRepository;
    public PostEntity createPost(PostEntity mypost){
        return mypostRepository.save(mypost);
    }
    public PostEntity updatePost(long id,PostEntity mypost){
        if(mypostRepository.existsById(id)){
            PostEntity current= mypostRepository.findById(id).get();
            current.setAuthor(mypost.getAuthor());
            current.setTitle(mypost.getTitle());
            current.setContent(mypost.getContent());
            current.setCreatedAt(mypost.getCreatedAt());
            return mypostRepository.save(current);
        }
        return mypost;
    }
    public boolean deletePost(long id){
        if(mypostRepository.existsById(id)){
            mypostRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public List<PostEntity> getAllPosts(){
        return mypostRepository.findAll();
    }
    public PostEntity getPostById(long id){
        return mypostRepository.findById(id).get();
    }
}
