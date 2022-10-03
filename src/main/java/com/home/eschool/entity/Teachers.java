package com.home.eschool.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.sql.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Teachers extends BaseEntity {

    private String firstName;
    private String lastName;
    private String sureName;

    @Column(unique = true)
    private String inn;

    private String inps;
    private UUID diploma_id;
    private UUID passport_id;
    private UUID avatar_id;
    private String passportSeries;

    @Column(unique = true)
    private String passportNumber;

    private Date dateOfBirth;
    private String phoneNumber;

    @Column(columnDefinition = "text")
    private String address;

    @Column(unique = true)
    private String email;

    @OneToOne
    private Users profile;
}
