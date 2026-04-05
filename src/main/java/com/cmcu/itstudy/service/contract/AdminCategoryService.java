package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.admin.category.AdminCategoryCreateRequestDto;
import com.cmcu.itstudy.dto.admin.category.AdminCategoryPageResponseDto;
import com.cmcu.itstudy.dto.admin.category.AdminCategoryResponseDto;
import com.cmcu.itstudy.dto.admin.category.AdminCategoryStatusPatchRequestDto;
import com.cmcu.itstudy.dto.admin.category.AdminCategoryUpdateRequestDto;

import java.util.UUID;

public interface AdminCategoryService {

    AdminCategoryPageResponseDto listCategories(int page, int size);

    AdminCategoryResponseDto getCategory(UUID id);

    AdminCategoryResponseDto createCategory(AdminCategoryCreateRequestDto request);

    AdminCategoryResponseDto updateCategory(UUID id, AdminCategoryUpdateRequestDto request);

    AdminCategoryResponseDto patchStatus(UUID id, AdminCategoryStatusPatchRequestDto request);
}
