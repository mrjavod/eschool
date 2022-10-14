package com.home.eschool.services;

import com.home.eschool.entity.States;
import com.home.eschool.entity.Teachers;
import com.home.eschool.entity.enums.StateEnum;
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
import java.text.ParseException;
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
    private final StateService stateService;

    public TeachersService(TeachersRepo teachersRepo,
                           UserService userService,
                           FilesService filesService,
                           StateService stateService) {
        this.teachersRepo = teachersRepo;
        this.userService = userService;
        this.filesService = filesService;
        this.stateService = stateService;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(TeachersDto teacher) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

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
        try {
            teachers.setDateOfBirth(new Date(simpleDateFormat.parse(teacher.getDateOfBirth()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        teachers.setAddress(teacher.getAddress());
        teachers.setEmail(teacher.getEmail());
        teachers.setPhoneNumber(teacher.getPhoneNumber());
        teachers.setProfile(userService.createProfile(teachers));

        teachers.setSecondPhoneNumber(teacher.getSecondPhoneNumber());
        teachers.setPnfl(teacher.getPnfl());
        teachers.setReference_086_id(teacher.getReference_086_id());
        teachers.setCovid_test_id(teacher.getCovid_test_id());
        teachers.setSecond_diploma_id(teacher.getSecond_diploma_id());
        teachers.setStates(stateService.getStateByLabel(StateEnum.ACTIVE));

        teachersRepo.save(teachers);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(TeachersDto teacher) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

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

        try {
            teachers.setDateOfBirth(new Date(simpleDateFormat.parse(teacher.getDateOfBirth()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        teachers.setAddress(teacher.getAddress());
        teachers.setEmail(teacher.getEmail());
        teachers.setPhoneNumber(teacher.getPhoneNumber());

        teachers.setSecondPhoneNumber(teacher.getSecondPhoneNumber());
        teachers.setPnfl(teacher.getPnfl());
        teachers.setReference_086_id(teacher.getReference_086_id());
        teachers.setCovid_test_id(teacher.getCovid_test_id());
        teachers.setSecond_diploma_id(teacher.getSecond_diploma_id());

        userService.updateProfile(teachers);
        teachersRepo.save(teachers);
    }

    @Override
    public PageablePayload getAll(int page, String search) {

        int size = 10;
        List<TeachersPayload> list = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        States states = stateService.getStateByLabel(StateEnum.ACTIVE);

        Page<Teachers> teachers = teachersRepo.findAllByStatesAndFirstNameContainsOrLastNameContainsOrSureNameContains(
                PageRequest.of(page, size, Sort.by("lastName")), states, search, search, search);

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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

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
                teacher.getPnfl(),
                teacher.getSecondPhoneNumber(),
                teacher.getPassportSeries(),
                teacher.getPassportNumber(),
                teacher.getAddress(),
                filesService.getFileInfo(teacher.getDiploma_id()),
                filesService.getFileInfo(teacher.getSecond_diploma_id()),
                filesService.getFileInfo(teacher.getPassport_id()),
                filesService.getFileInfo(teacher.getAvatar_id()),
                filesService.getFileInfo(teacher.getCovid_test_id()),
                filesService.getFileInfo(teacher.getReference_086_id())
        );
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(List<UUID> teachers) {
        States states = stateService.getStateByLabel(StateEnum.DELETED);
        teachers.forEach(s -> {
            Optional<Teachers> optional = teachersRepo.findById(s);
            if (optional.isPresent()) {
                Teachers t = optional.get();
                t.setStates(states);

                teachersRepo.save(t);
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
