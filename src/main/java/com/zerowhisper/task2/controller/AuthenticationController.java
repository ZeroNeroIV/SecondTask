package com.zerowhisper.task2.controller;

import com.zerowhisper.task2.dto.LoginUserDto;
import com.zerowhisper.task2.dto.RegisterUserDto;
import com.zerowhisper.task2.dto.VerifyUserDto;
import com.zerowhisper.task2.entity.UserAccount;
import com.zerowhisper.task2.repository.UserAccountRepository;
import com.zerowhisper.task2.response.LoginResponse;
import com.zerowhisper.task2.service.AuthenticationService;
import com.zerowhisper.task2.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserAccountRepository userAccountRepository;

    private String currentUsername;
    private String currentRefreshToken;

    public AuthenticationController(
            JwtService jwtService,
            AuthenticationService authenticationService,
            UserAccountRepository userAccountRepository) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userAccountRepository = userAccountRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto registerUserDto) {
        try {
            UserAccount registeredUserAccount = authenticationService.signUp(registerUserDto);
            return ResponseEntity.ok(registeredUserAccount);
        } catch (Exception _) {
            return ResponseEntity.badRequest().body("The email or/and username is/are already in use.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        UserAccount authenticatedUserAccount = authenticationService.authenticate(loginUserDto);
//        if (authenticatedUser.getRefreshToken() != null)
//            return ResponseEntity.ok("Account has a token :D!");
        currentUsername = authenticatedUserAccount.getUsername();
        String accessToken = jwtService.generateToken(authenticatedUserAccount);
        currentRefreshToken = accessToken;
        LoginResponse loginResponse = new LoginResponse(accessToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifiedUser(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account Verified Successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Wrong code OR account verified");
        }
    }

    // NO NEED FOR THIS --> same as "/verify"
//    @PostMapping("/resend")
//    public ResponseEntity<String> resendVerificationCode(@RequestBody String email) {
//        try {
//            if (userAccountRepository.findByEmail(email).get().isEnabled())
//                return ResponseEntity.badRequest().body("Account Already Verified");
//            authenticationService.resendVerificationToken(email);
//            return ResponseEntity.ok("Verification Code Sent");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @GetMapping("/logout")
    public ResponseEntity<?> logOut() {
        Optional<UserAccount> optionalUser = userAccountRepository.findByUsername(currentUsername);
        if (optionalUser.isEmpty())
            return ResponseEntity.badRequest().build();
        UserAccount userAccount = optionalUser.get();
//        user.removeRefreshToken();
        // update the database
        currentRefreshToken = currentUsername = null;
        return ResponseEntity.noContent().build();
    }
}

