package com.example.demo.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "user_id")
  private UUID id;

  @Column(name = "wallet_address", nullable = false, unique = true)
  private String walletAddress;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "world_verified_level", nullable = false)
  private short worldVerifiedLevel = 0;

  @Column(name = "verified_at")
  private Instant verifiedAt;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Version
  private long version;

  protected User() {
    // for JPA
  }

  public User(String walletAddress, String username) {
    this.walletAddress = normalize(walletAddress);
    this.username = normalize(username);
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

  public String getWalletAddress() {
    return walletAddress;
  }

  public String getUsername() {
    return username;
  }

  public short getWorldVerifiedLevel() {
    return worldVerifiedLevel;
  }

  public Instant getVerifiedAt() {
    return verifiedAt;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void updateUsername(String username) {
    this.username = normalize(username);
  }

  public void markVerified(short level) {
    this.worldVerifiedLevel = level;
    this.verifiedAt = Instant.now();
  }
}
