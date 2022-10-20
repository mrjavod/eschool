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
public class TeachersSubjectsAndClassesDto {

    private UUID id;
    @NotNull
    private UUID teacherId;
    @NotNull
    private UUID classId;
    @NotNull
    private UUID subjectId;
}
