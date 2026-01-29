package com.example.demo.service;

import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import com.example.demo.web.dto.WorldIdDtos.WorldIdAction;
import com.example.demo.web.dto.WorldIdDtos.WorldIdProofPayload;
import com.example.demo.worldid.WorldIdVerificationService;
import com.example.demo.worldid.WorldIdVerificationService.VerificationResult;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final WorldIdVerificationService worldIdVerificationService;

  public UserService(UserRepository userRepository, WorldIdVerificationService worldIdVerificationService) {
    this.userRepository = userRepository;
    this.worldIdVerificationService = worldIdVerificationService;
  }

  @Transactional
  public User upsertUser(String walletAddress, String username) {
    Optional<User> existing = userRepository.findByWalletAddress(walletAddress);
    if (existing.isPresent()) {
      User user = existing.get();
      user.updateUsername(username);
      return user;
    }
    return userRepository.save(new User(walletAddress, username));
  }

  @Transactional
  public User verifyUser(UUID userId, WorldIdProofPayload proof) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));
    VerificationResult result = worldIdVerificationService.verifyAndReserve(WorldIdAction.VERIFY_USER, proof, user);
    user.markVerified(result.verificationLevel());
    return user;
  }

  @Transactional(readOnly = true)
  public Optional<User> findByWalletAddress(String walletAddress) {
    return userRepository.findByWalletAddress(walletAddress);
  }

  @Transactional(readOnly = true)
  public Optional<User> findById(UUID userId) {
    return userRepository.findById(userId);
  }
}
