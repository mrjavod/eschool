package com.home.eschool.controller;

import com.home.eschool.models.dto.ClassesDto;
import com.home.eschool.models.payload.ClassesPayload;
import com.home.eschool.models.payload.PageablePayload;
import com.home.eschool.services.ClassesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/classes")
@Tag(name = "Classes", description = "Sinflar bo'yicha CRUD")
public class ClassesController {

    private final ClassesService classesService;

    public ClassesController(ClassesService classesService) {
        this.classesService = classesService;
    }

    @GetMapping("/")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public PageablePayload getAll(@RequestParam(required = false, name = "page", defaultValue = "0") int page,
                                  @RequestParam(required = false, name = "search", defaultValue = "") String search) {
        return classesService.getAll(page, search);
    }

    @GetMapping("/getById/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ClassesPayload getById(@PathVariable("id") UUID id) {
        return classesService.getById(id);
    }

    @PostMapping("/create")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public void create(@Valid @RequestBody List<ClassesDto> classes) {
        classesService.create(classes);
    }

    @PostMapping("/update")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public void update(@Valid @RequestBody List<ClassesDto> classes) {
        classesService.update(classes);
    }

    @PostMapping("/updateOnlyOne")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public void updateOnlyOne(@Valid @RequestBody ClassesDto classes) {
        classesService.updateOnlyOne(classes);
    }

    @PostMapping("/delete")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public void delete(@RequestBody List<UUID> classes) {
        classesService.delete(classes);
    }

}
