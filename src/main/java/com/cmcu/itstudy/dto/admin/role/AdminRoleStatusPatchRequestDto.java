package com.cmcu.itstudy.dto.admin.role;

import jakarta.validation.constraints.NotNull;
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
public class AdminRoleStatusPatchRequestDto {

    /** {@code false} = soft-deleted (inactive); {@code true} = restored / active. */
    @NotNull(message = "active is required")
    private Boolean active;
}
