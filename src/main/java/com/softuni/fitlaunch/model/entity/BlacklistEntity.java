package com.softuni.fitlaunch.model.entity;

import jakarta.persistence.Entity;

@Entity
public class BlacklistEntity extends BaseEntity {

    private String ipAddress;

    public String getIpAddress() {
        return ipAddress;
    }

    public BlacklistEntity setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }
}
