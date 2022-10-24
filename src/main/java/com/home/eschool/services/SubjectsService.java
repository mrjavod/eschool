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
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(rollbackFor = Throwable.class)
    public void create(List<SubjectsDto> subjects) {
        List<Subjects> list = new ArrayList<>();
        Languages language = languageService.getLanguageByLabel(Settings.getLang());
        States states = stateService.getStateByLabel(StateEnum.ACTIVE);

        for (SubjectsDto subjectsDto : subjects) {
            if (subjectsRepo.findByName(subjectsDto.getName()).isPresent()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Bunday fan tizimda mavjud !");
            }

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
    @Transactional(rollbackFor = Throwable.class)
    public void update(List<SubjectsDto> subjects) {
        List<Subjects> list = new ArrayList<>();

        for (SubjectsDto subjectsDto : subjects) {
            Subjects newClass = subjectsRepo.findById(subjectsDto.getId()).orElse(null);
            if (newClass == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Bunday fan topilmadi !");
            }

            Optional<Subjects> optional = subjectsRepo.findByName(subjectsDto.getName());
            if (optional.isPresent() && optional.get().getId() != subjectsDto.getId()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Bunday fan tizimda mavjud !");
            }

            newClass.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
            newClass.setChangeUser(Settings.getCurrentUser());
            newClass.setName(subjectsDto.getName());
            list.add(newClass);
        }

        subjectsRepo.saveAll(list);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateOnlyOne(SubjectsDto subject) {
        Subjects oldSubject = subjectsRepo.findById(subject.getId()).orElse(null);
        if (oldSubject == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bunday fan topilmadi !");
        }

        Optional<Subjects> optional = subjectsRepo.findByName(subject.getName());
        if (optional.isPresent() && optional.get().getId() != subject.getId()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bunday fan tizimda mavjud !");
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
        States states = stateService.getStateByLabel(StateEnum.ACTIVE);

        Page<Subjects> subjectsPage = subjectsRepo.list(
                PageRequest.of(page, size, Sort.by("name")), states, search);

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
                    HttpStatus.BAD_REQUEST, "Bunday fan topilmadi !");
        }

        return new SubjectsPayload(subjects.getId(), subjects.getName());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
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
                        HttpStatus.BAD_REQUEST, "Bunday fan topilmadi !");
            }
        });
    }

    Subjects findById(UUID subjectId) {
        if (subjectId == null) {
            return null;
        }

        return subjectsRepo.findById(subjectId).orElse(null);
    }

    List<Subjects> getAllSubjects() {
        return subjectsRepo.findAllByStateLabel(StateEnum.ACTIVE);
    }
}
