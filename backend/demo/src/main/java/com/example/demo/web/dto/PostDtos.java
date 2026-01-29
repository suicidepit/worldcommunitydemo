package com.example.demo.web.dto;

import com.example.demo.domain.category.Category;
import com.example.demo.domain.comment.Comment;
import com.example.demo.domain.post.Post;
import com.example.demo.domain.user.User;
import com.example.demo.web.dto.WorldIdDtos.WorldIdProofPayload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class PostDtos {

  private PostDtos() {
  }

  public record CreatePostRequest(
      @NotBlank String authorWallet,
      @NotNull UUID categoryId,
      String title,
      @NotBlank String content,
      @Valid @NotNull WorldIdProofPayload proof
  ) {
  }

  public record PostResponse(
      UUID id,
      String title,
      String content,
      Instant createdAt,
      Instant updatedAt,
      boolean deleted,
      AuthorSummary author,
      CategorySummary category
  ) {
    public static PostResponse from(Post post) {
      return new PostResponse(
          post.getId(),
          post.getTitle(),
          post.getContent(),
          post.getCreatedAt(),
          post.getUpdatedAt(),
          post.isDeleted(),
          AuthorSummary.from(post.getAuthor()),
          CategorySummary.from(post.getCategory())
      );
    }
  }

  public record AuthorSummary(UUID id, String username, String walletAddress) {
    static AuthorSummary from(User user) {
      return new AuthorSummary(user.getId(), user.getUsername(), user.getWalletAddress());
    }
  }

  public record CategorySummary(UUID id, String theme) {
    static CategorySummary from(Category category) {
      return new CategorySummary(category.getId(), category.getTheme());
    }
  }

  public record CommentResponse(
      UUID id,
      UUID postId,
      UUID parentCommentId,
      String content,
      boolean deleted,
      Instant createdAt,
      AuthorSummary author
  ) {
    public static CommentResponse from(Comment comment) {
      return new CommentResponse(
          comment.getId(),
          comment.getPost().getId(),
          comment.getParent() == null ? null : comment.getParent().getId(),
          comment.getContent(),
          comment.isDeleted(),
          comment.getCreatedAt(),
          AuthorSummary.from(comment.getAuthor())
      );
    }
  }

  public record PostDetailResponse(
      PostResponse post,
      List<CommentResponse> comments
  ) {
  }
}
