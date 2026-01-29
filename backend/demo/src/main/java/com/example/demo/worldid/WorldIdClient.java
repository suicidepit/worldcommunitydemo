package com.example.demo.worldid;

import com.example.demo.web.dto.WorldIdDtos.WorldIdProofPayload;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class WorldIdClient {

  private final RestClient restClient;
  private final WorldIdProperties properties;

  public WorldIdClient(WorldIdProperties properties) {
    this.properties = properties;
    this.restClient = RestClient.builder()
        .baseUrl(properties.baseUrl())
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + properties.apiKey())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

  public VerificationResponse verify(WorldIdProofPayload proof, String action) {
    VerifyRequest request = new VerifyRequest(
        properties.appId(),
        action,
        proof.merkleRoot(),
        proof.nullifierHash(),
        proof.proof(),
        proof.credentialType(),
        proof.signal()
    );

    return restClient.post()
        .uri("/verify")
        .body(request)
        .retrieve()
        .body(VerificationResponse.class);
  }

  public record VerifyRequest(
      String app_id,
      String action,
      String merkle_root,
      String nullifier_hash,
      String proof,
      String credential_type,
      String signal
  ) {
  }

  public record VerificationResponse(
      boolean success,
      String action,
      String nullifier_hash,
      String verification_level,
      String error
  ) {
  }
}
