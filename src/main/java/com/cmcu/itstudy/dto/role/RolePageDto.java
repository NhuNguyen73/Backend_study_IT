package com.cmcu.itstudy.dto.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePageDto {

    private List<RoleDto> content;
    private Integer page;
    private Integer size;
    private Integer total;
}

