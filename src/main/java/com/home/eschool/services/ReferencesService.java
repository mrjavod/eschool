package com.home.eschool.services;

import com.home.eschool.models.payload.ReferencePayload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReferencesService {

    private final TeachersService teachersService;
    private final ClassesService classesService;
    private final SubjectsService subjectsService;

    public ReferencesService(TeachersService teachersService,
                             ClassesService classesService,
                             SubjectsService subjectsService) {
        this.teachersService = teachersService;
        this.classesService = classesService;
        this.subjectsService = subjectsService;
    }

    public List<ReferencePayload> getReferences(String object) {

        List<ReferencePayload> list = new ArrayList<>();

        switch (object) {
            case "classes":
                classesService.getAllClasses().forEach(classes ->
                        list.add(new ReferencePayload(classes.getId(), classes.getName())));
                break;

            case "teachers":
                teachersService.getAllTeachers().forEach(teachers ->
                        list.add(new ReferencePayload(teachers.getId(),
                                String.format("%s %s %s", teachers.getLastName(), teachers.getFirstName(), teachers.getSureName()))));
                break;

            case "subjects":
                subjectsService.getAllSubjects().forEach(subjects ->
                        list.add(new ReferencePayload(subjects.getId(), subjects.getName())));
                break;
        }

        return list;
    }
}
