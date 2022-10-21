package com.home.eschool.repository;

import com.home.eschool.entity.States;
import com.home.eschool.entity.Students;
import com.home.eschool.entity.enums.StateEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentsRepo extends JpaRepository<Students, UUID> {

    @Query("select t from Students t " +
            "where lower(concat(t.lastName, ' ', t.firstName, ' ', t.sureName)) like %?1% " +
            "   or lower(concat(t.firstName, ' ', t.lastName, ' ', t.sureName)) like %?1% ")
    List<Students> findAllByName(String name);

    @Query("select t from Students t where t.state = ?1")
    Page<Students> listOfActiveStudents(Pageable pageable, States states);

    List<Students> findAllByStateLabel(StateEnum active);
}
