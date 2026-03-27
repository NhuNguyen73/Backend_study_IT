package com.cmcu.itstudy.service.impl;

import com.cmcu.itstudy.dto.menu.MenuDto;
import com.cmcu.itstudy.entity.Menu;
import com.cmcu.itstudy.mapper.MenuMapper;
import com.cmcu.itstudy.repository.MenuRepository;
import com.cmcu.itstudy.service.contract.MenuService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    @Transactional
    public List<MenuDto> getMenusForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities() == null) {
            return List.of();
        }

        List<String> permissionNames = authentication.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .filter(permissionName -> permissionName != null && !permissionName.isBlank())
                .distinct()
                .toList();

        if (permissionNames.isEmpty()) {
            return List.of();
        }

        List<Menu> allowedMenus = menuRepository.findMenusByPermissionNames(permissionNames);
        if (allowedMenus.isEmpty()) {
            return List.of();
        }

        Set<Menu> visibleMenus = new LinkedHashSet<>(allowedMenus);
        for (Menu menu : allowedMenus) {
            Menu current = menu.getParent();
            while (current != null) {
                visibleMenus.add(current);
                current = current.getParent();
            }
        }

        List<Menu> sortedMenus = visibleMenus.stream()
                .sorted(Comparator.comparing(Menu::getDisplayOrder, Comparator.nullsLast(Integer::compareTo)))
                .toList();

        Map<UUID, Menu> menuById = sortedMenus.stream()
                .collect(Collectors.toMap(Menu::getId, menu -> menu, (left, right) -> left, LinkedHashMap::new));

        Map<UUID, MenuDto> dtoById = new LinkedHashMap<>();
        for (Menu menu : sortedMenus) {
            MenuDto menuDto = MenuMapper.toMenuDtoWithoutChildren(menu);
            menuDto.setChildren(new ArrayList<>());
            dtoById.put(menu.getId(), menuDto);
        }

        List<MenuDto> roots = new ArrayList<>();
        for (Menu menu : sortedMenus) {
            MenuDto currentDto = dtoById.get(menu.getId());
            Menu parent = menu.getParent();
            if (parent != null && dtoById.containsKey(parent.getId())) {
                dtoById.get(parent.getId()).getChildren().add(currentDto);
            } else {
                roots.add(currentDto);
            }
        }

        for (MenuDto root : roots) {
            sortChildren(root, menuById);
        }

        return roots;
    }

    private void sortChildren(MenuDto parent, Map<UUID, Menu> menuById) {
        if (parent.getChildren() == null || parent.getChildren().isEmpty()) {
            return;
        }

        parent.getChildren().sort(Comparator.comparing(
                child -> {
                    if (child.getId() == null) {
                        return null;
                    }
                    Menu menu = menuById.get(UUID.fromString(child.getId()));
                    return menu != null ? menu.getDisplayOrder() : null;
                },
                Comparator.nullsLast(Integer::compareTo)
        ));

        for (MenuDto child : parent.getChildren()) {
            sortChildren(child, menuById);
        }
    }
}
