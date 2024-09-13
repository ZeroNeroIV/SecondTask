package com.zerowhisper.task2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class UserAccount implements UserDetails {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(unique = true, nullable = false)
    private String username;

    @Setter
    @Column(nullable = false)
    private String password;

    @Getter
    @Setter
    @Column(unique = true, nullable = false)
    private String email;

    @Getter
    @Setter
    @Column(nullable = false)
    private String role;

    @Getter
    @Setter
    @Column(name = "verification_token")
    private String verificationToken;

    @Getter
    @Setter
    @Column(name = "verification_token_expiry")
    private LocalDateTime verificationTokenExpiry;

    @Setter
    @Column(nullable = false)
    private boolean enabled;

    public UserAccount() {
        enabled = false;
    }

    public UserAccount(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        enabled = false;
    }

    public UserAccount(String username, String email, String encode) {
        this.username = username;
        this.email = email;
        this.password = encode;
        this.role = "USER";
        enabled = false;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    // NEEDS TO BE FIXED
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // NEEDS TO BE FIXED
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
}
