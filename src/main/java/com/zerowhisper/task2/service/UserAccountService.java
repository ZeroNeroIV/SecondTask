package com.zerowhisper.task2.service;

import com.zerowhisper.task2.entity.UserAccount;
import com.zerowhisper.task2.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountService {

    public final UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public List<UserAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }

}
