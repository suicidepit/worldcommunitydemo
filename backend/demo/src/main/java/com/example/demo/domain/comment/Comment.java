package com.example.demo.domain.comment;

import com.example.demo.domain.post.Post;
import com.example.demo.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "comments")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "comment_id")
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "post_id")
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "author_id")
  private User author;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_comment_id")
  private Comment parent;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "is_deleted", nullable = false)
  private boolean deleted = false;

  protected Comment() {
  }

  public Comment(Post post, User author, Comment parent, String content) {
    this.post = Objects.requireNonNull(post, "post");
    this.author = Objects.requireNonNull(author, "author");
    this.parent = parent;
    this.content = normalizeContent(content);
  }

  @PrePersist
  void onCreate() {
    this.createdAt = Instant.now();
  }

  private String normalizeContent(String value) {
    return Objects.requireNonNull(value, "content")
        .trim();
  }

  public UUID getId() {
    return id;
  }

  public Post getPost() {
    return post;
  }

  public User getAuthor() {
    return author;
  }

  public Comment getParent() {
    return parent;
  }

  public String getContent() {
    return content;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public boolean isTopLevel() {
    return parent == null;
  }

  public void softDelete() {
    this.deleted = true;
  }
}
