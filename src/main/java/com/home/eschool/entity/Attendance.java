package com.home.eschool.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Attendance extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Students students;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Classes classes;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subjects subjects;

    private Date attendanceDate;

    private boolean attendanceIsReasonable;

    @Column(columnDefinition = "text")
    private String attendanceReason;
}
