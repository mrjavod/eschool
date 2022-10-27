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
public class GradeListDto {

    @NotNull
    private UUID classId;
    @NotNull
    private UUID subjectId;
    @NotNull
    private String gradeDate;
}
