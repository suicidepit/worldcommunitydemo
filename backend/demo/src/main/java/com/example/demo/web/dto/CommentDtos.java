package com.example.demo.web.dto;

import com.example.demo.web.dto.WorldIdDtos.WorldIdProofPayload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public final class CommentDtos {

  private CommentDtos() {
  }

  public record CreateCommentRequest(
      @NotBlank String authorWallet,
      @NotBlank String content,
      UUID parentCommentId,
      @Valid @NotNull WorldIdProofPayload proof
  ) {
  }
}
