package com.zerowhisper.task2.repository;

import com.zerowhisper.task2.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    @Query("SELECT u FROM UserAccount u WHERE u.username = ?1")
    Optional<UserAccount> findByUsername(String username);

    @Query("SELECT u FROM UserAccount u WHERE u.email = ?1")
    Optional<UserAccount> findByEmail(String email);

//    @Query("SELECT u FROM UserAccount u WHERE u.verificationToken = ?1")
//    Optional<UserAccount> findByVerificationToken(String verificationToken);

//    @Modifying
//    @Query("update UserAccount u set u.refreshToken=?1 where u.id=?2")
//    void setRefreshToken(String token, Long id);
}
