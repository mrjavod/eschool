package com.home.eschool.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.home.eschool.entity.Students;
import com.home.eschool.entity.Teachers;
import com.home.eschool.entity.addinfo.BirthInfo;
import com.home.eschool.entity.addinfo.Parents;
import com.home.eschool.models.dto.StudentsDto;
import com.home.eschool.models.dto.TeachersDto;
import com.home.eschool.models.payload.*;
import com.home.eschool.repository.StudentsRepo;
import com.home.eschool.repository.TeachersRepo;
import com.home.eschool.services.interfaces.CrudInterface;
import com.home.eschool.utils.Settings;
import com.home.eschool.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class StudentsService implements CrudInterface<StudentsDto, StudentsPayload> {

    private final StudentsRepo studentsRepo;
    private final FilesService filesService;
    private final StudentClassesService studentClassesService;
    private final StateService stateService;

    public StudentsService(StudentsRepo studentsRepo,
                           FilesService filesService,
                           StudentClassesService studentClassesService,
                           StateService stateService) {
        this.studentsRepo = studentsRepo;
        this.filesService = filesService;
        this.studentClassesService = studentClassesService;
        this.stateService = stateService;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(StudentsDto studentsDto) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Students students = new Students();
        students.setId(UUID.randomUUID());
        students.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        students.setCreateUser(Settings.getCurrentUser());
        students.setFirstName(studentsDto.getFirstName());
        students.setLastName(studentsDto.getLastName());
        students.setSureName(studentsDto.getSureName());
        students.setAvatar_id(studentsDto.getAvatar_id());
        students.setAddress(studentsDto.getAddress());
        students.setPhoneNumber(studentsDto.getPhoneNumber());
        students.setMonthlyPayment(studentsDto.getMonthlyPayment());

        try {
            students.setDateOfBirth(new Date(simpleDateFormat.parse(studentsDto.getDateOfBirth()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (studentsDto.getMother() != null) {
            students.setMother(Utils.convertToString(studentsDto.getMother()));
        }
        if (studentsDto.getFather() != null) {
            students.setFather(Utils.convertToString(studentsDto.getFather()));
        }
        if (studentsDto.getBirthInfo() != null) {
            students.setBirthInfo(Utils.convertToString(studentsDto.getBirthInfo()));
        }
        if (studentsDto.getAdditionalInfo() != null) {
            students.setAdditionalInfo(Utils.convertToString(studentsDto.getAdditionalInfo()));
        }

        studentsRepo.save(students);
        studentClassesService.create(students, studentsDto.getClassId());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(StudentsDto studentsDto) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Students students = studentsRepo.findById(studentsDto.getId()).orElse(null);
        if (students == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect Teacher Id");
        }

        students.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
        students.setChangeUser(Settings.getCurrentUser());
        students.setFirstName(studentsDto.getFirstName());
        students.setLastName(studentsDto.getLastName());
        students.setSureName(studentsDto.getSureName());
        students.setAvatar_id(studentsDto.getAvatar_id());
        students.setAddress(studentsDto.getAddress());
        students.setPhoneNumber(studentsDto.getPhoneNumber());
        students.setMonthlyPayment(studentsDto.getMonthlyPayment());

        try {
            students.setDateOfBirth(new Date(simpleDateFormat.parse(studentsDto.getDateOfBirth()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (studentsDto.getMother() != null) {
            students.setMother(Utils.convertToString(studentsDto.getMother()));
        }
        if (studentsDto.getFather() != null) {
            students.setFather(Utils.convertToString(studentsDto.getFather()));
        }
        if (studentsDto.getBirthInfo() != null) {
            students.setBirthInfo(Utils.convertToString(studentsDto.getBirthInfo()));
        }
        if (studentsDto.getAdditionalInfo() != null) {
            students.setAdditionalInfo(Utils.convertToString(studentsDto.getAdditionalInfo()));
        }

        studentsRepo.save(students);
        studentClassesService.update(students, studentsDto.getClassId());
    }

    @Override
    public PageablePayload getAll(int page, String search) {

        int size = 10;
        List<StudentsPayload> list = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Page<Students> studentsPage = studentsRepo.findAllByFirstNameContainsOrLastNameContainsOrSureNameContains(
                PageRequest.of(page, size, Sort.by("lastName")), search, search, search);

        studentsPage.forEach(t -> list.add(
                new StudentsPayload(
                        t.getId(),
                        t.getFirstName(),
                        t.getLastName(),
                        t.getSureName(),
                        simpleDateFormat.format(t.getDateOfBirth()),
                        t.getPhoneNumber(),
                        studentClassesService.getClassesInfo(t)
                )));

        return new PageablePayload(
                studentsPage.getTotalPages(),
                studentsPage.getTotalElements(),
                studentsPage.getSize(),
                list);
    }

    @Override
    public StudentsPayloadDetails getById(UUID id) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Students students = studentsRepo.findById(id).orElse(null);
        if (students == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect Teacher Id");
        }

        return new StudentsPayloadDetails(
                students.getId(),
                students.getFirstName(),
                students.getLastName(),
                students.getSureName(),
                simpleDateFormat.format(students.getDateOfBirth()),
                students.getPhoneNumber(),
                studentClassesService.getClassesInfo(students),
                students.getAddress(),
                filesService.getFileInfo(students.getAvatar_id()),
                Utils.convertToObject(students.getMother(), Parents.class),
                Utils.convertToObject(students.getFather(), Parents.class),
                Utils.convertToObject(students.getBirthInfo(), BirthInfo.class),
                students.getMonthlyPayment(),
                Utils.convertToObject(students.getAdditionalInfo(), JsonNode.class)
        );
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(List<UUID> teachers) {
        teachers.forEach(s -> {
            Optional<Students> optional = studentsRepo.findById(s);
            if (optional.isPresent()) {
                studentsRepo.deleteById(s);
            } else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Incorrect Subject Id");
            }
        });
    }
}
