package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "progress_pictures")
@Getter
@Setter
public class ProgressPicture extends BaseEntity{

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;
}
