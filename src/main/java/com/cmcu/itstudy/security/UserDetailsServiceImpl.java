package com.cmcu.itstudy.security;

import com.cmcu.itstudy.entity.Permission;
import com.cmcu.itstudy.entity.Role;
import com.cmcu.itstudy.entity.RolePermission;
import com.cmcu.itstudy.entity.User;
import com.cmcu.itstudy.entity.UserRole;
import com.cmcu.itstudy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        java.util.Set<String> permissionCodes = new java.util.HashSet<>();

        if (user.getUserRoles() != null) {
            for (UserRole userRole : user.getUserRoles()) {
                Role role = userRole.getRole();
                if (role == null || Boolean.FALSE.equals(role.getActive())) {
                    continue;
                }
                if (role.getRolePermissions() != null) {
                    for (RolePermission rp : role.getRolePermissions()) {
                        Permission permission = rp.getPermission();
                        if (permission != null && permission.getName() != null) {
                            permissionCodes.add(permission.getName());
                        }
                    }
                }
            }
        }

        java.util.List<GrantedAuthority> authorities = new java.util.ArrayList<>();

        permissionCodes.stream()
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);

        if (user.getUserRoles() != null) {
            for (UserRole userRole : user.getUserRoles()) {
                Role role = userRole.getRole();
                if (role != null && role.getName() != null && !Boolean.FALSE.equals(role.getActive())) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
                }
            }
        }

        return new UserDetailsImpl(user, authorities);
    }
}
