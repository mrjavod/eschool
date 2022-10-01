package com.home.eschool.controller;

import com.home.eschool.entity.Classes;
import com.home.eschool.models.ClassesDto;
import com.home.eschool.models.ClassesPayload;
import com.home.eschool.services.ClassesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/classes")
@Tag(name = "classes")
public class ClassesController {

    private final ClassesService classesService;

    public ClassesController(ClassesService classesService) {
        this.classesService = classesService;
    }

    @PostMapping("/create")
    public void create(@RequestBody List<ClassesDto> classes) {

        classesService.create(classes);
    }

    @PostMapping("/update")
    public void update(@RequestBody List<ClassesDto> classes) {

        classesService.update(classes);
    }

    @GetMapping("/")
    public List<ClassesPayload> getAll() {

        return classesService.getAll();
    }
}
