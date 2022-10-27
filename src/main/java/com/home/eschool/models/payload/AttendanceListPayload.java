package com.home.eschool.models.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceListPayload {

    private ReferencePayload student;
    private boolean attendanceStatus;
    private UUID attendanceId;
    private String attendanceDate;
    private boolean attendanceIsReasonable;
    private String attendanceReason;

    public AttendanceListPayload(ReferencePayload student, boolean attendanceStatus) {
        this.student = student;
        this.attendanceStatus = attendanceStatus;
    }
}
