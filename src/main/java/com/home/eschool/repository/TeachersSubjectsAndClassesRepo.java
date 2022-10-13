package com.home.eschool.repository;

import com.home.eschool.entity.TeachersSubjectsAndClasses;
import com.home.eschool.models.payload.TeachersSubjectsAndClassesPayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeachersSubjectsAndClassesRepo extends JpaRepository<TeachersSubjectsAndClasses, UUID> {

    @Query(nativeQuery = true,
            value = "select u from TeachersSubjectsAndClasses u \n" +
                    "inner join teachers t on t.id = u.teacher_id \n" +
                    "inner join subjects s on s.id = u.subject_id \n" +
                    "inner join classes c on c.id = u.class_id \n" +
                    "where u.state_id='ACTIVE'")
    Page<TeachersSubjectsAndClasses> findAll(Pageable pageable, String search);
}
