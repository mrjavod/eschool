package com.home.eschool.models.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GradeListPayload {

    private ReferencePayload student;
    private Integer gradeValue;
    private String gradeDate;
    private String gradeReason;

    public GradeListPayload(ReferencePayload student, Integer gradeValue) {
        this.student = student;
        this.gradeValue = gradeValue;
    }
}
