package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.menu.MenuDto;

import java.util.List;

public interface MenuService {

    List<MenuDto> getMenusForCurrentUser();
}
