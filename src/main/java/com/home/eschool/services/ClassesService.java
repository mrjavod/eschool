package com.home.eschool.services;

import com.home.eschool.entity.Classes;
import com.home.eschool.entity.Languages;
import com.home.eschool.entity.States;
import com.home.eschool.entity.enums.StateEnum;
import com.home.eschool.models.dto.ClassesDto;
import com.home.eschool.models.payload.ClassesPayload;
import com.home.eschool.models.payload.PageablePayload;
import com.home.eschool.repository.ClassesRepo;
import com.home.eschool.services.interfaces.CrudInterface;
import com.home.eschool.utils.Settings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClassesService implements CrudInterface<List<ClassesDto>, ClassesPayload> {

    private final ClassesRepo classesRepo;
    private final LanguageService languageService;
    private final StateService stateService;

    public ClassesService(ClassesRepo classesRepo,
                          LanguageService languageService,
                          StateService stateService) {
        this.classesRepo = classesRepo;
        this.languageService = languageService;
        this.stateService = stateService;
    }

    private boolean setActive(Optional<Classes> t) {
        if (t.isPresent() && t.get().getState().getLabel().equals(StateEnum.DELETED)) {
            Classes teacher = t.get();
            teacher.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
            teacher.setChangeUser(Settings.getCurrentUser());
            teacher.setState(stateService.getStateByLabel(StateEnum.ACTIVE));
            classesRepo.save(teacher);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(List<ClassesDto> classes) {
        List<Classes> list = new ArrayList<>();
        Languages language = languageService.getLanguageByLabel(Settings.getLang());
        States states = stateService.getStateByLabel(StateEnum.ACTIVE);

        for (ClassesDto aClass : classes) {

            Optional<Classes> t = classesRepo.findByName(aClass.getName());
            if (t.isPresent() && t.get().getState().getLabel().equals(StateEnum.ACTIVE)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Bunday sinf tizimda mavjud !");
            } else if (setActive(t)) return;

            Classes newClass = new Classes();
            newClass.setId(UUID.randomUUID());
            newClass.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
            newClass.setCreateUser(Settings.getCurrentUser());
            newClass.setLang(language);
            newClass.setName(aClass.getName());
            newClass.setState(states);
            list.add(newClass);
        }

        classesRepo.saveAll(list);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(List<ClassesDto> classes) {
        List<Classes> list = new ArrayList<>();
        for (ClassesDto aClass : classes) {
            Classes newClass = classesRepo.findById(aClass.getId()).orElse(null);
            if (newClass == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Bunday sinf topilmadi !");
            }

            Optional<Classes> t = classesRepo.findByName(aClass.getName());
            if (t.isPresent() && t.get().getState().getLabel().equals(StateEnum.ACTIVE)
                    && !t.get().getId().equals(aClass.getId())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Bunday sinf tizimda mavjud !");
            }

            newClass.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
            newClass.setChangeUser(Settings.getCurrentUser());
            newClass.setName(aClass.getName());
            list.add(newClass);
        }

        classesRepo.saveAll(list);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateOnlyOne(ClassesDto classes) {
        Classes oldSubject = classesRepo.findById(classes.getId()).orElse(null);
        if (oldSubject == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bunday sinf topilmadi !");
        }

        Optional<Classes> t = classesRepo.findByName(classes.getName());
        if (t.isPresent() && t.get().getState().getLabel().equals(StateEnum.ACTIVE)
                && !t.get().getId().equals(classes.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bunday sinf tizimda mavjud !");
        }

        oldSubject.setName(classes.getName());
        oldSubject.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
        oldSubject.setChangeUser(Settings.getCurrentUser());
        classesRepo.save(oldSubject);
    }

    @Override
    public PageablePayload getAll(int page, String search) {
        int size = 10;
        List<ClassesPayload> list = new ArrayList<>();

        States states = stateService.getStateByLabel(StateEnum.ACTIVE);

        Page<Classes> classesPage = classesRepo.list(
                PageRequest.of(page, size, Sort.by("name")), states);

        classesPage.forEach(t -> list.add(
                new ClassesPayload(
                        t.getId(),
                        t.getName())));

        return new PageablePayload(
                classesPage.getTotalPages(),
                classesPage.getTotalElements(),
                classesPage.getSize(),
                list);
    }

    @Override
    public ClassesPayload getById(UUID id) {
        Classes subjects = classesRepo.findById(id).orElse(null);

        if (subjects == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bunday sinf topilmadi !");
        }

        return new ClassesPayload(subjects.getId(), subjects.getName());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(List<UUID> classes) {
        States states = stateService.getStateByLabel(StateEnum.DELETED);
        classes.forEach(s -> {
            Optional<Classes> optional = classesRepo.findById(s);
            if (optional.isPresent()) {
                Classes t = optional.get();
                t.setChangeUser(Settings.getCurrentUser());
                t.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
                t.setState(states);
                classesRepo.save(t);
            } else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Bunday sinf topilmadi !");
            }
        });
    }

    Classes findById(UUID id) {
        return classesRepo.findById(id).orElse(null);
    }

    List<Classes> getAllClasses() {
        return classesRepo.findAllByStateLabel(StateEnum.ACTIVE);
    }

}
