package com.home.eschool.repository;

import com.home.eschool.entity.States;
import com.home.eschool.entity.Subjects;
import com.home.eschool.entity.enums.StateEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubjectsRepo extends JpaRepository<Subjects, UUID> {

    Optional<Subjects> findByName(String name);

    Page<Subjects> findAllByNameContains(Pageable pageable, String name);

    @Query("select t from Subjects t where t.state = ?1")
    Page<Subjects> list(Pageable pageable, States state);

    List<Subjects> findAllByStateLabel(StateEnum stateEnum);
}
