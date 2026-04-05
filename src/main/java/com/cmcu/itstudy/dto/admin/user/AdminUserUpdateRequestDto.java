package com.cmcu.itstudy.dto.admin.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserUpdateRequestDto {

    @Email(message = "Email must be a valid email address")
    private String email;

    private String fullName;

    private String avatar;

    private Boolean emailVerified;

    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
    private String password;
}
