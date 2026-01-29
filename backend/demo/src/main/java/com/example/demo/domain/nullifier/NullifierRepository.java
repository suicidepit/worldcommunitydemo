package com.example.demo.domain.nullifier;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NullifierRepository extends JpaRepository<Nullifier, NullifierId> {

  boolean existsById(NullifierId id);
}
