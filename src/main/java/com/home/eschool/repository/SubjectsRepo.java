package com.home.eschool.repository;

import com.home.eschool.entity.Subjects;
import com.home.eschool.entity.enums.StateEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubjectsRepo extends JpaRepository<Subjects, UUID> {

    Page<Subjects> findAllByNameContains(Pageable pageable, String name);

    List<Subjects> findAllByStateLabel(StateEnum stateEnum);
}
