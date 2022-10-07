package com.home.eschool.services;

import com.home.eschool.entity.AppSettings;
import com.home.eschool.entity.enums.SetsEnum;
import com.home.eschool.models.payload.StudyYearsPayload;
import com.home.eschool.repository.AppSettingsRepo;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AppSettingsService {

    private final AppSettingsRepo appSettingsRepo;

    public AppSettingsService(AppSettingsRepo appSettingsRepo) {
        this.appSettingsRepo = appSettingsRepo;
    }

    private List<StudyYearsPayload> getStudyYears() {
        List<StudyYearsPayload> list = new ArrayList<>();
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2022-2023"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2023-2024"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2024-2025"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2025-2026"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2026-2027"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2027-2028"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2028-2029"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2029-2030"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2030-2031"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2031-2032"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2032-2033"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2033-2034"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2034-2035"));
        return list;
    }

    public StudyYearsPayload getStudyYearsByName(String name) {
        return getStudyYears().stream().filter(e -> e.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public UUID getKeyByLabel(SetsEnum label) {
        return appSettingsRepo.findByLabel(label)
                .map(AppSettings::getKey).orElse(null);
    }

    public void createDefaultSettings() {

        AppSettings appSettings = new AppSettings();
        appSettings.setId(UUID.randomUUID());
        appSettings.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        appSettings.setLabel(SetsEnum.STUDY_YEAR);
        appSettings.setName("O'quv yili");

        StudyYearsPayload studyYearsPayload = getStudyYearsByName("2022-2023");
        if (studyYearsPayload != null) {
            appSettings.setKey(studyYearsPayload.getId());
            appSettings.setValue(studyYearsPayload.getName());
        }

        appSettingsRepo.save(appSettings);
    }

}
