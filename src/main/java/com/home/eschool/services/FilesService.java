package com.home.eschool.services;

import com.home.eschool.entity.Files;
import com.home.eschool.models.payload.FilesPayload;
import com.home.eschool.repository.FilesRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class FilesService {

    private final FilesRepo filesRepo;

    public FilesService(FilesRepo filesRepo) {
        this.filesRepo = filesRepo;
    }


    public boolean isAvailableTypes(String contentType) {
        return true;
    }

    public FilesPayload upload(MultipartFile file) {
        return null;
    }

    public Files getFileById(UUID fileId) {
        return null;
    }
}
