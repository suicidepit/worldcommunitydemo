package com.example.demo.web.controller;

import com.example.demo.domain.comment.Comment;
import com.example.demo.domain.user.User;
import com.example.demo.service.CommentService;
import com.example.demo.service.LikeService;
import com.example.demo.service.UserService;
import com.example.demo.web.controller.PostController.LikeResponse;
import com.example.demo.web.dto.LikeDtos;
import com.example.demo.web.dto.PostDtos;
import com.example.demo.web.dto.WorldIdDtos.WorldIdAction;
import com.example.demo.web.dto.WorldIdDtos.WorldIdProofPayload;
import com.example.demo.worldid.WorldIdVerificationService;
import com.example.demo.worldid.WorldIdVerificationService.WorldIdVerificationException;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

  private final CommentService commentService;
  private final UserService userService;
  private final LikeService likeService;
  private final WorldIdVerificationService worldIdVerificationService;

  public CommentController(
      CommentService commentService,
      UserService userService,
      LikeService likeService,
      WorldIdVerificationService worldIdVerificationService
  ) {
    this.commentService = commentService;
    this.userService = userService;
    this.likeService = likeService;
    this.worldIdVerificationService = worldIdVerificationService;
  }

  @DeleteMapping("/{commentId}")
  public PostDtos.CommentResponse deleteComment(
      @PathVariable UUID commentId,
      @RequestParam("wallet") String walletAddress
  ) {
    User author = userService.findByWalletAddress(walletAddress)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
    return commentService.softDelete(commentId, author.getId())
        .map(PostDtos.CommentResponse::from)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found or not owned"));
  }

  @PostMapping("/{commentId}/likes")
  public LikeResponse likeComment(
      @PathVariable UUID commentId,
      @Valid @RequestBody LikeDtos.LikeRequest request
  ) {
    Comment comment = commentService.findById(commentId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
    User user = userService.findByWalletAddress(request.walletAddress())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    ensureVerifiedUser(user);
    verifyWorldIdProof(WorldIdAction.COMMENT_LIKE, request.proof(), user);
    boolean created = likeService.likeComment(comment, user);
    long count = likeService.commentLikeCount(comment);
    return new LikeResponse(created, count);
  }

  @DeleteMapping("/{commentId}/likes")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void unlikeComment(
      @PathVariable UUID commentId,
      @Valid @RequestBody LikeDtos.LikeRequest request
  ) {
    Comment comment = commentService.findById(commentId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
    User user = userService.findByWalletAddress(request.walletAddress())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    likeService.unlikeComment(comment, user);
  }

  private void verifyWorldIdProof(WorldIdAction action, WorldIdProofPayload proof, User user) {
    try {
      worldIdVerificationService.verifyAndReserve(action, proof, user);
    } catch (WorldIdVerificationException ex) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }
  }

  private void ensureVerifiedUser(User user) {
    if (user.getWorldVerifiedLevel() <= 0) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be verified");
    }
  }
}
