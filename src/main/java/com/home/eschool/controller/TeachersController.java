package com.home.eschool.controller;

import com.home.eschool.models.dto.TeachersDto;
import com.home.eschool.models.payload.TeachersPayload;
import com.home.eschool.models.payload.TeachersPayloadDetails;
import com.home.eschool.services.TeachersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/teachers")
@Tag(name = "Teachers", description = "Ustozlar bo'yicha CRUD")
public class TeachersController {

    private final TeachersService teachersService;

    public TeachersController(TeachersService teachersService) {
        this.teachersService = teachersService;
    }

    @GetMapping("/")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public List<TeachersPayload> getAll() {
        return teachersService.getAll();
    }

    @GetMapping("/getById/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public TeachersPayloadDetails getById(@PathVariable("id") UUID id) {
        return teachersService.getById(id);
    }

    @PostMapping("/create")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public void create(@RequestBody TeachersDto teacher) {
        teachersService.create(teacher);
    }

    @PostMapping("/update")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public void update(@RequestBody TeachersDto teacher) {
        teachersService.update(teacher);
    }

}
