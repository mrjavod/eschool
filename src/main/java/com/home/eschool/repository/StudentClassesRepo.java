package com.home.eschool.repository;

import com.home.eschool.entity.Classes;
import com.home.eschool.entity.States;
import com.home.eschool.entity.StudentClasses;
import com.home.eschool.entity.Students;
import com.home.eschool.entity.enums.StateEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentClassesRepo extends JpaRepository<StudentClasses, UUID> {

    Optional<StudentClasses> findByStudentsAndStates_Label(Students students, StateEnum label);

    List<StudentClasses> findByClassesAndStatesAndStudyYearId(Classes classes,
                                                              States states,
                                                              UUID studyYear);
}
