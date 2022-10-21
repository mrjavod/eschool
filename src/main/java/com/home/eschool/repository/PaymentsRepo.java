package com.home.eschool.repository;

import com.home.eschool.entity.Payments;
import com.home.eschool.entity.States;
import com.home.eschool.entity.Students;
import com.home.eschool.models.payload.MonthlyPayments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PaymentsRepo extends JpaRepository<Payments, UUID> {

    Page<Payments> findAllByStateAndStudyYearId(
            Pageable pageable,
            States states,
            UUID studyYearId
    );

    Page<Payments> findAllByStateAndStudyYearIdAndPaymentDateStartsWith(
            Pageable pageable,
            States states,
            UUID studyYearId,
            String paymentDate
    );

    Page<Payments> findAllByStateAndStudyYearIdAndStudentsIn(
            Pageable pageable,
            States states,
            UUID studyYearId,
            List<Students> studentsList
    );

    Page<Payments> findAllByStateAndStudyYearIdAndPaymentDateStartsWithAndStudentsIn(
            Pageable pageable,
            States states,
            UUID studyYearId,
            String paymentDate,
            List<Students> studentsList
    );

    @Query(nativeQuery = true,
            value = "select sum(p.payment_amount), date_part('month', p.payment_date) as month from payments p \n" +
                    "where p.study_year_id = ?1\n" +
                    "group by date_part('month', p.payment_date)\n")
    List<MonthlyPayments> getStatsByStudyYear(UUID studyYearId);
}
