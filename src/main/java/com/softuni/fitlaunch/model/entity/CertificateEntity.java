package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "certificates")
@Getter
@Setter
public class CertificateEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;
    @Column(name = "issuing_authority", nullable = false)
    private String issuingAuthority;

    @Column(name = "issue_date", nullable = false)
    private String issueDate;

}
