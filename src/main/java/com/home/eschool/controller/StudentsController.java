package com.home.eschool.controller;

import com.home.eschool.models.dto.StudentsDto;
import com.home.eschool.models.payload.ClassStudentsPayload;
import com.home.eschool.models.payload.PageablePayload;
import com.home.eschool.models.payload.StudentsPayloadDetails;
import com.home.eschool.services.StudentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/students")
@Tag(name = "Students", description = "O'quvchilar bo'yicha CRUD")
public class StudentsController {

    private final StudentsService studentsService;

    public StudentsController(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

    @GetMapping("/")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public PageablePayload getAll(@RequestParam(required = false, name = "page", defaultValue = "0") int page,
                                  @RequestParam(required = false, name = "search", defaultValue = "") String search) {
        return studentsService.getAll(page, search);
    }

    @GetMapping("/getById/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public StudentsPayloadDetails getById(@PathVariable("id") UUID id) {
        return studentsService.getById(id);
    }

    @PostMapping("/create")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public void create(@Valid @RequestBody StudentsDto studentsDto) {
        studentsService.create(studentsDto);
    }

    @PostMapping("/update")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public void update(@Valid @RequestBody StudentsDto studentsDto) {
        studentsService.update(studentsDto);
    }

    @PostMapping("/delete")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public void delete(@RequestBody List<UUID> students) {
        studentsService.delete(students);
    }

    @GetMapping("/getStudentsByClass/{classId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public List<ClassStudentsPayload> getStudentsByClass(@PathVariable("classId") UUID classId) {
        return studentsService.getStudentsByClass(classId);
    }

    @PostMapping("/count")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public int count() {
        return studentsService.getAllStudents().size();
    }

}
