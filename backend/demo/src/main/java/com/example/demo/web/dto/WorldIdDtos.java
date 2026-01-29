package com.example.demo.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class WorldIdDtos {

  private WorldIdDtos() {
  }

  public record WorldIdProofPayload(
      @NotBlank String merkleRoot,
      @NotBlank String nullifierHash,
      @NotBlank String proof,
      @NotBlank String credentialType,
      String signal
  ) {
  }

  public enum WorldIdAction {
    VERIFY_USER("world_verify"),
    POST_CREATE("post_create"),
    COMMENT_CREATE("comment_create"),
    POST_LIKE("post_like"),
    COMMENT_LIKE("comment_like");

    private final String actionId;

    WorldIdAction(String actionId) {
      this.actionId = actionId;
    }

    public String actionId() {
      return actionId;
    }
  }
}
