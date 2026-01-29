package com.example.demo.domain.nullifier;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class NullifierId implements Serializable {

  @Column(name = "action", length = 64)
  private String action;

  @Column(name = "nullifier_hash", length = 255)
  private String nullifierHash;

  public NullifierId() {
  }

  public NullifierId(String action, String nullifierHash) {
    this.action = action;
    this.nullifierHash = nullifierHash;
  }

  public String getAction() {
    return action;
  }

  public String getNullifierHash() {
    return nullifierHash;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NullifierId that = (NullifierId) o;
    return Objects.equals(action, that.action) && Objects.equals(nullifierHash, that.nullifierHash);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, nullifierHash);
  }
}
