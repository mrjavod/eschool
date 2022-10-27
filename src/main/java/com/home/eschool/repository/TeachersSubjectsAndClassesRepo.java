package com.home.eschool.repository;

import com.home.eschool.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeachersSubjectsAndClassesRepo extends JpaRepository<TeachersSubjectsAndClasses, UUID> {

    @Query("select t from TeachersSubjectsAndClasses t where t.classes=?1 and t.states=?2")
    Page<TeachersSubjectsAndClasses> list(Pageable pageable,
                                          Classes classes,
                                          States state);

    @Query("select t from TeachersSubjectsAndClasses t " +
            "where t.teachers=?1 and t.subjects=?2 and t.classes=?3 and t.studyYearId=?4")
    Optional<TeachersSubjectsAndClasses> getTeachersSubjectsAndClasses(Teachers teachers,
                                                                       Subjects subjects,
                                                                       Classes classes,
                                                                       UUID studyYearId);

    @Query("select t from TeachersSubjectsAndClasses t " +
            "where t.teachers=?1 and t.states=?2 and t.studyYearId=?3 \n" +
            "order by t.classes")
    List<TeachersSubjectsAndClasses> getTeachersClasses(Teachers teachers,
                                                        States states,
                                                        UUID studyYearId);

    @Query("select t from TeachersSubjectsAndClasses t " +
            "where t.teachers=?1 and t.states=?2 and t.studyYearId=?3 and t.classes=?4")
    List<TeachersSubjectsAndClasses> getTeacherSubjectsByClass(Teachers teachers,
                                                               States states,
                                                               UUID studyYearId,
                                                               Classes classes);

}
