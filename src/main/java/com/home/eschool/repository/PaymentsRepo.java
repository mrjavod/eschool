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

    @Query(nativeQuery = true,
            value = "select t.* from payments t " +
                    "where to_char(t.payment_date, 'yyyy-mm-dd') = ?3 and t.study_year_id = ?2 and t.state_id = ?1",
            countQuery = "select count(*) from payments t")
    Page<Payments> findAllByPaymentDate(
            Pageable pageable,
            UUID stateId,
            UUID studyYearId,
            String paymentDate
    );

    Page<Payments> findAllByStateAndStudyYearIdAndStudentsIn(
            Pageable pageable,
            States states,
            UUID studyYearId,
            List<Students> studentsList
    );

    @Query(nativeQuery = true,
            value = "select t.* from payments t " +
                    "where to_char(t.payment_date, 'yyyy-mm-dd') = ?3" +
                    " and t.study_year_id = ?2" +
                    " and t.state_id = ?1 " +
                    " and t.student_id in (?4)",
            countQuery = "select count(*) from payments t")
    Page<Payments> findAllByCdateAndName(
            Pageable pageable,
            UUID stateId,
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
