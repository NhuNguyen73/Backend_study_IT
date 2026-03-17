package com.cmcu.itstudy.mapper;

import com.cmcu.itstudy.dto.menu.MenuDto;
import com.cmcu.itstudy.entity.Menu;

import java.util.List;
import java.util.stream.Collectors;

public final class MenuMapper {

    private MenuMapper() {
    }

    public static MenuDto toMenuDto(Menu menu) {
        if (menu == null) {
            return null;
        }

        List<MenuDto> childDtos = null;
        if (menu.getChildren() != null) {
            childDtos = menu.getChildren()
                    .stream()
                    .map(MenuMapper::toMenuDto)
                    .collect(Collectors.toList());
        }

        return MenuDto.builder()
                .id(menu.getId() != null ? menu.getId().toString() : null)
                .name(menu.getName())
                .route(menu.getRoute())
                .children(childDtos)
                .build();
    }
}

