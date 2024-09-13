package com.zerowhisper.task2.repository;

import com.zerowhisper.task2.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.token = ?1")
    Optional<RefreshToken> findByToken(RefreshToken token);
}
