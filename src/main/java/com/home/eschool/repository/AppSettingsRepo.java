package com.home.eschool.repository;

import com.home.eschool.entity.AppSettings;
import com.home.eschool.entity.enums.SetsEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppSettingsRepo extends JpaRepository<AppSettings, UUID> {

    Optional<AppSettings> findByLabel(SetsEnum label);

    Optional<AppSettings> findByKey(UUID key);
}
