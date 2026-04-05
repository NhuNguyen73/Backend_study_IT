package com.cmcu.itstudy.dto.admin.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
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
public class AdminCategoryUpdateRequestDto {

    @Size(max = 150)
    private String name;

    @Size(max = 180)
    private String slug;

    @Size(max = 500)
    private String description;

    @Setter(AccessLevel.NONE)
    private UUID parentId;

    @JsonIgnore
    @Builder.Default
    private boolean parentIdInRequest = false;

    @JsonProperty("parentId")
    public void setParentId(UUID value) {
        this.parentId = value;
        this.parentIdInRequest = true;
    }

    private Integer displayOrder;
}
