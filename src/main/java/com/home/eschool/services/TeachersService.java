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
    public void create(TeachersDto teachersDto) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Optional<Teachers> t = teachersRepo.findByInn(teachersDto.getInn());
        if (t.isPresent() && t.get().getState().getLabel().equals(StateEnum.ACTIVE)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bunda INN ga ega ustoz tizimda mavjud !");
        } else if (setActive(t)) return;

        t = teachersRepo.findByPassportNumber(teachersDto.getPassportNumber());
        if (t.isPresent() && t.get().getState().getLabel().equals(StateEnum.ACTIVE)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bunda PASPORT RAQAM ga ega ustoz tizimda mavjud !");
        } else if (setActive(t)) return;

        t = teachersRepo.findByPhoneNumber(teachersDto.getPassportNumber());
        if (t.isPresent() && t.get().getState().getLabel().equals(StateEnum.ACTIVE)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bunda TELEFON RAQAM ga ega ustoz tizimda mavjud !");
        } else if (setActive(t)) return;

        Teachers teachers = new Teachers();
        teachers.setId(UUID.randomUUID());
        teachers.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        teachers.setCreateUser(Settings.getCurrentUser());
        teachers.setFirstName(teachersDto.getFirstName());
        teachers.setLastName(teachersDto.getLastName());
        teachers.setSureName(teachersDto.getSureName());
        teachers.setInn(teachersDto.getInn());
        teachers.setInps(teachersDto.getInps());
        teachers.setDiploma_id(teachersDto.getDiploma_id());
        teachers.setPassport_id(teachersDto.getPassport_id());
        teachers.setAvatar_id(teachersDto.getAvatar_id());
        teachers.setPassportSeries(teachersDto.getPassportSeries());
        teachers.setPassportNumber(teachersDto.getPassportNumber());
        try {
            teachers.setDateOfBirth(new Date(simpleDateFormat.parse(teachersDto.getDateOfBirth()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        teachers.setAddress(teachersDto.getAddress());
        teachers.setEmail(teachersDto.getEmail());
        teachers.setPhoneNumber(teachersDto.getPhoneNumber());
        teachers.setProfile(userService.createProfile(teachers));

        teachers.setSecondPhoneNumber(teachersDto.getSecondPhoneNumber());
        teachers.setPnfl(teachersDto.getPnfl());
        teachers.setReference_086_id(teachersDto.getReference_086_id());
        teachers.setCovid_test_id(teachersDto.getCovid_test_id());
        teachers.setSecond_diploma_id(teachersDto.getSecond_diploma_id());
        teachers.setState(stateService.getStateByLabel(StateEnum.ACTIVE));

        teachersRepo.save(teachers);
    }

    private boolean setActive(Optional<Teachers> t) {
        if (t.isPresent() && t.get().getState().getLabel().equals(StateEnum.DELETED)) {
            Teachers teacher = t.get();
            teacher.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
            teacher.setChangeUser(Settings.getCurrentUser());
            teacher.setState(stateService.getStateByLabel(StateEnum.ACTIVE));
            teachersRepo.save(teacher);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(TeachersDto teachersDto) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Teachers teachers = teachersRepo.findById(teachersDto.getId()).orElse(null);
        if (teachers == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bunday ID ga ega ustoz topilmadi !");
        }

        Optional<Teachers> t = teachersRepo.findByInn(teachersDto.getInn());
        if (t.isPresent() && t.get().getState().getLabel().equals(StateEnum.ACTIVE) &&
                !t.get().getId().equals(teachers.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bunda INN ga ega ustoz tizimda mavjud !");
        }

        t = teachersRepo.findByPassportNumber(teachersDto.getPassportNumber());
        if (t.isPresent() && t.get().getState().getLabel().equals(StateEnum.ACTIVE) &&
                !t.get().getId().equals(teachers.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bunda PASPORT RAQAM ga ega ustoz tizimda mavjud !");
        }

        t = teachersRepo.findByPhoneNumber(teachersDto.getPassportNumber());
        if (t.isPresent() && t.get().getState().getLabel().equals(StateEnum.ACTIVE) &&
                !t.get().getId().equals(teachers.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bunda TELEFON RAQAM ga ega ustoz tizimda mavjud !");
        }

        teachers.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
        teachers.setChangeUser(Settings.getCurrentUser());
        teachers.setFirstName(teachersDto.getFirstName());
        teachers.setLastName(teachersDto.getLastName());
        teachers.setSureName(teachersDto.getSureName());
        teachers.setInn(teachersDto.getInn());
        teachers.setInps(teachersDto.getInps());
        teachers.setDiploma_id(teachersDto.getDiploma_id());
        teachers.setPassport_id(teachersDto.getPassport_id());
        teachers.setAvatar_id(teachersDto.getAvatar_id());
        teachers.setPassportSeries(teachersDto.getPassportSeries());
        teachers.setPassportNumber(teachersDto.getPassportNumber());

        try {
            teachers.setDateOfBirth(new Date(simpleDateFormat.parse(teachersDto.getDateOfBirth()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        teachers.setAddress(teachersDto.getAddress());
        teachers.setEmail(teachersDto.getEmail());
        teachers.setPhoneNumber(teachersDto.getPhoneNumber());

        teachers.setSecondPhoneNumber(teachersDto.getSecondPhoneNumber());
        teachers.setPnfl(teachersDto.getPnfl());
        teachers.setReference_086_id(teachersDto.getReference_086_id());
        teachers.setCovid_test_id(teachersDto.getCovid_test_id());
        teachers.setSecond_diploma_id(teachersDto.getSecond_diploma_id());

        userService.updateProfile(teachers);
        teachersRepo.save(teachers);
    }

    @Override
    public PageablePayload getAll(int page, String search) {

        int size = 10;
        List<TeachersPayload> list = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        States states = stateService.getStateByLabel(StateEnum.ACTIVE);

        Page<Teachers> teachers = teachersRepo.listOfActiveTeachers(
                PageRequest.of(page, size, Sort.by("lastName")), states);

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
                t.setChangeUser(Settings.getCurrentUser());
                t.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
                t.setState(states);

                teachersRepo.save(t);
                userService.deleteUser(optional.get().getProfile());
            } else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Bunday ID ga ega ustoz topilmadi !");
            }
        });
    }

    public Teachers findById(UUID teacherId) {
        if (teacherId == null) {
            return null;
        }

        return teachersRepo.findById(teacherId).orElse(null);
    }

    public List<Teachers> getAllTeachers() {
        return teachersRepo.findAllByStateLabel(StateEnum.ACTIVE);
    }
}
