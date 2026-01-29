package com.example.demo.domain.like;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class CommentLikeId implements Serializable {

  @Column(name = "comment_id")
  private UUID commentId;

  @Column(name = "user_id")
  private UUID userId;

  public CommentLikeId() {
  }

  public CommentLikeId(UUID commentId, UUID userId) {
    this.commentId = commentId;
    this.userId = userId;
  }

  public UUID getCommentId() {
    return commentId;
  }

  public UUID getUserId() {
    return userId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommentLikeId that = (CommentLikeId) o;
    return Objects.equals(commentId, that.commentId) && Objects.equals(userId, that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commentId, userId);
  }
}
