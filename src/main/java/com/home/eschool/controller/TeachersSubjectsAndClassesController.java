package com.home.eschool.controller;

import com.home.eschool.models.dto.TeachersSubjectsAndClassesDto;
import com.home.eschool.models.payload.ClassesPayload;
import com.home.eschool.models.payload.PageablePayload;
import com.home.eschool.models.payload.SubjectsPayload;
import com.home.eschool.models.payload.TeachersSubjectsAndClassesPayload;
import com.home.eschool.services.TeachersSubjectsAndClassesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/teachersSubjects")
@Tag(name = "Teachers Subjects And Classes", description = "Ustozlarning dars beradigan fanlari va sinflari")
public class TeachersSubjectsAndClassesController {

    private final TeachersSubjectsAndClassesService teachersSubjectsAndClassesService;

    public TeachersSubjectsAndClassesController(TeachersSubjectsAndClassesService teachersSubjectsAndClassesService) {
        this.teachersSubjectsAndClassesService = teachersSubjectsAndClassesService;
    }

    @GetMapping("/")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public PageablePayload getAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "search", defaultValue = "") String search) {
        return teachersSubjectsAndClassesService.getAll(page, search);
    }

    @GetMapping("/getById/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public TeachersSubjectsAndClassesPayload getById(@PathVariable("id") UUID id) {
        return teachersSubjectsAndClassesService.getById(id);
    }

    @PostMapping("/create")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public void create(@Valid @RequestBody TeachersSubjectsAndClassesDto dto) {
        teachersSubjectsAndClassesService.create(dto);
    }

    @PostMapping("/update")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public void update(@Valid @RequestBody TeachersSubjectsAndClassesDto dto) {
        teachersSubjectsAndClassesService.update(dto);
    }

    @PostMapping("/delete")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public void delete(@RequestBody List<UUID> uuids) {
        teachersSubjectsAndClassesService.delete(uuids);
    }

    @PostMapping("/getClasses")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public List<ClassesPayload> getClasses() {
        return teachersSubjectsAndClassesService.getTeacherClasses();
    }

    @PostMapping("/getSubjectsByClass/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public List<SubjectsPayload> getSubjectsByClass(@PathVariable("id") UUID classId) {
        return teachersSubjectsAndClassesService.getTeacherSubjectsByClass(classId);
    }

}
