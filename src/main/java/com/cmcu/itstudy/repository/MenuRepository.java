package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, UUID> {

    List<Menu> findByParentIsNullOrderByDisplayOrderAsc();

    @Query("""
            select distinct m
            from Menu m
            join m.menuPermissions mp
            join mp.permission p
            where p.name in :permissionNames
            order by m.displayOrder asc
            """)
    List<Menu> findMenusByPermissionNames(@Param("permissionNames") List<String> permissionNames);
}

