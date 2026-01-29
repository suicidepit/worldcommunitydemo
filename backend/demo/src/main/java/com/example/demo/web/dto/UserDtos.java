package com.example.demo.web.dto;

import com.example.demo.domain.user.User;
import com.example.demo.web.dto.WorldIdDtos.WorldIdProofPayload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public final class UserDtos {

  private UserDtos() {
  }

  public record UpsertUserRequest(
      @NotBlank String walletAddress,
      @NotBlank String username
  ) {
  }

  public record VerifyUserRequest(
      @Valid @NotNull WorldIdProofPayload proof
  ) {
  }

  public record UserResponse(
      UUID id,
      String walletAddress,
      String username,
      short worldVerifiedLevel,
      Instant verifiedAt,
      Instant createdAt
  ) {
    public static UserResponse from(User user) {
      return new UserResponse(
          user.getId(),
          user.getWalletAddress(),
          user.getUsername(),
          user.getWorldVerifiedLevel(),
          user.getVerifiedAt(),
          user.getCreatedAt()
      );
    }
  }
}
