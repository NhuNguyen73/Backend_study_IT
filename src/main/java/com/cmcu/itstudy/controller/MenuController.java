package com.cmcu.itstudy.controller;

import com.cmcu.itstudy.dto.common.ApiResponse;
import com.cmcu.itstudy.dto.menu.MenuDto;
import com.cmcu.itstudy.service.contract.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<MenuDto>>> getMenusForCurrentUser() {
        List<MenuDto> menus = menuService.getMenusForCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(menus, "Menus for current user"));
    }
}
