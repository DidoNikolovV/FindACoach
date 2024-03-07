package com.softuni.fitlaunch.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "user_profiles")
public class UserProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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


    public Long getId() {
        return id;
    }

    public UserProfileEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserProfileEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserProfileEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public UserProfileEntity setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public UserProfileEntity setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getJoinedDate() {
        return joinedDate;
    }

    public UserProfileEntity setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
        return this;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public UserProfileEntity setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public UserProfileEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public String getMembership() {
        return membership;
    }

    public UserProfileEntity setMembership(String membershipStatus) {
        this.membership = membershipStatus;
        return this;
    }
}
