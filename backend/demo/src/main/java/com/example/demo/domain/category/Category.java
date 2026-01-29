package com.example.demo.domain.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "categories")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "category_id")
  private UUID id;

  @Column(name = "theme", nullable = false, unique = true)
  private String theme;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  protected Category() {
  }

  public Category(String theme) {
    this.theme = normalize(theme);
  }

  @PrePersist
  void onCreate() {
    this.createdAt = Instant.now();
  }

  private String normalize(String value) {
    return Objects.requireNonNull(value, "value")
        .trim();
  }

  public UUID getId() {
    return id;
  }

  public String getTheme() {
    return theme;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
