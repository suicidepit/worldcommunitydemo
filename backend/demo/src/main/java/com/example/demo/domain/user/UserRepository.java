package com.example.demo.domain.user;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByWalletAddress(String walletAddress);

  Optional<User> findByUsername(String username);
}
