package com.home.eschool.repository;

import com.home.eschool.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.UUID;

public interface AttendanceRepo extends JpaRepository<Attendance, UUID> {

    @Query("select t from Attendance t " +
            "where t.classes.id=?1 and t.subjects.id=?2 and t.students.id=?3 and t.attendanceDate=?4")
    Attendance getAttendance(UUID classId, UUID subjectId, UUID studentId, Date date);
}
