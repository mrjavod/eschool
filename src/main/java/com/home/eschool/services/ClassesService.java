package com.home.eschool.services;

import com.home.eschool.entity.Classes;
import com.home.eschool.entity.Languages;
import com.home.eschool.models.ClassesDto;
import com.home.eschool.models.ClassesPayload;
import com.home.eschool.repository.ClassesRepo;
import com.home.eschool.repository.LanguagesRepo;
import com.home.eschool.utils.Settings;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ClassesService {

    private final ClassesRepo classesRepo;
    private final LanguagesRepo languagesRepo;

    public ClassesService(ClassesRepo classesRepo,
                          LanguagesRepo languagesRepo) {
        this.classesRepo = classesRepo;
        this.languagesRepo = languagesRepo;
    }

    public void create(List<ClassesDto> classes) {

        List<Classes> list = new ArrayList<>();
        Languages language = languagesRepo.getLanguagesByLabel(Settings.getLang());

        for (ClassesDto aClass : classes) {
            Classes newClass = new Classes();

            newClass.setId(UUID.randomUUID());
            newClass.setCrateDate(Timestamp.valueOf(LocalDateTime.now()));
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
            Classes newClass = classesRepo.getById(aClass.getId());

            newClass.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
            newClass.setChangeUser(Settings.getCurrentUser());
            newClass.setName(aClass.getName());
            list.add(newClass);
        }

        classesRepo.saveAll(list);
    }

    public List<ClassesPayload> getAll() {

        List<ClassesPayload> list = new ArrayList<>();

        classesRepo.findAll().forEach(c -> list.add(new ClassesPayload(c.getId(), c.getName())));

        return list;
    }
}
