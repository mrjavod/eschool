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
    private String pnfl;
    private String secondPhoneNumber;
    private String passportSeries;
    private String passportNumber;
    private String address;

    private FilesPayload diploma;
    private FilesPayload secondDiploma;
    private FilesPayload passport;
    private FilesPayload avatar;
    private FilesPayload covidTest;
    private FilesPayload reference086;

    public TeachersPayloadDetails(UUID id,
                                  String firstName,
                                  String lastName,
                                  String sureName,
                                  String dateOfBirth,
                                  String phoneNumber,
                                  String email,
                                  String inn,
                                  String inps,
                                  String pnfl,
                                  String secondPhoneNumber,
                                  String passportSeries,
                                  String passportNumber,
                                  String address,
                                  FilesPayload diploma,
                                  FilesPayload secondDiploma,
                                  FilesPayload passport,
                                  FilesPayload avatar,
                                  FilesPayload covidTest,
                                  FilesPayload reference086) {
        super(id, firstName, lastName, sureName, dateOfBirth, phoneNumber, email);
        this.inn = inn;
        this.inps = inps;
        this.pnfl = pnfl;
        this.secondPhoneNumber = secondPhoneNumber;
        this.passportSeries = passportSeries;
        this.passportNumber = passportNumber;
        this.address = address;
        this.diploma = diploma;
        this.secondDiploma = secondDiploma;
        this.passport = passport;
        this.avatar = avatar;
        this.covidTest = covidTest;
        this.reference086 = reference086;
    }
}
