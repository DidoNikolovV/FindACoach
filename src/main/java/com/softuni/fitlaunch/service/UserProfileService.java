package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.UserProfileEntity;
import com.softuni.fitlaunch.repository.UserProfileRepository;
import com.softuni.fitlaunch.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    private final UserRepository userRepository;

    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    public UserProfileEntity getUserProfileById(Long id) {
        UserEntity userEntity = userRepository.findById(id).get();
        UserProfileEntity userProfileEntity = new UserProfileEntity();

        userProfileEntity.setAvatarUrl("/images/profile-avatar.jpg");
        userProfileEntity.setLocation("Bulgaria");
        userProfileEntity.setLastLoginDate(String.valueOf(LocalDate.now()));
        userProfileEntity.setId(userEntity.getId());
        userProfileEntity.setEmail(userEntity.getEmail());
        userProfileEntity.setName(userEntity.getUsername());

        userProfileRepository.save(userProfileEntity);

        return userProfileEntity;

    }
}
