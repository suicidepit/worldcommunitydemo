package com.example.demo.web.dto;

import com.example.demo.web.dto.WorldIdDtos.WorldIdProofPayload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class LikeDtos {

  private LikeDtos() {
  }

  public record LikeRequest(
      @NotBlank String walletAddress,
      @Valid @NotNull WorldIdProofPayload proof
  ) {
  }
}
