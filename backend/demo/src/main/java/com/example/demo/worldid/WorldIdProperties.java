package com.example.demo.worldid;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "worldid")
public record WorldIdProperties(
    String baseUrl,
    String apiKey,
    String appId
) {

  public WorldIdProperties {
    if (baseUrl == null || apiKey == null || appId == null) {
      throw new IllegalArgumentException("World ID properties must be set");
    }
  }
}
