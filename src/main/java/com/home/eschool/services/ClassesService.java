package com.home.eschool.services;

import com.home.eschool.entity.Classes;
import com.home.eschool.entity.Languages;
import com.home.eschool.models.dto.ClassesDto;
import com.home.eschool.models.payload.ClassesPayload;
import com.home.eschool.repository.ClassesRepo;
import com.home.eschool.utils.Settings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClassesService {

    private final ClassesRepo classesRepo;
    private final LanguageService languageService;

    public ClassesService(ClassesRepo classesRepo,
                          LanguageService languageService) {
        this.classesRepo = classesRepo;
        this.languageService = languageService;
    }

    public void create(List<ClassesDto> classes) {
        List<Classes> list = new ArrayList<>();
        Languages language = languageService.getLanguageByLabel(Settings.getLang());

        for (ClassesDto aClass : classes) {
            Classes newClass = new Classes();

            newClass.setId(UUID.randomUUID());
            newClass.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
            newClass.setCreateUser(Settings.getCurrentUser());
            newClass.setLang(language);
            newClass.setName(aClass.getName());
            list.add(newClass);
        }

        classesRepo.saveAll(list);
    }

    public void update(List<ClassesDto> classes) {

        List<Classes> list = new ArrayList<>();

        for (ClassesDto aClass : classes) {
            Classes newClass = classesRepo.findById(aClass.getId()).orElse(null);
            if (newClass == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Incorrect Subject Id");
            }
            newClass.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
            newClass.setChangeUser(Settings.getCurrentUser());
            newClass.setName(aClass.getName());
            list.add(newClass);
        }

        classesRepo.saveAll(list);
    }

    public void updateOnlyOne(ClassesDto classes) {
        Classes oldSubject = classesRepo.findById(classes.getId()).orElse(null);
        if (oldSubject == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect Subject Id");
        }
        oldSubject.setName(classes.getName());
        oldSubject.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
        oldSubject.setChangeUser(Settings.getCurrentUser());

        classesRepo.save(oldSubject);
    }

    public List<ClassesPayload> getAll() {
        List<ClassesPayload> list = new ArrayList<>();
        classesRepo.findAll().forEach(c -> list.add(new ClassesPayload(c.getId(), c.getName())));
        return list;
    }

    public ClassesPayload getById(UUID id) {
        Classes subjects = classesRepo.findById(id).orElse(null);

        if (subjects == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect Subject Id");
        }

        return new ClassesPayload(subjects.getId(), subjects.getName());
    }

    public void delete(List<UUID> classes) {
        classes.forEach(s -> {
            Optional<Classes> optional = classesRepo.findById(s);
            if (optional.isPresent()) {
                classesRepo.deleteById(s);
            } else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Incorrect Subject Id");
            }
        });
    }

}
