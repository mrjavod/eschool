package com.home.eschool.repository;

import com.home.eschool.entity.Classes;
import com.home.eschool.entity.States;
import com.home.eschool.entity.TeachersSubjectsAndClasses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeachersSubjectsAndClassesRepo extends JpaRepository<TeachersSubjectsAndClasses, UUID> {

    @Query("select t from TeachersSubjectsAndClasses t where t.classes=?1 and t.states=?2")
    Page<TeachersSubjectsAndClasses> list(Pageable pageable,
                                          Classes classes,
                                          States state);
}
