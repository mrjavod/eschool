package com.home.eschool.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeachersSubjectsAndClassesDto {

    private UUID id;
    private UUID teacherId;
    private UUID classId;
    private UUID subjectId;
}
