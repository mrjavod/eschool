package com.home.eschool.controller;

import com.home.eschool.models.payload.ReferencePayload;
import com.home.eschool.services.ReferencesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/references")
@Tag(name = "References", description = "Ma'lumotnomalar")
public class ReferencesController {

    private final ReferencesService service;

    public ReferencesController(ReferencesService service) {
        this.service = service;
    }

    @GetMapping("/{object}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public List<ReferencePayload> getReferences(@PathVariable(value = "object") String object) {
        return service.getReferences(object);
    }

}
