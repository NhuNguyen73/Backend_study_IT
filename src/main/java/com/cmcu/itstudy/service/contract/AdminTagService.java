package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.admin.tag.AdminTagCreateRequestDto;
import com.cmcu.itstudy.dto.admin.tag.AdminTagPageResponseDto;
import com.cmcu.itstudy.dto.admin.tag.AdminTagResponseDto;
import com.cmcu.itstudy.dto.admin.tag.AdminTagStatusPatchRequestDto;
import com.cmcu.itstudy.dto.admin.tag.AdminTagUpdateRequestDto;

import java.util.UUID;

public interface AdminTagService {

    AdminTagPageResponseDto listTags(int page, int size);

    AdminTagResponseDto getTag(UUID id);

    AdminTagResponseDto createTag(AdminTagCreateRequestDto request);

    AdminTagResponseDto updateTag(UUID id, AdminTagUpdateRequestDto request);

    AdminTagResponseDto patchStatus(UUID id, AdminTagStatusPatchRequestDto request);
}
