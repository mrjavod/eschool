package com.home.eschool.services;

import com.home.eschool.entity.Teachers;
import com.home.eschool.models.dto.TeachersDto;
import com.home.eschool.models.payload.TeachersPayload;
import com.home.eschool.models.payload.TeachersPayloadDetails;
import com.home.eschool.repository.TeachersRepo;
import com.home.eschool.utils.Settings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class TeachersService {

    private final TeachersRepo teachersRepo;
    private final UserService userService;

    public TeachersService(TeachersRepo teachersRepo,
                           UserService userService) {
        this.teachersRepo = teachersRepo;
        this.userService = userService;
    }

    public void create(TeachersDto teacher) {
        Teachers teachers = new Teachers();
        teachers.setId(UUID.randomUUID());
        teachers.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        teachers.setCreateUser(Settings.getCurrentUser());
        teachers.setFirstName(teacher.getFirstName());
        teachers.setLastName(teacher.getLastName());
        teachers.setSureName(teacher.getSureName());
        teachers.setInn(teacher.getInn());
        teachers.setInps(teacher.getInps());
        teachers.setDiploma_id(teacher.getDiploma_id());
        teachers.setPassport_id(teacher.getPassport_id());
        teachers.setAvatar_id(teacher.getAvatar_id());
        teachers.setPassportSeries(teacher.getPassportSeries());
        teachers.setPassportNumber(teacher.getPassportNumber());
        teachers.setDateOfBirth(Date.valueOf(teacher.getDateOfBirth()));
        teachers.setAddress(teacher.getAddress());
        teachers.setEmail(teacher.getEmail());
        teachers.setPhoneNumber(teacher.getPhoneNumber());
        teachers.setProfile(userService.createProfile(teachers));

        teachersRepo.save(teachers);
    }

    public void update(TeachersDto teacher) {
        Teachers teachers = teachersRepo.findById(teacher.getId()).orElse(null);

        if (teachers == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect Teacher Id");
        }

        teachers.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
        teachers.setChangeUser(Settings.getCurrentUser());
        teachers.setFirstName(teacher.getFirstName());
        teachers.setLastName(teacher.getLastName());
        teachers.setSureName(teacher.getSureName());
        teachers.setInn(teacher.getInn());
        teachers.setInps(teacher.getInps());
        teachers.setDiploma_id(teacher.getDiploma_id());
        teachers.setPassport_id(teacher.getPassport_id());
        teachers.setAvatar_id(teacher.getAvatar_id());
        teachers.setPassportSeries(teacher.getPassportSeries());
        teachers.setPassportNumber(teacher.getPassportNumber());
        teachers.setDateOfBirth(Date.valueOf(teacher.getDateOfBirth()));
        teachers.setAddress(teacher.getAddress());
        teachers.setEmail(teacher.getEmail());
        teachers.setPhoneNumber(teacher.getPhoneNumber());

        userService.updateProfile(teachers);
        teachersRepo.save(teachers);
    }

    public List<TeachersPayload> getAll() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");

        List<TeachersPayload> list = new ArrayList<>();
        teachersRepo.findAll().forEach(t -> {
            list.add(
                    new TeachersPayload(
                            t.getId(),
                            t.getFirstName(),
                            t.getLastName(),
                            t.getSureName(),
                            simpleDateFormat.format(t.getDateOfBirth()),
                            t.getPhoneNumber(),
                            t.getEmail()));
        });

        return list;
    }

    public TeachersPayloadDetails getById(UUID id) {
        return null;
    }
}
