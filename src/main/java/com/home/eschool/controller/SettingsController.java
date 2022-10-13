package com.home.eschool.controller;


import com.home.eschool.models.dto.ExportDto;
import com.home.eschool.models.payload.ExportPayload;
import com.home.eschool.services.AppSettingsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/settings")
@Tag(name = "Settings", description = "Tizimdagi asosiy sozlamalar")
public class SettingsController {

    private final AppSettingsService settingsService;

    public SettingsController(AppSettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @PostMapping("/export")
    public ResponseEntity export(@RequestBody ExportDto dto) {

        ExportPayload payload = settingsService.export(dto);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment; filename=" + payload.getFileName());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok().headers(headers).body(payload.getContent());
    }
}
