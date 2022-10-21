package com.home.eschool.services;

import com.home.eschool.entity.Payments;
import com.home.eschool.entity.States;
import com.home.eschool.entity.Students;
import com.home.eschool.entity.enums.PaymentTypeEnum;
import com.home.eschool.entity.enums.SetsEnum;
import com.home.eschool.entity.enums.StateEnum;
import com.home.eschool.models.dto.PaymentsDto;
import com.home.eschool.models.payload.PageablePayload;
import com.home.eschool.models.payload.PaymentsPayload;
import com.home.eschool.models.payload.PaymentsStatsPayload;
import com.home.eschool.repository.PaymentsRepo;
import com.home.eschool.utils.Settings;
import com.home.eschool.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentsService {

    private final PaymentsRepo paymentsRepo;
    private final StateService stateService;
    private final AppSettingsService appSettingsService;
    private final StudentsService studentsService;

    public PaymentsService(PaymentsRepo paymentsRepo,
                           StateService stateService,
                           AppSettingsService appSettingsService,
                           StudentsService studentsService) {
        this.paymentsRepo = paymentsRepo;
        this.stateService = stateService;
        this.appSettingsService = appSettingsService;
        this.studentsService = studentsService;
    }

    public PageablePayload getAll(int page, String cdate, String name) {

        States states = stateService.getStateByLabel(StateEnum.ACTIVE);
        UUID studyYearId = appSettingsService.getKeyByLabel(SetsEnum.STUDY_YEAR);

        int size = 10;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        Page<Payments> payments = paymentsRepo.findAllByStateAndStudyYearId(
                PageRequest.of(page, size, Sort.by("createDate")), states, studyYearId);

        if (!Utils.isEmpty(cdate) && !Utils.isEmpty(name)) {
            List<Students> students = studentsService.findStudentsByName(name);

            payments = paymentsRepo.findAllByStateAndStudyYearIdAndPaymentDateStartsWithAndStudentsIn(
                    PageRequest.of(page, size, Sort.by("createDate")), states, studyYearId, cdate, students);

        } else if (!Utils.isEmpty(cdate)) {

            payments = paymentsRepo.findAllByStateAndStudyYearIdAndPaymentDateStartsWith(
                    PageRequest.of(page, size, Sort.by("createDate")), states, studyYearId, cdate);

        } else if (!Utils.isEmpty(name)) {
            List<Students> students = studentsService.findStudentsByName(name);

            payments = paymentsRepo.findAllByStateAndStudyYearIdAndStudentsIn(
                    PageRequest.of(page, size, Sort.by("createDate")), states, studyYearId, students);
        }

        List<PaymentsPayload> list = new ArrayList<>();
        payments.forEach(p -> list.add(
                new PaymentsPayload(
                        p.getId(),
                        studentsService.getStudentsPayload(p.getStudents()),
                        p.getPaymentAmount(),
                        simpleDateFormat.format(p.getPaymentDate())
                )));

        return new PageablePayload(
                payments.getTotalPages(),
                payments.getTotalElements(),
                payments.getSize(),
                list);
    }

    public PaymentsPayload getById(UUID id) {
        return null;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void create(PaymentsDto paymentsDto) {
        Payments payments = new Payments();
        payments.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        payments.setCreateUser(Settings.getCurrentUser());
        payments.setId(UUID.randomUUID());
        payments.setState(stateService.getStateByLabel(StateEnum.ACTIVE));
        payments.setStudyYearId(appSettingsService.getKeyByLabel(SetsEnum.STUDY_YEAR));
        payments.setStudents(studentsService.getStudentById(paymentsDto.getStudentId()));
        payments.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
        payments.setPaymentAmount(paymentsDto.getPaymentAmount());
        payments.setPaymentPurpose(paymentsDto.getPaymentPurpose());
        payments.setPaymentType(paymentsDto.getPaymentType() == 0 ? PaymentTypeEnum.CASH : PaymentTypeEnum.CASH);

        paymentsRepo.save(payments);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void delete(List<UUID> payments) {
        States states = stateService.getStateByLabel(StateEnum.DELETED);
        payments.forEach(s -> {
            Optional<Payments> optional = paymentsRepo.findById(s);
            if (optional.isPresent()) {
                Payments t = optional.get();
                t.setChangeUser(Settings.getCurrentUser());
                t.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
                t.setState(states);

                paymentsRepo.save(t);
            } else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Bunday ID topilmadi !");
            }
        });
    }

    public PaymentsStatsPayload getStats() {
        UUID studyYearId = appSettingsService.getKeyByLabel(SetsEnum.STUDY_YEAR);
        return new PaymentsStatsPayload(
                appSettingsService.getStudyYearsById(studyYearId),
                paymentsRepo.getStatsByStudyYear(studyYearId)
        );
    }
}
