package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.request.LoginRequest;
import com.cmcu.itstudy.dto.request.RegisterRequest;
import com.cmcu.itstudy.dto.response.LoginResponse;
import com.cmcu.itstudy.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}

