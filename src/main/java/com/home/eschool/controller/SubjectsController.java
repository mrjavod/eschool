package com.home.eschool.controller;

import com.home.eschool.models.payload.SubjectsPayload;
import com.home.eschool.models.dto.SubjectsDto;
import com.home.eschool.services.SubjectsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subjects")
@Tag(name = "Subjects", description = "Fanlar bo'yicha CRUD")
public class SubjectsController {

    private final SubjectsService subjectsService;

    public SubjectsController(SubjectsService subjectsService) {
        this.subjectsService = subjectsService;
    }

    @GetMapping("/")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public List<SubjectsPayload> getAll() {
        return subjectsService.getAll();
    }

    @GetMapping("/getById/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public SubjectsPayload getById(@PathVariable("id") UUID id) {
        return subjectsService.getById(id);
    }

    @PostMapping("/create")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public void create(@RequestBody List<SubjectsDto> subjects) {
        subjectsService.create(subjects);
    }

    @PostMapping("/update")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public void update(@RequestBody List<SubjectsDto> subjects) {
        subjectsService.update(subjects);
    }

    @PostMapping("/updateOnlyOne")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public void updateOnlyOne(@RequestBody SubjectsDto subject) {
        subjectsService.updateOnlyOne(subject);
    }

    @PostMapping("/delete")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public void delete(@RequestBody List<UUID> subjects) {
        subjectsService.delete(subjects);
    }
}
