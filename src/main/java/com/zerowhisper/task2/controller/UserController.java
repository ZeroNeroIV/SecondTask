package com.zerowhisper.task2.controller;


import com.zerowhisper.task2.entity.UserAccount;
import com.zerowhisper.task2.service.UserAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {

    private final UserAccountService userAccountService;


    public UserController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserAccount> currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount currentUserAccount = (UserAccount) authentication.getPrincipal();
        return ResponseEntity.ok(currentUserAccount);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserAccount>> getAllUsers() {
        return ResponseEntity.ok(userAccountService.getAllUsers());
    }

}
