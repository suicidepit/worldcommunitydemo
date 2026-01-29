package com.example.demo.domain.post;

import com.example.demo.domain.category.Category;
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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "posts")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "post_id")
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "category_id")
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "author_id")
  private User author;

  @Column(name = "title")
  private String title;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @Column(name = "is_deleted", nullable = false)
  private boolean deleted = false;

  @Version
  private long version;

  protected Post() {
  }

  public Post(Category category, User author, String title, String content) {
    this.category = Objects.requireNonNull(category, "category");
    this.author = Objects.requireNonNull(author, "author");
    this.title = title == null ? null : title.trim();
    this.content = normalizeContent(content);
  }

  @PrePersist
  void onCreate() {
    this.createdAt = Instant.now();
    this.updatedAt = this.createdAt;
  }

  @PreUpdate
  void onUpdate() {
    this.updatedAt = Instant.now();
  }

  private String normalizeContent(String value) {
    return Objects.requireNonNull(value, "content")
        .trim();
  }

  public UUID getId() {
    return id;
  }

  public Category getCategory() {
    return category;
  }

  public User getAuthor() {
    return author;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void updateContent(String title, String content) {
    this.title = title == null ? null : title.trim();
    this.content = normalizeContent(content);
  }

  public void softDelete() {
    this.deleted = true;
  }
}
