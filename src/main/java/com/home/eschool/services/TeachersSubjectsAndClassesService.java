package com.home.eschool.services;

import com.home.eschool.entity.Classes;
import com.home.eschool.entity.States;
import com.home.eschool.entity.TeachersSubjectsAndClasses;
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
        TeachersSubjectsAndClasses data = new TeachersSubjectsAndClasses();
        data.setId(UUID.randomUUID());
        data.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        data.setCreateUser(Settings.getCurrentUser());
        data.setStates(stateService.getStateByLabel(StateEnum.ACTIVE));
        data.setStudyYearId(appSettingsService.getKeyByLabel(SetsEnum.STUDY_YEAR));
        data.setClasses(classesService.findById(t.getClassId()));
        data.setTeachers(teachersService.findById(t.getTeacherId()));
        data.setSubjects(subjectsService.findById(t.getSubjectId()));

        teachersSubjectsAndClassesRepo.save(data);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(TeachersSubjectsAndClassesDto t) {
        TeachersSubjectsAndClasses data = teachersSubjectsAndClassesRepo.findById(t.getId()).orElse(null);
        if (data == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect Teacher Id");
        }

        data.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
        data.setChangeUser(Settings.getCurrentUser());
        data.setClasses(classesService.findById(t.getClassId()));
        data.setTeachers(teachersService.findById(t.getTeacherId()));
        data.setSubjects(subjectsService.findById(t.getSubjectId()));

        teachersSubjectsAndClassesRepo.save(data);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(List<UUID> ids) {
        ids.forEach(s -> {
            Optional<TeachersSubjectsAndClasses> optional = teachersSubjectsAndClassesRepo.findById(s);
            if (optional.isPresent()) {
                TeachersSubjectsAndClasses data = optional.get();
                data.setStates(stateService.getStateByLabel(StateEnum.DELETED));
                data.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
                data.setChangeUser(Settings.getCurrentUser());

                teachersSubjectsAndClassesRepo.save(data);
            } else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Incorrect Subject Id");
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
                    HttpStatus.BAD_REQUEST, "Incorrect Teacher Id");
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
}
