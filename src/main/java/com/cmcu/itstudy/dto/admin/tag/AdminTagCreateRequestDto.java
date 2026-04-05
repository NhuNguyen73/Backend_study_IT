package com.cmcu.itstudy.dto.admin.tag;

import jakarta.validation.constraints.NotBlank;
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
public class AdminTagCreateRequestDto {

    @NotBlank(message = "Name is required")
    @Size(max = 100)
    private String name;

    @Size(max = 120)
    private String slug;
}
