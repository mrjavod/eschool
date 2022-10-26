package com.home.eschool.repository;

import com.home.eschool.entity.Classes;
import com.home.eschool.entity.States;
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
public interface ClassesRepo extends JpaRepository<Classes, UUID> {

    @Query("select t from Classes t where t.state = ?1")
    Page<Classes> list(Pageable pageable, States state);

    Page<Classes> findAllByNameContains(Pageable pageable, String search);

    List<Classes> findAllByStateLabel(StateEnum stateEnum);

    Optional<Classes> findByName(String name);
}
