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
public class TeachersPayloadDetails extends TeachersPayload {

    private String inn;
    private String inps;
    private String passportSeries;
    private String passportNumber;
    private String address;

    private FilesPayload diploma;
    private FilesPayload passport;
    private FilesPayload avatar;

    public TeachersPayloadDetails(UUID id,
                                  String firstName,
                                  String lastName,
                                  String sureName,
                                  String dateOfBirth,
                                  String phoneNumber,
                                  String email,
                                  String inn,
                                  String inps,
                                  String passportSeries,
                                  String passportNumber,
                                  String address,
                                  FilesPayload diploma,
                                  FilesPayload passport,
                                  FilesPayload avatar) {
        super(id, firstName, lastName, sureName, dateOfBirth, phoneNumber, email);
        this.inn = inn;
        this.inps = inps;
        this.passportSeries = passportSeries;
        this.passportNumber = passportNumber;
        this.address = address;
        this.diploma = diploma;
        this.passport = passport;
        this.avatar = avatar;
    }
}
