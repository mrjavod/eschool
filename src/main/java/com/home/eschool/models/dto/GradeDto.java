package com.home.eschool.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GradeDto {

    @NotNull
    private UUID subjectId;
    @NotNull
    private UUID studentId;
    @NotNull
    private String gradeDate;
    @NotNull
    private int gradeValue;
    @NotNull
    private String gradeReason;
}
