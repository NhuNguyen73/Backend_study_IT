package com.cmcu.itstudy.dto.admin.user;

import jakarta.validation.constraints.NotNull;
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
public class AdminAssignRoleRequestDto {

    @NotNull(message = "roleId is required")
    private UUID roleId;
}
