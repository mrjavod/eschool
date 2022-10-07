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
public class StudentsDto {

    private UUID id;

    @NotNull
    @Schema(required = true)
    private String firstName;

    @NotNull
    @Schema(required = true)
    private String lastName;

    @NotNull
    @Schema(required = true)
    private String sureName;

    private UUID avatar_id;

    @Schema(description = "Tug'ilgan sana: format(yyyy-mm-dd)", example = "1995-09-30", required = true)
    private String dateOfBirth;

    private String phoneNumber;
    private String address;

    @Schema(description = "O'qiydigan sinfi", required = true)
    private UUID classId;

}
