package com.home.eschool.services;

import com.home.eschool.entity.*;
import com.home.eschool.entity.enums.SetsEnum;
import com.home.eschool.entity.enums.StateEnum;
import com.home.eschool.models.dto.TeachersSubjectsAndClassesDto;
import com.home.eschool.models.payload.*;
import com.home.eschool.repository.TeachersSubjectsAndClassesRepo;
import com.home.eschool.services.interfaces.CrudInterface;
import com.home.eschool.utils.Settings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeachersSubjectsAndClassesService implements CrudInterface<TeachersSubjectsAndClassesDto, TeachersSubjectsAndClassesPayload> {

    private final TeachersSubjectsAndClassesRepo teachersSubjectsAndClassesRepo;
    private final AppSettingsService appSettingsService;
    private final StateService stateService;
    private final ClassesService classesService;
    private final TeachersService teachersService;
    private final SubjectsService subjectsService;

    public TeachersSubjectsAndClassesService(TeachersSubjectsAndClassesRepo teachersSubjectsAndClassesRepo,
                                             AppSettingsService appSettingsService,
                                             StateService stateService,
                                             ClassesService classesService,
                                             TeachersService teachersService,
                                             SubjectsService subjectsService) {
        this.teachersSubjectsAndClassesRepo = teachersSubjectsAndClassesRepo;
        this.appSettingsService = appSettingsService;
        this.stateService = stateService;
        this.classesService = classesService;
        this.teachersService = teachersService;
        this.subjectsService = subjectsService;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(TeachersSubjectsAndClassesDto t) {

        Classes classes = classesService.findById(t.getClassId());
        Teachers teachers = teachersService.findById(t.getTeacherId());
        Subjects subjects = subjectsService.findById(t.getSubjectId());
        UUID studyYearId = appSettingsService.getKeyByLabel(SetsEnum.STUDY_YEAR);

        Optional<TeachersSubjectsAndClasses> tsc = teachersSubjectsAndClassesRepo.getTeachersSubjectsAndClasses(
                teachers, subjects, classes, studyYearId
        );

        if (!tsc.isPresent()) {

            TeachersSubjectsAndClasses data = new TeachersSubjectsAndClasses();
            data.setId(UUID.randomUUID());
            data.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
            data.setCreateUser(Settings.getCurrentUser());
            data.setStates(stateService.getStateByLabel(StateEnum.ACTIVE));
            data.setStudyYearId(studyYearId);
            data.setClasses(classes);
            data.setTeachers(teachers);
            data.setSubjects(subjects);
            teachersSubjectsAndClassesRepo.save(data);

        } else {

            TeachersSubjectsAndClasses data = tsc.get();
            if (data.getStates().getLabel().equals(StateEnum.DELETED)) {
                data.setStates(stateService.getStateByLabel(StateEnum.ACTIVE));
                teachersSubjectsAndClassesRepo.save(data);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(TeachersSubjectsAndClassesDto t) {

    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(List<UUID> ids) {
        States states = stateService.getStateByLabel(StateEnum.DELETED);
        ids.forEach(s -> {
            Optional<TeachersSubjectsAndClasses> optional = teachersSubjectsAndClassesRepo.findById(s);
            if (optional.isPresent()) {
                TeachersSubjectsAndClasses data = optional.get();
                data.setStates(states);
                data.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
                data.setChangeUser(Settings.getCurrentUser());

                teachersSubjectsAndClassesRepo.save(data);
            } else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Incorrect Id");
            }
        });
    }

    @Override
    public PageablePayload getAll(int page, String search) {
        int size = 10;
        List<TeachersSubjectsAndClassesPayload> list = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        States states = stateService.getStateByLabel(StateEnum.ACTIVE);
        Classes classes = classesService.findById(UUID.fromString(search));

        Page<TeachersSubjectsAndClasses> pageData = teachersSubjectsAndClassesRepo.list(
                PageRequest.of(page, size), classes, states);

        pageData.forEach(t -> list.add(
                new TeachersSubjectsAndClassesPayload(
                        t.getId(),
                        new ClassesPayload(t.getClasses().getId(), t.getClasses().getName()),
                        new SubjectsPayload(t.getSubjects().getId(), t.getSubjects().getName()),
                        new TeachersPayload(t.getTeachers().getId(),
                                t.getTeachers().getFirstName(),
                                t.getTeachers().getLastName(),
                                t.getTeachers().getSureName(),
                                simpleDateFormat.format(t.getTeachers().getDateOfBirth()),
                                t.getTeachers().getPhoneNumber(),
                                t.getTeachers().getEmail()))
        ));

        return new PageablePayload(
                pageData.getTotalPages(),
                pageData.getTotalElements(),
                pageData.getSize(),
                list);
    }

    @Override
    public TeachersSubjectsAndClassesPayload getById(UUID id) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        TeachersSubjectsAndClasses t = teachersSubjectsAndClassesRepo.findById(id).orElse(null);
        if (t == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect Id");
        }

        return new TeachersSubjectsAndClassesPayload(
                t.getId(),
                new ClassesPayload(t.getClasses().getId(), t.getClasses().getName()),
                new SubjectsPayload(t.getSubjects().getId(), t.getSubjects().getName()),
                new TeachersPayload(t.getTeachers().getId(),
                        t.getTeachers().getFirstName(),
                        t.getTeachers().getLastName(),
                        t.getTeachers().getSureName(),
                        simpleDateFormat.format(t.getTeachers().getDateOfBirth()),
                        t.getTeachers().getPhoneNumber(),
                        t.getTeachers().getEmail()));
    }

    public List<ClassesPayload> getTeacherClasses() {

        List<ClassesPayload> list = new ArrayList<>();

        UUID studyYearId = appSettingsService.getKeyByLabel(SetsEnum.STUDY_YEAR);
        States states = stateService.getStateByLabel(StateEnum.ACTIVE);
        Teachers teacher = teachersService.findByUserId(Settings.getCurrentUser().getId());

        if (teacher != null) {

            teachersSubjectsAndClassesRepo.getTeachersClasses(teacher, states, studyYearId)
                    .forEach(t -> list.add(
                            new ClassesPayload(
                                    t.getClasses().getId(),
                                    t.getClasses().getName())));
        }

        return list;
    }

    public List<SubjectsPayload> getTeacherSubjectsByClass(UUID classId) {

        List<SubjectsPayload> list = new ArrayList<>();

        UUID studyYearId = appSettingsService.getKeyByLabel(SetsEnum.STUDY_YEAR);
        States states = stateService.getStateByLabel(StateEnum.ACTIVE);
        Teachers teacher = teachersService.findByUserId(Settings.getCurrentUser().getId());
        Classes classes = classesService.findById(classId);

        if (teacher != null && classes != null) {

            teachersSubjectsAndClassesRepo.getTeacherSubjectsByClass(
                    teacher, states, studyYearId, classes).forEach(t -> list.add(
                    new SubjectsPayload(
                            t.getSubjects().getId(),
                            t.getSubjects().getName())));
        }

        return list;
    }
}
