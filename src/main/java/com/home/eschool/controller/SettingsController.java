package com.home.eschool.controller;


import com.home.eschool.models.dto.ExportDto;
import com.home.eschool.models.payload.ExportPayload;
import com.home.eschool.services.AppSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/settings")
@Tag(name = "Settings", description = "Tizimdagi asosiy sozlamalar")
public class SettingsController {

    private final AppSettingsService settingsService;

    public SettingsController(AppSettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @PostMapping("/export")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public ResponseEntity export(@RequestBody ExportDto dto) {

        ExportPayload payload = settingsService.export(dto);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment; filename=" + payload.getFileName());
        headers.set("Content-Length", payload.getContent().toByteArray().length + "");
        headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

        return ResponseEntity.ok().headers(headers).body(payload.getContent().toByteArray());
    }

    @PostMapping(value = "/import", consumes = "multipart/form-data")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public ResponseEntity importFile(@RequestParam("file") MultipartFile file,
                                     @RequestParam("file") String object) {
        return settingsService.importFile(file, object);
    }
}
