package com.example.demo.service;

import com.example.demo.domain.nullifier.Nullifier;
import com.example.demo.domain.nullifier.NullifierId;
import com.example.demo.domain.nullifier.NullifierRepository;
import com.example.demo.domain.user.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NullifierService {

  private final NullifierRepository nullifierRepository;

  public NullifierService(NullifierRepository nullifierRepository) {
    this.nullifierRepository = nullifierRepository;
  }

  @Transactional
  public void reserve(String action, String nullifierHash, User user) {
    NullifierId id = new NullifierId(action, nullifierHash);
    if (nullifierRepository.existsById(id)) {
      throw new NullifierReplayException(action, nullifierHash);
    }
    try {
      nullifierRepository.save(new Nullifier(action, nullifierHash, user));
    } catch (DataIntegrityViolationException ex) {
      throw new NullifierReplayException(action, nullifierHash);
    }
  }

  public static class NullifierReplayException extends RuntimeException {
    public NullifierReplayException(String action, String nullifierHash) {
      super("Nullifier already used for action %s".formatted(action));
    }
  }
}
