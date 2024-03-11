package com.softuni.fitlaunch.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "user_profiles")
public class UserProfileEntity extends BaseEntity {

    private String name;
    private String email;
    private String location;
    private String avatarUrl;
    private String joinedDate;
    private String lastLoginDate;

    private String membership;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    private UserEntity user;

}
