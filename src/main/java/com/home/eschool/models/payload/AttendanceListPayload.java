package com.home.eschool.models.payload;

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
public class AttendanceListPayload {

    private ReferencePayload students;
    private boolean attendanceStatus;
    private UUID attendanceId;
    private String attendanceDate;
    private boolean attendanceIsReasonable;
    private String attendanceReason;

    public AttendanceListPayload(ReferencePayload students, boolean attendanceStatus) {
        this.students = students;
        this.attendanceStatus = attendanceStatus;
    }
}
