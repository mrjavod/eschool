package com.home.eschool.controller;

import com.home.eschool.models.dto.AttendanceDto;
import com.home.eschool.models.dto.AttendanceListDto;
import com.home.eschool.services.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attendance")
@Tag(name = "Attendance", description = "Davomatlar bo'yicha CRUD")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Object list(@Valid @RequestBody AttendanceListDto dto) {
        return attendanceService.list(dto);
    }

    @PostMapping("/create")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public void create(@Valid @RequestBody AttendanceDto dto) {
        attendanceService.create(dto);
    }

    @GetMapping("/getById/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Object getById(@PathVariable("id") UUID id) {
        return attendanceService.getById(id);
    }

    @PostMapping("/delete")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public void delete(@RequestBody List<UUID> uuids) {
        attendanceService.delete(uuids);
    }

}
