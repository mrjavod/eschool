package com.home.eschool.controller;

import com.home.eschool.models.dto.GradeDto;
import com.home.eschool.models.dto.GradeListDto;
import com.home.eschool.models.payload.GradeListPayload;
import com.home.eschool.services.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/grade")
@Tag(name = "Grade", description = "Baholash")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping("/")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public List<GradeListPayload> list(@Valid @RequestBody GradeListDto dto) {
        return gradeService.list(dto);
    }

    @PostMapping("/save")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public void save(@Valid @RequestBody GradeDto dto) {
        gradeService.save(dto);
    }
}
