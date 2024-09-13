package com.zerowhisper.task2.service;

import com.zerowhisper.task2.dto.LoginUserDto;
import com.zerowhisper.task2.dto.RegisterUserDto;
import com.zerowhisper.task2.dto.VerifyUserDto;
import com.zerowhisper.task2.entity.UserAccount;
import com.zerowhisper.task2.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Autowired
    public AuthenticationService(
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            EmailService emailService) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    public UserAccount signUp(RegisterUserDto registerUserDto) {
        UserAccount userAccount = new UserAccount(
                registerUserDto.username,
                passwordEncoder.encode(registerUserDto.password),
                registerUserDto.email,
                registerUserDto.role == null || registerUserDto.role.isEmpty()
                        ? "USER"
                        : registerUserDto.role
        );
//        userRepository.setRefreshToken(..., user.getId());
        userAccount.setVerificationToken(generateVerificationToken());


        sendVerificationEmail(userAccount);

        userAccount.setVerificationTokenExpiry(LocalDateTime.now().plusMinutes(10));

        return userAccountRepository.save(userAccount);
    }

    public UserAccount authenticate(LoginUserDto loginUserDto) {
        Optional<UserAccount> optionalUser = userAccountRepository
                .findByUsername(loginUserDto.getUsername());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Username not found!");
        }
        UserAccount userAccount = optionalUser.get();
        if (!userAccount.isEnabled()) {
            throw new RuntimeException("User not verified");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.getUsername(),
                        loginUserDto.getPassword()
                ));
        return userAccount;
    }

    public void verifyUser(VerifyUserDto verifyUserDto) {
        Optional<UserAccount> optionalUser = userAccountRepository
                .findByEmail(verifyUserDto.email);
        if (optionalUser.isPresent()) {
            UserAccount userAccount = optionalUser.get();
            if (userAccount.getVerificationTokenExpiry().isBefore(LocalDateTime.now()))
                throw new RuntimeException("Verification token expired");
            if (userAccount.getVerificationToken().equals(verifyUserDto.verificationToken)) {
                userAccount.setEnabled(true);
                userAccount.setVerificationToken(null);
                userAccount.setVerificationTokenExpiry(null);
                userAccountRepository.save(userAccount);
            } else {
                throw new RuntimeException("Invalid verification token");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void sendVerificationEmail(UserAccount userAccount) {
        String subject = "Account Verification";
        String verificationCode = userAccount.getVerificationToken();
        String message = "<h2>Hello "
                + userAccount.getUsername()
                + ",</h2>\n\n"
                + "<b>Please use this verification code to verify your account: "
                + verificationCode
                + "</b>\n\n<h3>You have 10 minutes to verify your account."
                + "</h3>\n\n...<code>have a nice day :3 👍</code>";
        emailService.sendVerificationEmail(userAccount.getEmail(), subject, message);
    }

    private String generateVerificationToken() {
        Random rand = new Random();
        Integer randomInteger = rand.nextInt(90000000) + 10000000 ;
        return String.valueOf(randomInteger);
    }

//    public void resendVerificationToken(String email) {
//        Optional<UserAccount> optionalUser = userAccountRepository.findByEmail(email);
//        if (optionalUser.isPresent()) {
//            UserAccount userAccount = optionalUser.get();
//            if (userAccount.isEnabled()) {
//                throw new RuntimeException("User already verified");
//            }
//            userAccount.setVerificationToken(generateVerificationToken());
//            userAccount.setVerificationTokenExpiry(LocalDateTime.now().plusHours(1));
//            sendVerificationEmail(userAccount);
//            userAccountRepository.save(userAccount);
//        } else {
//            throw new RuntimeException("User not found");
//        }
//    }
}
