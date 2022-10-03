package com.home.eschool.models.payload;

import com.home.eschool.entity.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserPayload {

    private UUID id;
    private String username;
    private String fullName;
    private RoleEnum role;
    private String roleName;
}
