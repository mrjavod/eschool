package com.home.eschool.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeachersDto {

    private UUID id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String sureName;

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
    private String passportNumber;

    @NotNull
    @Schema(required = true, description = "Tug'ilgan sana: format(yyyy-mm-dd)", example = "30-09-1995")
    private String dateOfBirth;

    @NotNull
    @Schema(required = true)
    private String phoneNumber;
    private String secondPhoneNumber;
    private String address;

    @Email
    @Schema(example = "test@test.com")
    private String email;
}
