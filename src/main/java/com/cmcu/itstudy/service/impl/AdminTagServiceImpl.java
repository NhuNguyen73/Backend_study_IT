package com.cmcu.itstudy.service.impl;

import com.cmcu.itstudy.dto.admin.tag.AdminTagCreateRequestDto;
import com.cmcu.itstudy.dto.admin.tag.AdminTagPageResponseDto;
import com.cmcu.itstudy.dto.admin.tag.AdminTagResponseDto;
import com.cmcu.itstudy.dto.admin.tag.AdminTagStatusPatchRequestDto;
import com.cmcu.itstudy.dto.admin.tag.AdminTagUpdateRequestDto;
import com.cmcu.itstudy.entity.Tag;
import com.cmcu.itstudy.mapper.AdminTagMapper;
import com.cmcu.itstudy.repository.TagRepository;
import com.cmcu.itstudy.service.contract.AdminTagService;
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
public class AdminTagServiceImpl implements AdminTagService {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    private final TagRepository tagRepository;

    public AdminTagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminTagPageResponseDto listTags(int page, int size) {
        int p = Math.max(0, page);
        int s = size > 0 ? Math.min(size, MAX_PAGE_SIZE) : DEFAULT_PAGE_SIZE;
        Pageable pageable = PageRequest.of(p, s, Sort.by(Sort.Direction.ASC, "name"));
        Page<Tag> result = tagRepository.findAll(pageable);
        List<AdminTagResponseDto> content = result.getContent().stream()
                .map(AdminTagMapper::toDto)
                .collect(Collectors.toList());
        return AdminTagPageResponseDto.builder()
                .content(content)
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminTagResponseDto getTag(UUID id) {
        Tag t = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));
        return AdminTagMapper.toDto(t);
    }

    @Override
    @Transactional
    public AdminTagResponseDto createTag(AdminTagCreateRequestDto request) {
        String name = request.getName().trim();
        if (tagRepository.existsByName(name)) {
            throw new IllegalArgumentException("Tag name already exists");
        }
        String slug = resolveSlug(request.getSlug(), name);
        if (tagRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Tag slug already exists");
        }
        Tag t = Tag.builder()
                .name(name)
                .slug(slug)
                .usageCount(0L)
                .active(Boolean.TRUE)
                .build();
        t = tagRepository.save(t);
        return AdminTagMapper.toDto(t);
    }

    @Override
    @Transactional
    public AdminTagResponseDto updateTag(UUID id, AdminTagUpdateRequestDto request) {
        boolean hasChange = (request.getName() != null && !request.getName().isBlank())
                || (request.getSlug() != null && !request.getSlug().isBlank());
        if (!hasChange) {
            throw new IllegalArgumentException("No fields to update");
        }

        Tag t = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));

        if (request.getName() != null && !request.getName().isBlank()) {
            String newName = request.getName().trim();
            if (!newName.equals(t.getName()) && tagRepository.existsByNameAndIdNot(newName, id)) {
                throw new IllegalArgumentException("Tag name already exists");
            }
            t.setName(newName);
        }
        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            String newSlug = request.getSlug().trim();
            if (!newSlug.equals(t.getSlug()) && tagRepository.existsBySlugAndIdNot(newSlug, id)) {
                throw new IllegalArgumentException("Tag slug already exists");
            }
            t.setSlug(newSlug);
        }
        tagRepository.save(t);
        return AdminTagMapper.toDto(tagRepository.findById(id).orElseThrow());
    }

    @Override
    @Transactional
    public AdminTagResponseDto patchStatus(UUID id, AdminTagStatusPatchRequestDto request) {
        Tag t = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));
        t.setActive(request.getActive());
        tagRepository.save(t);
        return AdminTagMapper.toDto(t);
    }

    private static String resolveSlug(String requestedSlug, String name) {
        String raw = requestedSlug != null && !requestedSlug.isBlank() ? requestedSlug.trim() : null;
        return slugify(raw != null ? raw : name);
    }

    private static String slugify(String input) {
        if (input == null || input.isBlank()) {
            return "tag";
        }
        String s = Normalizer.normalize(input.trim(), Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        s = s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("^-+|-+$", "");
        return s.isEmpty() ? "tag" : s;
    }
}
