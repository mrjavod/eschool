package com.home.eschool.models.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentsPayload {

    private UUID id;
    private String firstName;
    private String lastName;
    private String sureName;
    private String dateOfBirth;
    private String phoneNumber;
    private ClassesPayload classes;
}
