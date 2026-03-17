package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.auth.ForgotPasswordRequestDto;
import com.cmcu.itstudy.dto.auth.LoginRequestDto;
import com.cmcu.itstudy.dto.auth.RefreshRequestDto;
import com.cmcu.itstudy.dto.auth.RegisterRequestDto;
import com.cmcu.itstudy.dto.auth.ResetPasswordRequestDto;
import com.cmcu.itstudy.dto.auth.TokenResponseDto;
import com.cmcu.itstudy.dto.auth.UserInfoDto;
import com.cmcu.itstudy.dto.common.MessageResponseDto;
import com.cmcu.itstudy.entity.User;

public interface AuthService {

    MessageResponseDto register(RegisterRequestDto request);

    TokenResponseDto login(LoginRequestDto request);

    TokenResponseDto refreshToken(RefreshRequestDto request);

    MessageResponseDto logout(String refreshToken);

    MessageResponseDto logoutAll(User currentUser);

    UserInfoDto getCurrentUser(User currentUser);

    MessageResponseDto forgotPassword(ForgotPasswordRequestDto request);

    MessageResponseDto resetPassword(ResetPasswordRequestDto request);
}

