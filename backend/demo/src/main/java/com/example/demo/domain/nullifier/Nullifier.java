package com.example.demo.domain.nullifier;

import com.example.demo.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "nullifiers")
public class Nullifier {

  @EmbeddedId
  private NullifierId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  protected Nullifier() {
  }

  public Nullifier(String action, String nullifierHash, User user) {
    this.id = new NullifierId(action, nullifierHash);
    this.user = user;
  }

  @PrePersist
  void onCreate() {
    this.createdAt = Instant.now();
  }

  public NullifierId getId() {
    return id;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public User getUser() {
    return user;
  }
}
