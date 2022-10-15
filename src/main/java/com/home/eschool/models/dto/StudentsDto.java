package com.home.eschool.models.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.home.eschool.entity.addinfo.BirthInfo;
import com.home.eschool.entity.addinfo.Parents;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
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

    private Parents mother;
    private Parents father;
    private BirthInfo birthInfo;
    private BigDecimal monthlyPayment;
    private JsonNode additionalInfo;
}
