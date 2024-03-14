package com.softuni.fitlaunch.web.rest;


import com.softuni.fitlaunch.model.dto.comment.CommentCreationDTO;
import com.softuni.fitlaunch.model.dto.comment.CommentMessageDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.view.CommentView;
import com.softuni.fitlaunch.model.enums.UserRoleEnum;
import com.softuni.fitlaunch.service.CommentService;
import com.softuni.fitlaunch.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CommentsRestController {

    private final CommentService commentService;

    private final UserService userService;

    public CommentsRestController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }


    @GetMapping("/{workoutId}/comments/{commentId}")
    public ResponseEntity<CommentView> getComment(@PathVariable("commentId") Long commentId) {
        return ResponseEntity.ok(commentService.getComment(commentId));
    }

    @GetMapping("/{programId}/{weekId}/{workoutId}/comments")
    public ResponseEntity<List<CommentView>> getAllCommentsForWorkout(@PathVariable("programId") Long programId, @PathVariable("weekId") Long weekId, @PathVariable("workoutId") Long workoutId) {
        return ResponseEntity.ok(commentService.getAllCommentsForWorkout(workoutId));
    }

    @GetMapping("/{workoutId}/comments")
    public ResponseEntity<List<CommentView>> getCommentsByWorkoutId(@PathVariable("workoutId") Long workoutId) {
        return ResponseEntity.ok(commentService.getAllCommentsForWorkout(workoutId));
    }

    @PostMapping(value = "/{programId}/{weekId}/{workoutId}/comments", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CommentView> postComment(@PathVariable("programId") Long programId,
                                                   @PathVariable("weekId") Long weekId,
                                                   @PathVariable("workoutId") Long workoutId,
                                                   @RequestBody CommentMessageDTO commentMessageDTO,
                                                   Principal principal) {

        UserDTO user = userService.getUserByUsername(principal.getName());

        CommentCreationDTO commentDTO = new CommentCreationDTO(
                user.getUsername(),
                programId,
                workoutId,
                commentMessageDTO.getMessage()
        );

        CommentView commentView = commentService.addComment(commentDTO);


        return ResponseEntity.created(
                URI.create(String.format("/api/%d/%d/%d/comments/%d", programId, weekId, workoutId, commentView.getId()))
        ).body(commentView);
    }


    @DeleteMapping("/{programId}/{weekId}/{workoutId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("programId") Long programId,
                                                     @PathVariable("weekId") Long weekId,
                                                     @PathVariable("workoutId") Long workoutId,
                                                     @PathVariable("commentId") Long commentId,
                                                     Principal principal) {

        UserDTO user = userService.getUserByUsername(principal.getName());

        CommentView comment = commentService.getComment(commentId);

        if (user.getRoles().stream().anyMatch(r -> r.getRole().equals(UserRoleEnum.ADMIN)) || user.getUsername().equals(comment.getAuthorUsername())) {
            commentService.deleteCommentById(commentId);
        }

        return ResponseEntity
                .noContent()
                .build();
    }

}
