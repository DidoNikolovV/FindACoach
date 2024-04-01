package com.softuni.fitlaunch.web.rest;


import com.softuni.fitlaunch.model.dto.comment.CommentCreationDTO;
import com.softuni.fitlaunch.model.dto.view.CommentView;
import com.softuni.fitlaunch.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/v1/comments")
public class CommentsRestController {

    private final CommentService commentService;


    public CommentsRestController(CommentService commentService) {
        this.commentService = commentService;
    }


    @GetMapping("/{workoutId}/{commentId}")
    public ResponseEntity<CommentView> getComment(@PathVariable("workoutId") Long workoutId, @PathVariable("commentId") Long commentId) {
        return ResponseEntity.ok(commentService.getComment(commentId));
    }


    @GetMapping("/{workoutId}/all")
    public ResponseEntity<List<CommentView>> getCommentsByWorkoutId(@PathVariable("workoutId") Long workoutId) {
        return ResponseEntity.ok(commentService.getAllCommentsForWorkout(workoutId));
    }

    @PostMapping(value = "/{workoutId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CommentView> postComment(@PathVariable("workoutId") Long workoutId,
                                                   @RequestBody CommentCreationDTO commentCreationDTO, Principal principal) {

        CommentView commentView = commentService.addComment(commentCreationDTO, principal.getName(), workoutId);


        return ResponseEntity.created(
                URI.create(String.format("/api/v1/comments/%d/%d", workoutId, commentView.getId()))
        ).body(commentView);
    }


    @DeleteMapping("/{workoutId}/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("workoutId") Long workoutId, @PathVariable("commentId") Long commentId,
                                              Principal principal) {

        commentService.deleteCommentById(commentId, principal.getName());

        return ResponseEntity
                .noContent()
                .build();
    }

}
