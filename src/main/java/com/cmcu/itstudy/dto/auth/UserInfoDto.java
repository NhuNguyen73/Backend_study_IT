package com.cmcu.itstudy.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    private String id;
    private String email;
    private String fullName;
    private String phone;
    private String bio;
    private String avatar;
    private String status;
    private Boolean emailVerified;
    private List<String> roles;
    private List<String> permissions;
}

