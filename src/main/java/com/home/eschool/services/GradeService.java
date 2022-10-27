package com.home.eschool.services;

import com.home.eschool.entity.Grade;
import com.home.eschool.models.dto.GradeDto;
import com.home.eschool.models.dto.GradeListDto;
import com.home.eschool.models.payload.GradeListPayload;
import com.home.eschool.models.payload.ReferencePayload;
import com.home.eschool.repository.GradeRepo;
import com.home.eschool.utils.Settings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GradeService {

    private final GradeRepo gradeRepo;
    private final StudentClassesService studentClassesService;
    private final StudentsService studentsService;
    private final SubjectsService subjectsService;

    public GradeService(GradeRepo gradeRepo,
                        StudentClassesService studentClassesService,
                        StudentsService studentsService,
                        SubjectsService subjectsService) {
        this.gradeRepo = gradeRepo;
        this.studentClassesService = studentClassesService;
        this.studentsService = studentsService;
        this.subjectsService = subjectsService;
    }

    public List<GradeListPayload> list(GradeListDto dto) {

        List<GradeListPayload> list = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        studentClassesService.getStudentsByClass(dto.getClassId()).forEach(e -> {

                    Grade grade = gradeRepo.getGrade(e.getId(), dto.getSubjectId(), Date.valueOf(dto.getGradeDate()));

                    GradeListPayload payload;
                    if (grade == null) {
                        payload = new GradeListPayload(
                                new ReferencePayload(
                                        e.getId(),
                                        e.getName()),
                                null);
                    } else {
                        payload = new GradeListPayload(
                                new ReferencePayload(
                                        e.getId(),
                                        e.getName()),
                                grade.getGradeValue(),
                                simpleDateFormat.format(grade.getGradeDate()),
                                grade.getGradeReason());
                    }

                    list.add(payload);
                }
        );

        return list;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void save(GradeDto dto) {

        Grade grade = gradeRepo.getGrade(dto.getStudentId(), dto.getSubjectId(),
                Date.valueOf(dto.getGradeDate()));

        if (grade == null && dto.getGradeValue() == 0) {
            return;
        }

        if (grade != null && dto.getGradeValue() == 0) {
            gradeRepo.delete(grade);
            return;
        }

        if (grade == null && dto.getGradeValue() > 0) {
            grade = new Grade();
            grade.setId(UUID.randomUUID());
            grade.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
            grade.setCreateUser(Settings.getCurrentUser());
            grade.setGradeDate(Date.valueOf(dto.getGradeDate()));
            grade.setGradeReason(dto.getGradeReason());
            grade.setGradeValue(dto.getGradeValue());
            grade.setStudents(studentsService.getStudentById(dto.getStudentId()));
            grade.setSubjects(subjectsService.findById(dto.getSubjectId()));
            gradeRepo.save(grade);
            return;
        }

        if (grade != null && dto.getGradeValue() > 0) {
            grade.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
            grade.setChangeUser(Settings.getCurrentUser());
            grade.setGradeReason(dto.getGradeReason());
            grade.setGradeValue(dto.getGradeValue());
            gradeRepo.save(grade);
        }
    }
}
