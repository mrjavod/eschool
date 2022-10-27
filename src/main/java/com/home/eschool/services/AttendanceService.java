package com.home.eschool.services;

import com.home.eschool.models.dto.AttendanceDto;
import com.home.eschool.models.dto.AttendanceListDto;
import com.home.eschool.repository.AttendanceRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AttendanceService {

    private final AttendanceRepo attendanceRepo;

    public AttendanceService(AttendanceRepo attendanceRepo) {
        this.attendanceRepo = attendanceRepo;
    }

    public Object list(AttendanceListDto dto) {
        return null;
    }

    public void create(AttendanceDto dto) {
    }

    public Object getById(UUID id) {
        return null;
    }

    public void delete(List<UUID> uuids) {
    }
}
