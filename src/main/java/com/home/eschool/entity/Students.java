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
public class Students extends BaseEntity {

    private String firstName;
    private String lastName;
    private String sureName;
    private Date dateOfBirth;
    private String phoneNumber;

    @Column(columnDefinition = "text")
    private String address;
    private UUID avatar_id;

    @OneToOne
    private States state;
}
