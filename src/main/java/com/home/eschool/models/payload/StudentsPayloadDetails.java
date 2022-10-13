package com.home.eschool.models.payload;

import com.fasterxml.jackson.databind.JsonNode;
import com.home.eschool.entity.addinfo.BirthInfo;
import com.home.eschool.entity.addinfo.Parents;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentsPayloadDetails extends StudentsPayload {

    private String address;
    private FilesPayload avatar;
    private Parents mother;
    private Parents father;
    private BirthInfo birthInfo;
    private BigDecimal monthlyPayment;
    private JsonNode additionalInfo;

    public StudentsPayloadDetails(UUID id,
                                  String firstName,
                                  String lastName,
                                  String sureName,
                                  String dateOfBirth,
                                  String phoneNumber,
                                  ClassesPayload classes,
                                  String address,
                                  FilesPayload avatar,
                                  Parents mother,
                                  Parents father,
                                  BirthInfo birthInfo,
                                  BigDecimal monthlyPayment,
                                  JsonNode additionalInfo) {
        super(id, firstName, lastName, sureName, dateOfBirth, phoneNumber, classes);
        this.address = address;
        this.avatar = avatar;
        this.mother = mother;
        this.father = father;
        this.birthInfo = birthInfo;
        this.monthlyPayment = monthlyPayment;
        this.additionalInfo = additionalInfo;
    }
}
