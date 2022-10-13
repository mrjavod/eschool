package com.home.eschool.services;

import com.home.eschool.entity.Teachers;
import com.home.eschool.models.dto.TeachersDto;
import com.home.eschool.models.payload.PageablePayload;
import com.home.eschool.models.payload.TeachersPayload;
import com.home.eschool.models.payload.TeachersPayloadDetails;
import com.home.eschool.repository.TeachersRepo;
import com.home.eschool.services.interfaces.CrudInterface;
import com.home.eschool.utils.Settings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class TeachersService implements CrudInterface<TeachersDto, TeachersPayload> {

    private final TeachersRepo teachersRepo;
    private final UserService userService;
    private final FilesService filesService;

    public TeachersService(TeachersRepo teachersRepo,
                           UserService userService,
                           FilesService filesService) {
        this.teachersRepo = teachersRepo;
        this.userService = userService;
        this.filesService = filesService;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
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

    @Override
    @Transactional(rollbackFor = Throwable.class)
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

    @Override
    public PageablePayload getAll(int page, String search) {

        int size = 10;
        List<TeachersPayload> list = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Page<Teachers> teachers = teachersRepo.findAllByFirstNameContains(
                PageRequest.of(page, size, Sort.by("lastName")), search);

        teachers.forEach(t -> list.add(
                new TeachersPayload(
                        t.getId(),
                        t.getFirstName(),
                        t.getLastName(),
                        t.getSureName(),
                        simpleDateFormat.format(t.getDateOfBirth()),
                        t.getPhoneNumber(),
                        t.getEmail())));

        return new PageablePayload(
                teachers.getTotalPages(),
                teachers.getTotalElements(),
                teachers.getSize(),
                list);
    }

    @Override
    public TeachersPayloadDetails getById(UUID id) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Teachers teacher = teachersRepo.findById(id).orElse(null);
        if (teacher == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect Teacher Id");
        }

        return new TeachersPayloadDetails(
                teacher.getId(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getSureName(),
                simpleDateFormat.format(teacher.getDateOfBirth()),
                teacher.getPhoneNumber(),
                teacher.getEmail(),
                teacher.getInn(),
                teacher.getInps(),
                teacher.getPassportSeries(),
                teacher.getPassportNumber(),
                teacher.getAddress(),
                filesService.getFileInfo(teacher.getDiploma_id()),
                filesService.getFileInfo(teacher.getPassport_id()),
                filesService.getFileInfo(teacher.getAvatar_id())
        );
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(List<UUID> teachers) {
        teachers.forEach(s -> {
            Optional<Teachers> optional = teachersRepo.findById(s);
            if (optional.isPresent()) {
                teachersRepo.deleteById(s);
                //userService.deleteUser(optional.get().getProfile());
            } else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Incorrect Subject Id");
            }
        });
    }

    public Teachers findById(UUID teacherId) {
        if (teacherId == null) {
            return null;
        }

        return teachersRepo.findById(teacherId).orElse(null);
    }
}
