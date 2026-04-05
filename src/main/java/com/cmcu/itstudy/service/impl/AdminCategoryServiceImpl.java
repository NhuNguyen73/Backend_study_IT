package com.cmcu.itstudy.service.impl;

import com.cmcu.itstudy.dto.admin.category.AdminCategoryCreateRequestDto;
import com.cmcu.itstudy.dto.admin.category.AdminCategoryPageResponseDto;
import com.cmcu.itstudy.dto.admin.category.AdminCategoryResponseDto;
import com.cmcu.itstudy.dto.admin.category.AdminCategoryStatusPatchRequestDto;
import com.cmcu.itstudy.dto.admin.category.AdminCategoryUpdateRequestDto;
import com.cmcu.itstudy.entity.Category;
import com.cmcu.itstudy.mapper.AdminCategoryMapper;
import com.cmcu.itstudy.repository.CategoryRepository;
import com.cmcu.itstudy.service.contract.AdminCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    private final CategoryRepository categoryRepository;

    public AdminCategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminCategoryPageResponseDto listCategories(int page, int size) {
        int p = Math.max(0, page);
        int s = size > 0 ? Math.min(size, MAX_PAGE_SIZE) : DEFAULT_PAGE_SIZE;
        Pageable pageable = PageRequest.of(p, s, Sort.by(Sort.Direction.ASC, "displayOrder", "name"));
        Page<Category> result = categoryRepository.findAllPaged(pageable);
        List<AdminCategoryResponseDto> content = result.getContent().stream()
                .map(AdminCategoryMapper::toDto)
                .collect(Collectors.toList());
        return AdminCategoryPageResponseDto.builder()
                .content(content)
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminCategoryResponseDto getCategory(UUID id) {
        Category c = categoryRepository.findByIdWithParent(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return AdminCategoryMapper.toDto(c);
    }

    @Override
    @Transactional
    public AdminCategoryResponseDto createCategory(AdminCategoryCreateRequestDto request) {
        String name = request.getName().trim();
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("Category name already exists");
        }
        String slug = resolveSlug(request.getSlug(), name);
        if (categoryRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Category slug already exists");
        }
        Category parent = null;
        if (request.getParentId() != null) {
            parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
        }
        int order = request.getDisplayOrder() != null ? request.getDisplayOrder() : 0;
        Category c = Category.builder()
                .name(name)
                .slug(slug)
                .description(trimToNull(request.getDescription()))
                .parent(parent)
                .displayOrder(order)
                .active(Boolean.TRUE)
                .build();
        c = categoryRepository.save(c);
        return AdminCategoryMapper.toDto(categoryRepository.findByIdWithParent(c.getId()).orElseThrow());
    }

    @Override
    @Transactional
    public AdminCategoryResponseDto updateCategory(UUID id, AdminCategoryUpdateRequestDto request) {
        boolean hasChange = (request.getName() != null && !request.getName().isBlank())
                || (request.getSlug() != null && !request.getSlug().isBlank())
                || request.getDescription() != null
                || request.isParentIdInRequest()
                || request.getDisplayOrder() != null;
        if (!hasChange) {
            throw new IllegalArgumentException("No fields to update");
        }

        Category c = categoryRepository.findByIdWithParent(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        if (request.getName() != null && !request.getName().isBlank()) {
            String newName = request.getName().trim();
            if (!newName.equals(c.getName()) && categoryRepository.existsByNameAndIdNot(newName, id)) {
                throw new IllegalArgumentException("Category name already exists");
            }
            c.setName(newName);
        }
        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            String newSlug = request.getSlug().trim();
            if (!newSlug.equals(c.getSlug()) && categoryRepository.existsBySlugAndIdNot(newSlug, id)) {
                throw new IllegalArgumentException("Category slug already exists");
            }
            c.setSlug(newSlug);
        }
        if (request.getDescription() != null) {
            c.setDescription(trimToNull(request.getDescription()));
        }
        if (request.isParentIdInRequest()) {
            if (request.getParentId() == null) {
                c.setParent(null);
            } else {
                if (request.getParentId().equals(id)) {
                    throw new IllegalArgumentException("Category cannot be its own parent");
                }
                Category parent = categoryRepository.findById(request.getParentId())
                        .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
                c.setParent(parent);
            }
        }
        if (request.getDisplayOrder() != null) {
            c.setDisplayOrder(request.getDisplayOrder());
        }
        categoryRepository.save(c);
        return AdminCategoryMapper.toDto(categoryRepository.findByIdWithParent(id).orElseThrow());
    }

    @Override
    @Transactional
    public AdminCategoryResponseDto patchStatus(UUID id, AdminCategoryStatusPatchRequestDto request) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        c.setActive(request.getActive());
        categoryRepository.save(c);
        return AdminCategoryMapper.toDto(categoryRepository.findByIdWithParent(id).orElseThrow());
    }

    private static String resolveSlug(String requestedSlug, String name) {
        String raw = requestedSlug != null && !requestedSlug.isBlank() ? requestedSlug.trim() : null;
        return slugify(raw != null ? raw : name);
    }

    private static String slugify(String input) {
        if (input == null || input.isBlank()) {
            return "category";
        }
        String s = Normalizer.normalize(input.trim(), Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        s = s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("^-+|-+$", "");
        return s.isEmpty() ? "category" : s;
    }

    private static String trimToNull(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
