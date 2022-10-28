package com.home.eschool.services;

import com.home.eschool.entity.Attendance;
import com.home.eschool.models.dto.AttendanceDto;
import com.home.eschool.models.dto.AttendanceListDto;
import com.home.eschool.models.payload.AttendanceListPayload;
import com.home.eschool.models.payload.ReferencePayload;
import com.home.eschool.repository.AttendanceRepo;
import com.home.eschool.utils.Settings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttendanceService {

    private final AttendanceRepo attendanceRepo;
    private final StudentClassesService studentClassesService;
    private final StudentsService studentsService;
    private final SubjectsService subjectsService;
    private final ClassesService classesService;

    public AttendanceService(AttendanceRepo attendanceRepo,
                             StudentClassesService studentClassesService,
                             StudentsService studentsService,
                             SubjectsService subjectsService,
                             ClassesService classesService) {
        this.attendanceRepo = attendanceRepo;
        this.studentClassesService = studentClassesService;
        this.studentsService = studentsService;
        this.subjectsService = subjectsService;
        this.classesService = classesService;
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

    @Transactional(rollbackFor = Throwable.class)
    public void save(AttendanceDto dto) {

        Attendance attendance = attendanceRepo.getAttendance(dto.getClassId(),
                dto.getSubjectId(), dto.getStudentId(), Date.valueOf(dto.getAttendanceDate()));

        if (attendance == null) {
            attendance = new Attendance();
            attendance.setId(UUID.randomUUID());
            attendance.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
            attendance.setCreateUser(Settings.getCurrentUser());
            attendance.setAttendanceDate(Date.valueOf(dto.getAttendanceDate()));
            attendance.setAttendanceIsReasonable(dto.isAttendanceIsReasonable());
            attendance.setAttendanceReason(dto.getAttendanceReason());
            attendance.setClasses(classesService.findById(dto.getClassId()));
            attendance.setStudents(studentsService.getStudentById(dto.getStudentId()));
            attendance.setSubjects(subjectsService.findById(dto.getSubjectId()));
        } else {
            attendance.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
            attendance.setChangeUser(Settings.getCurrentUser());
            attendance.setAttendanceIsReasonable(dto.isAttendanceIsReasonable());
            attendance.setAttendanceReason(dto.getAttendanceReason());
        }

        attendanceRepo.save(attendance);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void delete(List<UUID> uuids) {
        uuids.forEach(s -> {
            Optional<Attendance> optional = attendanceRepo.findById(s);
            if (optional.isPresent()) {
                attendanceRepo.delete(optional.get());
            } else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Noto'g'ri ID yuborildi !");
            }
        });
    }
}
