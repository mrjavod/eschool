package com.home.eschool.controller;

import com.home.eschool.models.dto.AttendanceDto;
import com.home.eschool.models.dto.AttendanceListDto;
import com.home.eschool.models.payload.AttendanceListPayload;
import com.home.eschool.services.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<AttendanceListPayload> list(@Valid @RequestBody AttendanceListDto dto) {
        return attendanceService.list(dto);
    }

    @PostMapping("/save")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public void save(@Valid @RequestBody AttendanceDto dto) {
        attendanceService.save(dto);
    }

    @PostMapping("/delete")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public void delete(@RequestBody List<UUID> uuids) {
        attendanceService.delete(uuids);
    }

}
