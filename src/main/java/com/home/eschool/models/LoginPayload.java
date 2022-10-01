package com.home.eschool.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginPayload {

    private String accessToken;
    private UserPayload user;
}
