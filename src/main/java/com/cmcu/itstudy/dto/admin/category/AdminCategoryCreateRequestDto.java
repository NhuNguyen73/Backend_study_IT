package com.cmcu.itstudy.dto.admin.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCategoryCreateRequestDto {

    @NotBlank(message = "Name is required")
    @Size(max = 150)
    private String name;

    @Size(max = 180)
    private String slug;

    @Size(max = 500)
    private String description;

    private UUID parentId;

    private Integer displayOrder;
}
