package com.cmcu.itstudy.dto.admin.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionIdsRequestDto {

    @Builder.Default
    private List<UUID> permissionIds = new ArrayList<>();
}
