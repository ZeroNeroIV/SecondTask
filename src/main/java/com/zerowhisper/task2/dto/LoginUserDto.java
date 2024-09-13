package com.zerowhisper.task2.dto;

import lombok.Getter;

@Getter
public class LoginUserDto {
    private final String username;
    private final String password;

    LoginUserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
