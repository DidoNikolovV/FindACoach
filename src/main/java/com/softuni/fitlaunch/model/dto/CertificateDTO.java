package com.softuni.fitlaunch.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CertificateDTO {

    private Long id;
    private String name;
    private String issuingAuthority;
    private String issueDate;
}
