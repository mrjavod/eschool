package com.home.eschool.controller;

import com.home.eschool.entity.Files;
import com.home.eschool.models.payload.FilesPayload;
import com.home.eschool.services.FilesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/files")
@Tag(name = "Files", description = "Fayllar bo'yicha CRUD")
public class FilesController {

    private final FilesService filesService;

    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public FilesPayload upload(@RequestParam("file") MultipartFile file) throws IOException {

        if (!filesService.isAvailableTypes(file.getContentType())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect file type");
        }

        return filesService.upload(file);
    }

    @GetMapping("/download/{file_id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity download(@PathVariable("file_id") UUID fileId) {

        Files file = filesService.getFileById(fileId);

        if (file == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "File not found");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment; filename=" + file.getName());
        headers.setContentType(MediaType.valueOf(file.getMimeType()));

        return ResponseEntity.ok().headers(headers).body(file.getContent());
    }
}
