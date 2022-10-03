package com.home.eschool.services;

import com.home.eschool.entity.Languages;
import com.home.eschool.entity.Subjects;
import com.home.eschool.models.payload.SubjectsPayload;
import com.home.eschool.models.dto.SubjectsDto;
import com.home.eschool.repository.SubjectsRepo;
import com.home.eschool.utils.Settings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubjectsService {

    private final SubjectsRepo subjectsRepo;
    private final LanguageService languageService;

    public SubjectsService(SubjectsRepo subjectsRepo,
                           LanguageService languageService) {
        this.subjectsRepo = subjectsRepo;
        this.languageService = languageService;
    }

    public void create(List<SubjectsDto> subjects) {
        List<Subjects> list = new ArrayList<>();
        Languages language = languageService.getLanguageByLabel(Settings.getLang());

        for (SubjectsDto subjectsDto : subjects) {
            Subjects newClass = new Subjects();

            newClass.setId(UUID.randomUUID());
            newClass.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
            newClass.setCreateUser(Settings.getCurrentUser());
            newClass.setLang(language);
            newClass.setName(subjectsDto.getName());
            list.add(newClass);
        }

        subjectsRepo.saveAll(list);
    }

    public void update(List<SubjectsDto> subjects) {
        List<Subjects> list = new ArrayList<>();

        for (SubjectsDto subjectsDto : subjects) {
            Subjects newClass = subjectsRepo.findById(subjectsDto.getId()).orElse(null);
            if (newClass == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Incorrect Subject Id");
            }
            newClass.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
            newClass.setChangeUser(Settings.getCurrentUser());
            newClass.setName(subjectsDto.getName());
            list.add(newClass);
        }

        subjectsRepo.saveAll(list);
    }

    public void updateOnlyOne(SubjectsDto subject) {
        Subjects oldSubject = subjectsRepo.findById(subject.getId()).orElse(null);
        if (oldSubject == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect Subject Id");
        }
        oldSubject.setName(subject.getName());
        oldSubject.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
        oldSubject.setChangeUser(Settings.getCurrentUser());

        subjectsRepo.save(oldSubject);
    }

    public List<SubjectsPayload> getAll() {
        List<SubjectsPayload> list = new ArrayList<>();
        subjectsRepo.findAll().forEach(s -> list.add(new SubjectsPayload(s.getId(), s.getName())));
        return list;
    }

    public SubjectsPayload getById(UUID id) {
        Subjects subjects = subjectsRepo.findById(id).orElse(null);

        if (subjects == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect Subject Id");
        }

        return new SubjectsPayload(subjects.getId(), subjects.getName());
    }

    public void delete(List<UUID> subjects) {
        subjects.forEach(s -> {
            Optional<Subjects> optional = subjectsRepo.findById(s);
            if (optional.isPresent()) {
                subjectsRepo.deleteById(s);
            } else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Incorrect Subject Id");
            }
        });
    }
}
