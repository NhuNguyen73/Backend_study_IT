package com.cmcu.itstudy.dto.user;

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
public class UpdateProfileRequestDto {

    @Size(max = 255, message = "Họ tên tối đa 255 ký tự")
    private String fullName;

    @Size(max = 32, message = "Số điện thoại tối đa 32 ký tự")
    private String phone;

    @Size(max = 2000, message = "Giới thiệu tối đa 2000 ký tự")
    private String bio;

    @Size(max = 2048, message = "URL ảnh đại diện tối đa 2048 ký tự")
    private String avatarUrl;
}
