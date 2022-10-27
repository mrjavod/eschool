package com.home.eschool.repository;

import com.home.eschool.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttendanceRepo extends JpaRepository<Attendance, UUID> {
}
