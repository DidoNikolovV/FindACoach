package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.UserProfileEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails extends User {

    private final Long id;
    private final UserProfileEntity userProfile;

    private final boolean isActivated;


    public CustomUserDetails(Long id, String username, String password, UserProfileEntity userProfile, boolean isActivated, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, isActivated, true, true, true, authorities);
        this.id = id;
        this.userProfile = userProfile;
        this.isActivated = isActivated;
    }

    public Long getId() {
        return id;
    }

    public UserProfileEntity getUserProfile() {
        return userProfile;
    }


    @Override
    public boolean isEnabled() {
        return isActivated;
    }


    public static CustomUserDetails create(UserEntity userEntity) {
        UserProfileEntity userProfile = userEntity.getUserProfile();

//        Collection<GrantedAuthority> authorities = userEntity.getRoles()
//                .stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().name()))
//                .collect(Collectors.toList());

        Collection<GrantedAuthority> authorities = userEntity.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().name()))
                .collect(Collectors.toList());


//        return new CustomUserDetails(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword(), authorities);

        return new CustomUserDetails(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword(), userProfile, userEntity.isActivated(), authorities);
    }
}
