package com.home.eschool.repository;

import com.home.eschool.entity.States;
import com.home.eschool.entity.Students;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentsRepo extends JpaRepository<Students, UUID> {

    Page<Students> findAllByFirstNameContainsOrLastNameContainsOrSureNameContains(Pageable pageable,
                                                                                  String firstName,
                                                                                  String lastName,
                                                                                  String sureName);

    @Query("select t from Students t where t.state = ?1")
    Page<Students> listOfActiveStudents(Pageable pageable, States states);
}
