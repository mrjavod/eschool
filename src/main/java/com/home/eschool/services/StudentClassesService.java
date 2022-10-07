package com.home.eschool.services;

import com.home.eschool.entity.Classes;
import com.home.eschool.entity.StudentClasses;
import com.home.eschool.entity.Students;
import com.home.eschool.entity.enums.SetsEnum;
import com.home.eschool.entity.enums.StateEnum;
import com.home.eschool.models.payload.ClassesPayload;
import com.home.eschool.repository.StudentClassesRepo;
import com.home.eschool.utils.Settings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentClassesService {

    private final StudentClassesRepo studentClassesRepo;
    private final ClassesService classesService;
    private final AppSettingsService appSettingsService;
    private final StateService stateService;

    public StudentClassesService(StudentClassesRepo studentClassesRepo,
                                 ClassesService classesService,
                                 AppSettingsService appSettingsService,
                                 StateService stateService) {
        this.studentClassesRepo = studentClassesRepo;
        this.classesService = classesService;
        this.appSettingsService = appSettingsService;
        this.stateService = stateService;
    }

    public void create(Students students, UUID classId) {

        Classes classes = classesService.findById(classId);
        if (classes == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect Subject Id");
        }

        StudentClasses studentClasses = new StudentClasses();
        studentClasses.setId(UUID.randomUUID());
        studentClasses.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        studentClasses.setCreateUser(Settings.getCurrentUser());
        studentClasses.setStudents(students);
        studentClasses.setStudyYearId(appSettingsService.getKeyByLabel(SetsEnum.STUDY_YEAR));
        studentClasses.setClasses(classes);
        studentClasses.setStates(stateService.getStateByLabel(StateEnum.ACTIVE));
        studentClassesRepo.save(studentClasses);
    }

    public void update(Students students, UUID classId) {

        Optional<StudentClasses> oldStudentClasses =
                studentClassesRepo.findByStudentsAndStates_Label(students, StateEnum.ACTIVE);

        if (oldStudentClasses.isPresent()) {

            Classes classes = classesService.findById(classId);
            if (classes == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Incorrect Subject Id");
            }

            StudentClasses studentClasses = oldStudentClasses.get();
            studentClasses.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
            studentClasses.setChangeUser(Settings.getCurrentUser());
            studentClasses.setClasses(classes);
            studentClassesRepo.save(studentClasses);
        }
    }

    public ClassesPayload getClassesInfo(Students students) {
        Optional<StudentClasses> studentClasses =
                studentClassesRepo.findByStudentsAndStates_Label(students, StateEnum.ACTIVE);
        if (studentClasses.isPresent()) {
            Classes classes = studentClasses.get().getClasses();
            return new ClassesPayload(classes.getId(), classes.getName());
        }
        return null;
    }
}
