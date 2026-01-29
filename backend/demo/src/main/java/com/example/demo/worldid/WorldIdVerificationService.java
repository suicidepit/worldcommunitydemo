package com.example.demo.worldid;

import com.example.demo.domain.user.User;
import com.example.demo.service.NullifierService;
import com.example.demo.service.NullifierService.NullifierReplayException;
import com.example.demo.web.dto.WorldIdDtos.WorldIdAction;
import com.example.demo.web.dto.WorldIdDtos.WorldIdProofPayload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class WorldIdVerificationService {

  private final WorldIdClient client;
  private final NullifierService nullifierService;

  public WorldIdVerificationService(WorldIdClient client, NullifierService nullifierService) {
    this.client = client;
    this.nullifierService = nullifierService;
  }

  public VerificationResult verifyAndReserve(WorldIdAction action, WorldIdProofPayload proof, User user) {
    try {
      WorldIdClient.VerificationResponse response = client.verify(proof, action.actionId());
      if (response == null || !response.success()) {
        throw new WorldIdVerificationException("World ID verification failed: " + (response != null ? response.error() : "no response"));
      }
      if (!proof.nullifierHash().equals(response.nullifier_hash())) {
        throw new WorldIdVerificationException("Nullifier mismatch");
      }
      short verificationLevel = parseVerificationLevel(response.verification_level());
      nullifierService.reserve(action.actionId(), response.nullifier_hash(), user);
      return new VerificationResult(response.nullifier_hash(), verificationLevel);
    } catch (RestClientException | NullifierReplayException ex) {
      throw new WorldIdVerificationException("World ID verification error", ex);
    }
  }

  private short parseVerificationLevel(String level) {
    if (level == null) {
      return 0;
    }
    return switch (level.toLowerCase()) {
      case "orb" -> 2;
      case "device" -> 1;
      default -> 0;
    };
  }

  public record VerificationResult(String nullifierHash, short verificationLevel) {
  }

  public static class WorldIdVerificationException extends RuntimeException {
    public WorldIdVerificationException(String message) {
      super(message);
    }

    public WorldIdVerificationException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
