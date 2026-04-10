package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.auth.UserInfoDto;
import com.cmcu.itstudy.dto.user.UpdateProfileRequestDto;
import com.cmcu.itstudy.entity.User;

public interface UserProfileService {

    UserInfoDto updateProfile(User currentUser, UpdateProfileRequestDto request);
}
