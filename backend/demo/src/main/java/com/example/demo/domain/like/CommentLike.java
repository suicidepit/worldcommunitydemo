package com.example.demo.domain.like;

import com.example.demo.domain.comment.Comment;
import com.example.demo.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "comment_likes")
public class CommentLike {

  @EmbeddedId
  private CommentLikeId id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @MapsId("commentId")
  @JoinColumn(name = "comment_id")
  private Comment comment;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  protected CommentLike() {
  }

  public CommentLike(Comment comment, User user) {
    this.comment = comment;
    this.user = user;
    this.id = new CommentLikeId(comment.getId(), user.getId());
  }

  @PrePersist
  void onCreate() {
    this.createdAt = Instant.now();
  }

  public CommentLikeId getId() {
    return id;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
