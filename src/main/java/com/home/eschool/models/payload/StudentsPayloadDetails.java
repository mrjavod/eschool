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
public class StudentsPayloadDetails extends StudentsPayload {

    private String address;
    private FilesPayload avatar;

    public StudentsPayloadDetails(UUID id,
                                  String firstName,
                                  String lastName,
                                  String sureName,
                                  String dateOfBirth,
                                  String phoneNumber,
                                  String address,
                                  FilesPayload avatar,
                                  ClassesPayload classesPayload) {
        super(id, firstName, lastName, sureName, dateOfBirth, phoneNumber, classesPayload);
        this.address = address;
        this.avatar = avatar;
    }
}
