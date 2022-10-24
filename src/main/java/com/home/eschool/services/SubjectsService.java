package com.home.eschool.services;

import com.home.eschool.entity.Languages;
import com.home.eschool.entity.States;
import com.home.eschool.entity.Subjects;
import com.home.eschool.entity.enums.StateEnum;
import com.home.eschool.models.dto.SubjectsDto;
import com.home.eschool.models.payload.PageablePayload;
import com.home.eschool.models.payload.SubjectsPayload;
import com.home.eschool.repository.SubjectsRepo;
import com.home.eschool.services.interfaces.CrudInterface;
import com.home.eschool.utils.Settings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
public class SubjectsService implements CrudInterface<List<SubjectsDto>, SubjectsPayload> {

    private final SubjectsRepo subjectsRepo;
    private final LanguageService languageService;
    private final StateService stateService;

    public SubjectsService(SubjectsRepo subjectsRepo,
                           LanguageService languageService,
                           StateService stateService) {
        this.subjectsRepo = subjectsRepo;
        this.languageService = languageService;
        this.stateService = stateService;
    }

    @Override
    public void create(List<SubjectsDto> subjects) {
        List<Subjects> list = new ArrayList<>();
        Languages language = languageService.getLanguageByLabel(Settings.getLang());
        States states = stateService.getStateByLabel(StateEnum.ACTIVE);

        for (SubjectsDto subjectsDto : subjects) {
            Subjects newClass = new Subjects();

            newClass.setId(UUID.randomUUID());
            newClass.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
            newClass.setCreateUser(Settings.getCurrentUser());
            newClass.setLang(language);
            newClass.setName(subjectsDto.getName());
            newClass.setState(states);
            list.add(newClass);
        }

        subjectsRepo.saveAll(list);
    }

    @Override
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

    @Override
    public PageablePayload getAll(int page, String search) {
        int size = 10;
        List<SubjectsPayload> list = new ArrayList<>();

        Page<Subjects> subjectsPage = subjectsRepo.findAllByNameContains(
                PageRequest.of(page, size, Sort.by("name")), search);

        subjectsPage.forEach(t -> list.add(
                new SubjectsPayload(
                        t.getId(),
                        t.getName())));

        return new PageablePayload(
                subjectsPage.getTotalPages(),
                subjectsPage.getTotalElements(),
                subjectsPage.getSize(),
                list);
    }

    @Override
    public SubjectsPayload getById(UUID id) {
        Subjects subjects = subjectsRepo.findById(id).orElse(null);

        if (subjects == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect Subject Id");
        }

        return new SubjectsPayload(subjects.getId(), subjects.getName());
    }

    @Override
    public void delete(List<UUID> subjects) {
        States states = stateService.getStateByLabel(StateEnum.DELETED);
        subjects.forEach(s -> {
            Optional<Subjects> optional = subjectsRepo.findById(s);
            if (optional.isPresent()) {
                Subjects t = optional.get();
                t.setChangeUser(Settings.getCurrentUser());
                t.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
                t.setState(states);
                subjectsRepo.save(t);
            } else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Incorrect Subject Id");
            }
        });
    }

    public Subjects findById(UUID subjectId) {
        if (subjectId == null) {
            return null;
        }

        return subjectsRepo.findById(subjectId).orElse(null);
    }

    public List<Subjects> getAllSubjects() {
        return subjectsRepo.findAllByStateLabel(StateEnum.ACTIVE);
    }
}
