package com.example.blogsio.controller;

import com.example.blogsio.entity.CommentEntity;
import com.example.blogsio.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService myservice;
    @GetMapping("/getAll")
    public List<CommentEntity> getallcomments(){
        return myservice.getAll();
    }
    @GetMapping("/getbyid")
    public CommentEntity getCommentbyId(long id){
        return myservice.getByID(id);
    }
    @DeleteMapping("/deleteById")
    public boolean deletebyID(long id){
        return myservice.deleteCommentBytID(id);
    }
    @PutMapping("/update/{id}")
    public CommentEntity update(@PathVariable("id") long id,@RequestBody CommentEntity current){
        return myservice.updateComment(id,current);
    }
    @PostMapping("/addComment")
    public CommentEntity createComment(@RequestBody CommentEntity myComment){
        return myservice.createComment(myComment);
    }


}
