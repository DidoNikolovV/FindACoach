package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails extends User {

    @Getter
    private final Long id;

    private final boolean isActivated;

    public CustomUserDetails(Long id, String username, String password, boolean isActivated, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, isActivated, true, true, true, authorities);
        this.id = id;
        this.isActivated = isActivated;
    }

    @Override
    public boolean isEnabled() {
        return isActivated;
    }


    public static CustomUserDetails create(UserEntity userEntity) {
        Collection<GrantedAuthority> authorities = userEntity.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().name()))
                .collect(Collectors.toList());

        return new CustomUserDetails(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword(), userEntity.isActivated(), authorities);
    }
}
