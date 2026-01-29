package com.example.demo.domain.like;

import com.example.demo.domain.post.Post;
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
@Table(name = "post_likes")
public class PostLike {

  @EmbeddedId
  private PostLikeId id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @MapsId("postId")
  @JoinColumn(name = "post_id")
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  protected PostLike() {
  }

  public PostLike(Post post, User user) {
    this.post = post;
    this.user = user;
    this.id = new PostLikeId(post.getId(), user.getId());
  }

  @PrePersist
  void onCreate() {
    this.createdAt = Instant.now();
  }

  public PostLikeId getId() {
    return id;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
