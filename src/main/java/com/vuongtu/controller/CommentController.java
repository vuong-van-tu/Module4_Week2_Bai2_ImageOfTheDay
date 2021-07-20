package com.vuongtu.controller;

import com.vuongtu.model.Comment;
import com.vuongtu.service.CommentService;
import com.vuongtu.service.Impl.HibernateCommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService = new HibernateCommentServiceImpl();

    @GetMapping("/home")
    public ModelAndView showHome(){
        ModelAndView modelAndView = new ModelAndView("comment");
        List<Comment> comments = commentService.showAllComment();
        modelAndView.addObject("comments",comments);
        return modelAndView;
    }
    @GetMapping("/saveComment")
    public String addComment(Comment comment){
        commentService.addComment(comment);
        return "redirect:/home";
    }

    @GetMapping("/likeComment/{id}")
    public String like(@PathVariable long id){
        Comment comment = commentService.findOne(id);
        commentService.addLike(comment);
        return "redirect:/home";
    }

    @GetMapping("/dislikeComment/{id}")
    public String disLike(@PathVariable long id){
        Comment comment = commentService.findOne(id);
        commentService.disLike(comment);
        return "redirect:/home";
    }
}
