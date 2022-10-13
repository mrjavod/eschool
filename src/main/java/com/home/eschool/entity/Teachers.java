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
    private String pnfl;
    private UUID diploma_id;
    private UUID second_diploma_id;
    private UUID passport_id;
    private UUID avatar_id;
    private UUID covid_test_id;
    private UUID reference_086_id;
    private String passportSeries;

    @Column(unique = true)
    private String passportNumber;

    private Date dateOfBirth;

    @Column(unique = true)
    private String phoneNumber;
    private String secondPhoneNumber;

    @Column(columnDefinition = "text")
    private String address;

    private String email;

    @OneToOne
    private Users profile;

    @OneToOne
    private States states;
}
