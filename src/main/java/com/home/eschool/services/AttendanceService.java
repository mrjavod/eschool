package com.home.eschool.services;

import com.home.eschool.entity.Attendance;
import com.home.eschool.models.dto.AttendanceDto;
import com.home.eschool.models.dto.AttendanceListDto;
import com.home.eschool.models.payload.AttendanceListPayload;
import com.home.eschool.models.payload.ReferencePayload;
import com.home.eschool.repository.AttendanceRepo;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AttendanceService {

    private final AttendanceRepo attendanceRepo;
    private final StudentClassesService studentClassesService;

    public AttendanceService(AttendanceRepo attendanceRepo,
                             StudentClassesService studentClassesService) {
        this.attendanceRepo = attendanceRepo;
        this.studentClassesService = studentClassesService;
    }

    public List<AttendanceListPayload> list(AttendanceListDto dto) {

        List<AttendanceListPayload> list = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        studentClassesService.getStudentsByClass(dto.getClassId()).forEach(e -> {

                    Attendance attendance = attendanceRepo.getAttendance(dto.getClassId(), dto.getSubjectId(), e.getId(), Date.valueOf(dto.getAttendanceDate()));

                    AttendanceListPayload payload;
                    if (attendance == null) {
                        payload = new AttendanceListPayload(
                                new ReferencePayload(
                                        e.getId(),
                                        e.getName()),
                                true);
                    } else {
                        payload = new AttendanceListPayload(
                                new ReferencePayload(
                                        e.getId(),
                                        e.getName()),
                                false,
                                attendance.getId(),
                                simpleDateFormat.format(attendance.getAttendanceDate()),
                                attendance.isAttendanceIsReasonable(),
                                attendance.getAttendanceReason());
                    }

                    list.add(payload);
                }
        );

        return list;
    }

    public void create(AttendanceDto dto) {
    }

    public Object getById(UUID id) {
        return null;
    }

    public void delete(List<UUID> uuids) {
    }
}
