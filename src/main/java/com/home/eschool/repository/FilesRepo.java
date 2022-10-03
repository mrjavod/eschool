package com.home.eschool.repository;

import com.home.eschool.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FilesRepo extends JpaRepository<Files, UUID> {
}
