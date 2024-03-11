package com.softuni.fitlaunch.model.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BlacklistEntity extends BaseEntity {

    private String ipAddress;

}
