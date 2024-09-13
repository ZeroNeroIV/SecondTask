package com.zerowhisper.task2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @ManyToOne(targetEntity = UserAccount.class, fetch = FetchType.EAGER)
    private UserAccount userAccount;

    // IP or something to know each device so refresh tokens don't get messed up

    public RefreshToken() {
    }
}
